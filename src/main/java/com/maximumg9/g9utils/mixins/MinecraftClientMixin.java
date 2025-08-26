package com.maximumg9.g9utils.mixins;

import com.maximumg9.g9utils.G9HudLayer;
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
                G9utils.opt().dontStripWithItemInOffhand
            ) ||
            G9utils.opt().prioritizeOffhand
        ) {
            return new Hand[] {Hand.OFF_HAND, Hand.MAIN_HAND};
        }
        return Hand.values();
    }

    @Inject(method="<init>",at=@At("TAIL"))
    public void initRenderer(RunArgs args, CallbackInfo ci) {
        G9HudLayer.initHUD((MinecraftClient) (Object) this,(InGameHudDuck) this.inGameHud);
    }
}
