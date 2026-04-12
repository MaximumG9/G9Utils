package com.maximumg9.g9utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.render.CameraOverride;
import net.minecraft.client.render.DefaultFramebufferSet;
import net.minecraft.client.render.FrameGraphBuilder;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.Handle;
import net.minecraft.client.util.Pool;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector3f;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Unique;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class CylinderRenderHelper {
    public static final int MAX_CYLINDER_DIVISIONS = 5;
    private static final int RES_SCALE = 1;

    private static Framebuffer[] buffers = new Framebuffer[]{};
    private static Framebuffer finalBuffer = null;
    private static Framebuffer[] emptyBuffers = new Framebuffer[]{};

    private static final Identifier CURVING = G9utils.id("curving");
    private static final Identifier COMBINING = G9utils.id("combining");

    private static final Pool POOL = new Pool(3);

    public static void takeCylindricalScrenshot(File runDirectory, Consumer<Text> messageReceiver) {
        MinecraftClient cl = MinecraftClient.getInstance();
        if(cl.player == null) {
            messageReceiver.accept(Text.of("Could not take screenshot"));
            return;
        }

        int divisions = G9utils.opt().rendering.opt().cylindricalDivisions;

        int preFramebufferWidth = cl.getWindow().getFramebufferWidth();
        int preFramebufferHeight = cl.getWindow().getFramebufferHeight();
        Framebuffer normalFramebuffer = cl.getFramebuffer();
        float prePitch = cl.player.getPitch();
        float preYaw = cl.player.getYaw();
        float preLastPitch = cl.player.lastPitch;
        float preLastYaw = cl.player.lastYaw;
        cl.gameRenderer.setBlockOutlineEnabled(false);

        try {
            cl.gameRenderer.setCameraOverride(
                new CameraOverride(
                    new Vector3f(cl.gameRenderer.getCamera().getHorizontalPlane())
                )
            );

            setupBuffers(cl, divisions, MathHelper.TAU / divisions, preFramebufferWidth, preFramebufferHeight);

            rotateAndRenderIntoBuffers(cl,divisions);

            combineIntoFinalBuffer(cl);

            ScreenshotRecorder.saveScreenshot(
                runDirectory,
                getScreenshotFilename(new File(runDirectory,ScreenshotRecorder.SCREENSHOTS_DIRECTORY)),
                finalBuffer, 1,
                messageReceiver
            );
        } catch (Exception e) {
            messageReceiver.accept(
                Text.literal("Error while taking cylindrical screenshot")
                    .styled(style -> style.withColor(Formatting.RED))
            );
        } finally {
            cl.framebuffer = normalFramebuffer;

            cl.player.setPitch(prePitch);
            cl.player.setYaw(preYaw);
            cl.player.lastPitch = preLastPitch;
            cl.player.lastYaw = preLastYaw;
            cl.gameRenderer.setBlockOutlineEnabled(true);
            cl.getWindow().setFramebufferWidth(preFramebufferWidth);
            cl.getWindow().setFramebufferHeight(preFramebufferHeight);
            cl.gameRenderer.setCameraOverride(null);
            ((GameRendererMixinDuck) cl.gameRenderer).g9Utils$setFOVOverride(null);
        }
    }

    private static void rotateAndRenderIntoBuffers(MinecraftClient cl, int divisions) {
        float hFOVdegrees = (360f / divisions);

        float startingYaw = cl.player.getYaw();

        PostEffectProcessor curveEffect = cl.getShaderLoader().loadPostEffect(CURVING, DefaultFramebufferSet.MAIN_ONLY);
        Objects.requireNonNull(cl.player);
        if (curveEffect != null) {
            for (int i = 0; i < divisions; i++) {
                cl.framebuffer = buffers[i];

                cl.player.setPitch(0f);
                cl.player.setYaw(startingYaw + i * hFOVdegrees);

                cl.player.lastYaw = cl.player.getYaw();
                cl.player.lastPitch = cl.player.getPitch();

                cl.gameRenderer.updateCamera(RenderTickCounter.ONE);
                cl.gameRenderer.renderWorld(RenderTickCounter.ONE);

                curveEffect.render(
                    cl.framebuffer,
                    POOL
                );
            }
        }
    }

    private static void combineIntoFinalBuffer(MinecraftClient cl) {
        FrameGraphBuilder builder = new FrameGraphBuilder();

        HashMap<Identifier, Handle<Framebuffer>> map = new HashMap<>();

        for (int i = 0; i < MAX_CYLINDER_DIVISIONS; i++) {
            if (i < buffers.length) {
                int remappedIndex = (i + (buffers.length/2)) % buffers.length;
                map.put(
                    G9utils.id("i" + remappedIndex),
                    builder.createObjectNode("i" + remappedIndex, buffers[i])
                );
            } else {
                map.put(
                    G9utils.id("i" + i),
                    builder.createObjectNode("i" + i, emptyBuffers[i])
                );
            }
        }
        map.put(
            PostEffectProcessor.MAIN,
            builder.createObjectNode("main", finalBuffer)
        );

        PostEffectProcessor combiningEffect = cl.getShaderLoader().loadPostEffect(COMBINING, map.keySet());
        if (combiningEffect != null) {
            combiningEffect.render(
                builder,
                finalBuffer.textureWidth,
                finalBuffer.textureHeight,
                toImmutableBufferSet(map)
            );

            builder.run(POOL);
        }
    }

    @Unique
    private static void setupBuffers(MinecraftClient cl, int divisions, double hFOVRad, int baseFramebufferWidth, int baseFramebufferHeight) {
        double vScale = 1/Math.cos(hFOVRad/2);

        double newAspectRatio = ((double) baseFramebufferHeight / baseFramebufferWidth) * vScale;

        double vFOVrad = (Math.atan(Math.tan(hFOVRad/2) * newAspectRatio) * 2);
        float vFOVdegrees = (float) (180 * vFOVrad/MathHelper.PI);

        int framebufferWidth = baseFramebufferWidth/RES_SCALE;
        int framebufferHeight = (int) (vScale * baseFramebufferHeight /RES_SCALE);

        ((GameRendererMixinDuck) cl.gameRenderer).g9Utils$setFOVOverride(vFOVdegrees);

        if(buffers.length != divisions) {
            for(Framebuffer buf : buffers) {
                buf.delete();
            }
            buffers = new Framebuffer[divisions];
            for(int i=0;i<divisions;i++) {
                buffers[i] = new SimpleFramebuffer(null,framebufferWidth,framebufferHeight,true);
            }
        }

        cl.getWindow().setFramebufferWidth(framebufferWidth);
        cl.getWindow().setFramebufferHeight(framebufferHeight);

        if(finalBuffer == null || framebufferWidth * divisions != finalBuffer.textureWidth) {
            if(finalBuffer != null) {
                finalBuffer.delete();
            }
            finalBuffer = new SimpleFramebuffer("final",framebufferWidth * divisions, framebufferHeight,false);
        }

        if (emptyBuffers.length != MAX_CYLINDER_DIVISIONS) {
            for (Framebuffer emptyBuffer : emptyBuffers) {
                emptyBuffer.delete();
            }
            emptyBuffers = new Framebuffer[MAX_CYLINDER_DIVISIONS];
            for (int i = 0; i < MAX_CYLINDER_DIVISIONS; i++) {
                emptyBuffers[i] = new SimpleFramebuffer(null, 1, 1, false);
            }
        }
    }

    @Unique
    private static PostEffectProcessor.FramebufferSet toImmutableBufferSet(Map<Identifier,Handle<Framebuffer>> bufferMap) {
        return new PostEffectProcessor.FramebufferSet() {
            @Override
            public void set(Identifier id, Handle<Framebuffer> framebuffer) {
                bufferMap.put(id,framebuffer);
            }

            @Override
            public @Nullable Handle<Framebuffer> get(Identifier id) {
                return bufferMap.get(id);
            }
        };
    }

    private static String getScreenshotFilename(File directory) {
        String screenshotName = ScreenshotRecorder.getScreenshotFilename(directory).getName();
        return "cylinder_" + screenshotName;
    }
}
