package com.maximumg9.g9utils.mixins;

import com.maximumg9.g9utils.G9utils;
import com.maximumg9.g9utils.InGameHudDuck;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow @Final public InGameHud inGameHud;

    @Shadow @Nullable public ClientPlayerEntity player;

    @Inject(method="<init>",at=@At("TAIL"))
    public void initRenderer(RunArgs args, CallbackInfo ci) {
        ((InGameHudDuck) this.inGameHud)
            .g9Utils$addValue(
                () -> {
                    if(this.player == null) return Text.literal("");

                    float s = MathHelper.sin(this.player.getYaw() * 0.017453292F);

                    return Text.literal(String.valueOf(s));
                },
                Text.literal("sin(yaw):"),
                () -> G9utils.getOptions().seeCosAndSinForYaw
            );

        ((InGameHudDuck) this.inGameHud)
            .g9Utils$addValue(
                () -> {
                    if(this.player == null) return Text.literal("");

                    float c = MathHelper.cos(this.player.getYaw() * 0.017453292F);

                    return Text.literal(String.valueOf(c));
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

    }
}
