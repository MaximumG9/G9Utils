package com.maximumg9.g9utils.renderers;

import com.maximumg9.g9utils.G9utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Box;

import java.util.LinkedList;
import java.util.Objects;

public class LaggedHitboxRenderer implements DebugRenderer.Renderer {

    private final MinecraftClient client;
    // Box and System.nanoTime() timestamp to discard
    private final LinkedList<Pair<Box,Long>> boxes = new LinkedList<>();

    public LaggedHitboxRenderer(MinecraftClient client) {
        this.client = client;
    }

    private static final long MS_TO_NS = 1000000L;

    public void packet(PlayerMoveC2SPacket packet) {
        if(this.client.player == null) return;

        if(!packet.changesPosition()) return;

        long lag = G9utils.getOptions().lag * MS_TO_NS;

        Box bb = this.client.player.getBoundingBox();

        this.boxes.add(
            new Pair<>(bb,System.nanoTime() + lag)
        );
    }

    @Override
    public void clear() {
        this.boxes.clear();
    }

    @Override
    public void render(
        MatrixStack matrices,
        VertexConsumerProvider vertexConsumers,
        double cameraX,
        double cameraY,
        double cameraZ
    ) {
        if(boxes.isEmpty()) return;
        long time = System.nanoTime();
        while(
            Objects.requireNonNull(
                boxes.peekLast()
            ).getRight() > time
        ) {
            boxes.removeLast();
        }

        Pair<Box,Long> last = boxes.peekLast();

        if(last == null) return;

        WorldRenderer.drawBox(
            matrices,
            vertexConsumers.getBuffer(
                RenderLayer.getLines()
            ),
            last.getLeft(),
            1.0f,
            1.0f,
            1.0f,
            1.0f
        );
    }
}
