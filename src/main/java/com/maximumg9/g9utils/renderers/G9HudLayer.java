package com.maximumg9.g9utils.renderers;

import com.maximumg9.g9utils.ClientCommonNetworkHandlerMixinDuck;
import com.maximumg9.g9utils.G9utils;
import com.maximumg9.g9utils.InGameHudDuck;
import com.maximumg9.g9utils.PlayerMixinDuck;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
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

    public void render(DrawContext context, RenderTickCounter tickCounter) {
        int y = 0;

        for(Value value : values) {
            if(!value.shouldRender.get()) continue;
            Text text = value.name.copy().append(value.getter().get());

            int x = context.getScaledWindowWidth() - textRenderer.getWidth(text);
            context.drawText(textRenderer, text, x, y, Colors.WHITE, true);
            y += textRenderer.fontHeight;
        }

    }
    
    @SuppressWarnings("MalformedFormatString")
    public static void initHUD(MinecraftClient client, InGameHudDuck hud) {
        hud.g9Utils$addValue(
                () -> {
                    if(client.player == null) return Text.literal("");

                    float s = MathHelper.sin(client.player.getYaw() * 0.017453292F);

                    return Text.literal(
                        String.format(
                            "%." + G9utils.opt().hudOptions.opt().yawDecimalPlaces + "f",
                            s
                        )
                    );
                },
                Text.literal("sin(yaw):"),
                () -> G9utils.opt().hudOptions.opt().seeCosAndSinForYaw
            );

        hud.g9Utils$addValue(
                () -> {
                    if(client.player == null) return Text.literal("");

                    float c = MathHelper.cos(client.player.getYaw() * 0.017453292F);

                    return Text.literal(
                        String.format(
                            "%." + G9utils.opt().hudOptions.opt().yawDecimalPlaces + "f",
                            c
                        )
                    );
                },
                Text.literal("cos(yaw):"),
                () -> G9utils.opt().hudOptions.opt().seeCosAndSinForYaw
            );

        hud.g9Utils$addValue(
                () -> {
                    if(client.player == null) return Text.literal("");

                    float radYaw = client.player.getYaw() * 0.017453292F;

                    double degRadYaw = ((double)radYaw) * 180.0/Math.PI;

                    return Text.literal(
                        String.format(
                            "%." + G9utils.opt().hudOptions.opt().yawDecimalPlaces + "f",
                            degRadYaw
                        )
                    );
                },
                Text.literal("radian rounded yaw:"),
                () -> G9utils.opt().hudOptions.opt().seeRadianRoundedYaw
            );

        hud.g9Utils$addValue(
                () -> {
                    if(client.player == null) return Text.literal("");

                    return Text.literal(
                        String.format(
                            "%." + G9utils.opt().hudOptions.opt().yawDecimalPlaces + "f",
                            client.player.getYaw()
                        )
                    );
                },
                Text.literal("yaw:"),
                () -> G9utils.opt().hudOptions.opt().seeAccurateYaw
            );

        hud.g9Utils$addValue(
                () -> {
                    if(client.player == null) return Text.literal("");

                    return Text.literal(String.valueOf(client.player.isOnGround()));
                },
                Text.literal("[c]grounded:"),
                () -> G9utils.opt().hudOptions.opt().seeOnGround
            );
        hud.g9Utils$addValue(
            () -> {
                if(client.player == null) return Text.literal("");

                return Text.literal(String.valueOf(((PlayerMixinDuck)client.player).g9Utils$wasAirborneLastFrame()));
            },
            Text.literal("[c]wasOnGround:"),
            () -> G9utils.opt().hudOptions.opt().seeOnGround && G9utils.opt().cheats.opt().quakeAir
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
                () -> G9utils.opt().hudOptions.opt().seeOnGround && client.getServer() != null
            );

        hud.g9Utils$addValue(
                () -> {
                    if(client.player == null) return Text.literal("");

                    return Text.literal(
                        String.format(
                            "%." + G9utils.opt().hudOptions.opt().posDecimalPlaces + "f," +
                            "%." + G9utils.opt().hudOptions.opt().posDecimalPlaces + "f," +
                            "%." + G9utils.opt().hudOptions.opt().posDecimalPlaces + "f",
                            client.player.getX(),
                            client.player.getY(),
                            client.player.getZ()
                        )
                    );
                },
                Text.literal("[c]pos:"),
                () -> G9utils.opt().hudOptions.opt().seePos
            );

        hud.g9Utils$addValue(
                () -> {
                    if(client.getServer() == null) return Text.literal("");

                    if(client.player == null) return Text.literal("");

                    ServerPlayerEntity p = client.getServer().getPlayerManager().getPlayer(client.player.getUuid());

                    if(p == null) return Text.literal("");

                    return Text.literal(
                        String.format(
                            "%." + G9utils.opt().hudOptions.opt().posDecimalPlaces + "f," +
                            "%." + G9utils.opt().hudOptions.opt().posDecimalPlaces + "f," +
                            "%." + G9utils.opt().hudOptions.opt().posDecimalPlaces + "f",
                            p.getX(),
                            p.getY(),
                            p.getZ()
                        )
                    );
                },
                Text.literal("[s]pos:"),
                () -> G9utils.opt().hudOptions.opt().seePos && client.getServer() != null
            );

        hud.g9Utils$addValue(
                () -> {
                    if(client.player == null) return Text.literal("");

                    return Text.literal(
                        String.format(
                            "%." + G9utils.opt().hudOptions.opt().posDecimalPlaces + "f," +
                            "%." + G9utils.opt().hudOptions.opt().posDecimalPlaces + "f," +
                            "%." + G9utils.opt().hudOptions.opt().posDecimalPlaces + "f",
                            client.player.getVelocity().x,
                            client.player.getVelocity().y,
                            client.player.getVelocity().z
                        )
                    );
                },
                Text.literal("[c]vel:"),
                () -> G9utils.opt().hudOptions.opt().seeVel
            );
        hud.g9Utils$addValue(
            () -> {
                if(client.player == null) return Text.literal("");

                return Text.literal(
                    String.format(
                        "%." + G9utils.opt().hudOptions.opt().posDecimalPlaces + "f,",
                        client.player.getVelocity().horizontalLength()
                    )
                );
            },
            Text.literal("[c]velh:"),
            () -> G9utils.opt().hudOptions.opt().seeVel
        );
        hud.g9Utils$addValue(
            () -> {
                if(client.player == null) return Text.literal("");

                return Text.literal(
                    String.format(
                        "%." + G9utils.opt().hudOptions.opt().posDecimalPlaces + "f,",
                         ((PlayerMixinDuck)client.player).g9Utils$getLastCurrentSpeed()
                    )
                );
            },
            Text.literal("[c]\"currentspeed\":"),
            () -> G9utils.opt().hudOptions.opt().seeVel && G9utils.opt().cheats.opt().quakeAir
        );

        hud.g9Utils$addValue(
                () -> {
                    if(client.getServer() == null) return Text.literal("");

                    if(client.player == null) return Text.literal("");

                    ServerPlayerEntity p = client.getServer().getPlayerManager().getPlayer(client.player.getUuid());

                    if(p == null) return Text.literal("");

                    return Text.literal(
                        String.format(
                            "%." + G9utils.opt().hudOptions.opt().posDecimalPlaces + "f," +
                            "%." + G9utils.opt().hudOptions.opt().posDecimalPlaces + "f," +
                            "%." + G9utils.opt().hudOptions.opt().posDecimalPlaces + "f",
                            p.getVelocity().x,
                            p.getVelocity().y,
                            p.getVelocity().z
                        )
                    );
                },
                Text.literal("[s]vel:"),
                () -> G9utils.opt().hudOptions.opt().seeVel && client.getServer() != null
            );

        hud.g9Utils$addValue(
                () -> {
                    if(G9utils.lastSwordHitType == null) return Text.literal("");

                    return G9utils.lastSwordHitType.text;
                },
                Text.literal("Last Hit:"),
                () -> G9utils.opt().hudOptions.opt().seeSwordHitType
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
                () -> G9utils.opt().hudOptions.opt().seeServerSideSprint
            );
    }

    private record Value(Supplier<Text> getter, Text name, Supplier<Boolean> shouldRender) {}
}
