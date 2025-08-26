package com.maximumg9.g9utils.mixins;

import com.maximumg9.g9utils.G9utils;
import com.maximumg9.g9utils.InGameHudDuck;
import com.maximumg9.g9utils.ClientCommonNetworkHandlerMixinDuck;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow @Final public InGameHud inGameHud;

    @Shadow @Nullable public ClientPlayerEntity player;

    @Shadow @Nullable private IntegratedServer server;

    @Shadow @Nullable public abstract ClientPlayNetworkHandler getNetworkHandler();

    @Redirect(method="doItemUse", at= @At(value = "INVOKE", target = "Lnet/minecraft/util/Hand;values()[Lnet/minecraft/util/Hand;"))
    private Hand[] rearrangeOrder() {
        if(this.player == null) throw new IllegalStateException("WTF");
        if((
                this.player.getStackInHand(Hand.MAIN_HAND).isIn(ItemTags.AXES) &&
                G9utils.getOptions().dontStripWithItemInOffhand
            ) ||
            G9utils.getOptions().prioritizeOffhand
        ) {
            return new Hand[] {Hand.OFF_HAND, Hand.MAIN_HAND};
        }
        return Hand.values();
    }

    @Inject(method="<init>",at=@At("TAIL"))
    public void initRenderer(RunArgs args, CallbackInfo ci) {
        ((InGameHudDuck) this.inGameHud)
            .g9Utils$addValue(
                () -> {
                    if(this.player == null) return Text.literal("");

                    float s = MathHelper.sin(this.player.getYaw() * 0.017453292F);

                    return Text.literal(String.format("%." + G9utils.getOptions().yawDecimalPlaces + "f",s));
                },
                Text.literal("sin(yaw):"),
                () -> G9utils.getOptions().seeCosAndSinForYaw
            );

        ((InGameHudDuck) this.inGameHud)
            .g9Utils$addValue(
                () -> {
                    if(this.player == null) return Text.literal("");

                    float c = MathHelper.cos(this.player.getYaw() * 0.017453292F);

                    return Text.literal(String.format("%." + G9utils.getOptions().yawDecimalPlaces + "f",c));
                },
                Text.literal("cos(yaw):"),
                () -> G9utils.getOptions().seeCosAndSinForYaw
            );

        ((InGameHudDuck) this.inGameHud)
            .g9Utils$addValue(
                () -> {
                    if(this.player == null) return Text.literal("");

                    float radYaw = this.player.getYaw() * 0.017453292F;

                    double degRadYaw = ((double)radYaw) * 180.0/Math.PI;

                    return Text.literal(String.format("%." + G9utils.getOptions().yawDecimalPlaces + "f",degRadYaw));
                },
                Text.literal("radian rounded yaw:"),
                () -> G9utils.getOptions().seeRadianRoundedYaw
            );

        ((InGameHudDuck) this.inGameHud)
            .g9Utils$addValue(
                () -> {
                    if(this.player == null) return Text.literal("");

                    return Text.literal(String.format("%." + G9utils.getOptions().yawDecimalPlaces + "f",this.player.getYaw()));
                },
                Text.literal("yaw:"),
                () -> G9utils.getOptions().seeAccurateYaw
            );

        ((InGameHudDuck) this.inGameHud)
            .g9Utils$addValue(
                () -> {
                    if(this.player == null) return Text.literal("");

                    return Text.literal(String.valueOf(this.player.isOnGround()));
                },
                Text.literal("[c]grounded:"),
                () -> G9utils.getOptions().seeOnGround
            );

        ((InGameHudDuck) this.inGameHud)
            .g9Utils$addValue(
                () -> {
                    if(this.server == null) return Text.literal("");

                    if(this.player == null) return Text.literal("");

                    ServerPlayerEntity p = this.server.getPlayerManager().getPlayer(this.player.getUuid());

                    if(p == null) return Text.literal("");

                    return Text.literal(String.valueOf(p.isOnGround()));
                },
                Text.literal("[s]grounded:"),
                () -> G9utils.getOptions().seeOnGround && this.server != null
            );

        ((InGameHudDuck) this.inGameHud)
            .g9Utils$addValue(
                () -> {
                    if(this.player == null) return Text.literal("");

                    return Text.literal(
                        String.format(
                            "%." + G9utils.getOptions().posDecimalPlaces + "f," +
                                "%." + G9utils.getOptions().posDecimalPlaces + "f," +
                                "%." + G9utils.getOptions().posDecimalPlaces + "f",
                            this.player.getX(),
                            this.player.getY(),
                            this.player.getZ()
                        )
                    );
                },
                Text.literal("[c]pos:"),
                () -> G9utils.getOptions().seePos
            );

        ((InGameHudDuck) this.inGameHud)
            .g9Utils$addValue(
                () -> {
                    if(this.server == null) return Text.literal("");

                    if(this.player == null) return Text.literal("");

                    ServerPlayerEntity p = this.server.getPlayerManager().getPlayer(this.player.getUuid());

                    if(p == null) return Text.literal("");

                    return Text.literal(
                        String.format(
                            "%." + G9utils.getOptions().posDecimalPlaces + "f," +
                            "%." + G9utils.getOptions().posDecimalPlaces + "f," +
                            "%." + G9utils.getOptions().posDecimalPlaces + "f",
                            p.getX(),
                            p.getY(),
                            p.getZ()
                        )
                    );
                },
                Text.literal("[s]pos:"),
                () -> G9utils.getOptions().seePos && this.server != null
            );

        ((InGameHudDuck) this.inGameHud)
            .g9Utils$addValue(
                () -> {
                    if(this.player == null) return Text.literal("");

                    return Text.literal(
                        String.format(
                            "%." + G9utils.getOptions().posDecimalPlaces + "f," +
                                "%." + G9utils.getOptions().posDecimalPlaces + "f," +
                                "%." + G9utils.getOptions().posDecimalPlaces + "f",
                            this.player.getVelocity().x,
                            this.player.getVelocity().y,
                            this.player.getVelocity().z
                        )
                    );
                },
                Text.literal("[c]vel:"),
                () -> G9utils.getOptions().seeVel
            );

        ((InGameHudDuck) this.inGameHud)
            .g9Utils$addValue(
                () -> {
                    if(this.server == null) return Text.literal("");

                    if(this.player == null) return Text.literal("");

                    ServerPlayerEntity p = this.server.getPlayerManager().getPlayer(this.player.getUuid());

                    if(p == null) return Text.literal("");

                    return Text.literal(
                        String.format(
                            "%." + G9utils.getOptions().posDecimalPlaces + "f," +
                                "%." + G9utils.getOptions().posDecimalPlaces + "f," +
                                "%." + G9utils.getOptions().posDecimalPlaces + "f",
                            p.getVelocity().x,
                            p.getVelocity().y,
                            p.getVelocity().z
                        )
                    );
                },
                Text.literal("[s]vel:"),
                () -> G9utils.getOptions().seeVel && this.server != null
            );

        ((InGameHudDuck) this.inGameHud)
            .g9Utils$addValue(
                () -> {
                    if(G9utils.lastSwordHitType == null) return Text.literal("");

                    return G9utils.lastSwordHitType.text;
                },
                Text.literal("Last Hit:"),
                () -> G9utils.getOptions().seeSwordHitType
            );

        ((InGameHudDuck) this.inGameHud)
            .g9Utils$addValue(
                () -> {
                    ClientCommonNetworkHandler networkHandler = this.getNetworkHandler();

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
                () -> G9utils.getOptions().seeServerSideSprint
            );
    }
}
