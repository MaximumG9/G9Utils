package com.maximumg9.g9utils.renderers;

import com.maximumg9.g9utils.ClientCommonNetworkHandlerMixinDuck;
import com.maximumg9.g9utils.G9utils;
import com.maximumg9.g9utils.InGameHudDuck;
import com.maximumg9.g9utils.PlayerMixinDuck;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class G9HudLayer {
    private final List<Value> values = new ArrayList<>();

    private final TextRenderer textRenderer;

    public G9HudLayer(TextRenderer textRenderer) {
        this.textRenderer = textRenderer;
    }

    public void addValue(Supplier<Text> getter, Text name, Supplier<Boolean> shouldRender) {
        values.add(new Value(getter,name, shouldRender));
    }

    public void render(int mouseX, int mouseY, DrawContext context, RenderTickCounter tickCounter) {
        int y = 0;

        for(Value value : values) {
            if(!value.shouldRender.get()) continue;
            Text text = value.name.copy().append(value.getter().get());

            int width = textRenderer.getWidth(text);

            int x = context.getScaledWindowWidth() - width;

            TextWidget textWidget = new TextWidget(x,
                y,
                width,
                textRenderer.fontHeight,
                text,
                textRenderer
            );
            textWidget.render(context, mouseX, mouseY, tickCounter.getDynamicDeltaTicks());
            context.drawText(textRenderer, text, x, y, Colors.WHITE, true);
            y += textRenderer.fontHeight;
        }

        context.drawDeferredElements();
    }
    
    @SuppressWarnings("MalformedFormatString")
    public static void initHUD(MinecraftClient client, InGameHudDuck hud) {
        hud.g9Utils$addValue(
            () -> {
                if(client.player == null) return Text.literal("");

                float s = MathHelper.sin(client.player.getYaw() * 0.017453292F);

                    return Text.literal(
                        String.format(
                            "%." + G9utils.opt().hudOptions.yawDecimalPlaces + "f",
                            s
                        )
                    );
                },
                Text.literal("sin(yaw):"),
                () -> G9utils.opt().hudOptions.seeCosAndSinForYaw
            );

        hud.g9Utils$addValue(
            () -> {
                if(client.player == null) return Text.literal("");

                float c = MathHelper.cos(client.player.getYaw() * 0.017453292F);

                return Text.literal(
                    String.format(
                        "%." + G9utils.opt().hudOptions.yawDecimalPlaces + "f",
                        c
                    )
                );
            },
            Text.literal("cos(yaw):"),
            () -> G9utils.opt().hudOptions.seeCosAndSinForYaw
        );
        hud.g9Utils$addValue(
            () -> {
                if(client.player == null) return Text.literal("");

                float radYaw = client.player.getYaw() * 0.017453292F;

                double degRadYaw = ((double)radYaw) * 180.0/Math.PI;

                return Text.literal(
                    String.format(
                        "%." + G9utils.opt().hudOptions.yawDecimalPlaces + "f",
                        degRadYaw
                    )
                );
            },
            Text.literal("radian rounded yaw:"),
            () -> G9utils.opt().hudOptions.seeRadianRoundedYaw
        );
        hud.g9Utils$addValue(
            () -> {
                if(client.player == null) return Text.literal("");

                return Text.literal(
                    String.format(
                        "%." + G9utils.opt().hudOptions.yawDecimalPlaces + "f",
                        client.player.getYaw()
                    )
                );
            },
            Text.literal("yaw:"),
            () -> G9utils.opt().hudOptions.seeAccurateYaw
        );

        hud.g9Utils$addValue(
            () -> {
                if(client.player == null) return Text.literal("");

                return Text.literal(String.valueOf(client.player.isOnGround()));
            },
            Text.literal("[c]grounded:"),
            () -> G9utils.opt().hudOptions.seeOnGround
        );
        hud.g9Utils$addValue(
            () -> {
                if(client.player == null) return Text.literal("");

                return Text.literal(String.valueOf(((PlayerMixinDuck)client.player).g9Utils$wasAirborneLastFrame()));
            },
            Text.literal("[c]wasOnGround:"),
            () -> G9utils.opt().hudOptions.seeOnGround && G9utils.opt().cheats.quakeAir
        );
        hud.g9Utils$addValue(
            () -> {
                if(client.getServer() == null) return Text.literal("");

                if(client.player == null) return Text.literal("");

                ServerPlayerEntity p = client.getServer().getPlayerManager().getPlayer(client.player.getUuid());

                if(p == null) return Text.literal("");

                return Text.literal(String.valueOf(p.isOnGround()));
            },
            Text.literal("[s]grounded:"),
            () -> G9utils.opt().hudOptions.seeOnGround && client.getServer() != null
        );

        hud.g9Utils$addValue(
            () -> {
                if(client.player == null) return Text.literal("");

                return Text.literal(
                    String.format(
                        "%." + G9utils.opt().hudOptions.posDecimalPlaces + "f," +
                        "%." + G9utils.opt().hudOptions.posDecimalPlaces + "f," +
                        "%." + G9utils.opt().hudOptions.posDecimalPlaces + "f",
                        client.player.getX(),
                        client.player.getY(),
                        client.player.getZ()
                    )
                );
            },
            Text.literal("[c]pos:"),
            () -> G9utils.opt().hudOptions.seePos
        );
        hud.g9Utils$addValue(
            () -> {
                if(client.getServer() == null) return Text.literal("");

                if(client.player == null) return Text.literal("");

                ServerPlayerEntity p = client.getServer().getPlayerManager().getPlayer(client.player.getUuid());

                if(p == null) return Text.literal("");

                return Text.literal(
                    String.format(
                        "%." + G9utils.opt().hudOptions.posDecimalPlaces + "f," +
                        "%." + G9utils.opt().hudOptions.posDecimalPlaces + "f," +
                        "%." + G9utils.opt().hudOptions.posDecimalPlaces + "f",
                        p.getX(),
                        p.getY(),
                        p.getZ()
                    )
                );
            },
            Text.literal("[s]pos:"),
            () -> G9utils.opt().hudOptions.seePos && client.getServer() != null
        );

        hud.g9Utils$addValue(
            () -> {
                if(client.player == null) return Text.literal("");

                return Text.literal(
                    String.format(
                        "%." + G9utils.opt().hudOptions.posDecimalPlaces + "f," +
                        "%." + G9utils.opt().hudOptions.posDecimalPlaces + "f," +
                        "%." + G9utils.opt().hudOptions.posDecimalPlaces + "f",
                        client.player.getVelocity().x,
                        client.player.getVelocity().y,
                        client.player.getVelocity().z
                    )
                );
            },
            Text.literal("[c]vel:"),
            () -> G9utils.opt().hudOptions.seeVel
        );
        hud.g9Utils$addValue(
            () -> {
                if(client.player == null) return Text.literal("");

                return Text.literal(
                    String.format(
                        "%." + G9utils.opt().hudOptions.posDecimalPlaces + "f,",
                        client.player.getVelocity().horizontalLength()
                    )
                );
            },
            Text.literal("[c]velh:"),
            () -> G9utils.opt().hudOptions.seeVel
        );
        hud.g9Utils$addValue(
            () -> {
                if(client.player == null) return Text.literal("");

                return Text.literal(
                    String.format(
                        "%." + G9utils.opt().hudOptions.posDecimalPlaces + "f,",
                         ((PlayerMixinDuck)client.player).g9Utils$getLastCurrentSpeed()
                    )
                );
            },
            Text.literal("[c]\"currentspeed\":"),
            () -> G9utils.opt().hudOptions.seeVel && G9utils.opt().cheats.quakeAir
        );

        hud.g9Utils$addValue(
            () -> {
                if(client.getServer() == null) return Text.literal("");

                if(client.player == null) return Text.literal("");

                ServerPlayerEntity p = client.getServer().getPlayerManager().getPlayer(client.player.getUuid());

                if(p == null) return Text.literal("");

                return Text.literal(
                    String.format(
                        "%." + G9utils.opt().hudOptions.posDecimalPlaces + "f," +
                        "%." + G9utils.opt().hudOptions.posDecimalPlaces + "f," +
                        "%." + G9utils.opt().hudOptions.posDecimalPlaces + "f",
                        p.getVelocity().x,
                        p.getVelocity().y,
                        p.getVelocity().z
                    )
                );
            },
            Text.literal("[s]vel:"),
            () -> G9utils.opt().hudOptions.seeVel && client.getServer() != null
        );

        hud.g9Utils$addValue(
            () -> {
                if(G9utils.lastSwordHitType == null) return Text.literal("");

                return G9utils.lastSwordHitType.text;
            },
            Text.literal("Last Hit:"),
            () -> G9utils.opt().hudOptions.seeSwordHitType
        );

        hud.g9Utils$addValue(
            () -> {
                if(G9utils.lastAttributeSwap != null) {
                    return G9utils.lastAttributeSwap.getText();
                }
                return Text.literal("No Swap Done");
            },
            Text.literal("Attribute Swap:"),
            () -> G9utils.opt().hudOptions.seeAttributeSwaps
        );


        hud.g9Utils$addValue(
            () -> {
                ClientCommonNetworkHandler networkHandler = client.getNetworkHandler();

                if(networkHandler == null) return Text.literal("");

                return Text.literal(
                    String.valueOf(
                        ((ClientCommonNetworkHandlerMixinDuck)
                            networkHandler)
                            .g9Utils$isServerSideSprinting()
                    )
                );
            },
            Text.literal("[c]sssprinting:"),
            () -> G9utils.opt().hudOptions.seeServerSideSprint
        );
    }

    private record Value(Supplier<Text> getter, Text name, Supplier<Boolean> shouldRender) {}
}
