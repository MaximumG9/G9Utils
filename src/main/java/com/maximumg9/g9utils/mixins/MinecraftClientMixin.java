package com.maximumg9.g9utils.mixins;

import com.maximumg9.g9utils.AttributeSwap;
import com.maximumg9.g9utils.G9utils;
import com.maximumg9.g9utils.InGameHudDuck;
import com.maximumg9.g9utils.renderers.G9HudLayer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow @Final public InGameHud inGameHud;

    @Shadow @Nullable public ClientPlayerEntity player;

    @Shadow
    @org.jspecify.annotations.Nullable
    public ClientPlayerInteractionManager interactionManager;

    @Redirect(method="doItemUse", at= @At(value = "INVOKE", target = "Lnet/minecraft/util/Hand;values()[Lnet/minecraft/util/Hand;"))
    private Hand[] rearrangeOrder() {
        if(this.player == null) throw new IllegalStateException("WTF");
        if((
                this.player.getStackInHand(Hand.MAIN_HAND).isIn(ItemTags.AXES) &&
                G9utils.opt().cheats.dontStripWithItemInOffhand
            ) ||
            G9utils.opt().cheats.prioritizeOffhand
        ) {
            return new Hand[] {Hand.OFF_HAND, Hand.MAIN_HAND};
        }
        return Hand.values();
    }

    @Inject(method = "doAttack",at= @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;swingHand(Lnet/minecraft/util/Hand;)V"))
    public void doAttack(CallbackInfoReturnable<Boolean> cir) {
        if(!G9utils.opt().hudOptions.seeAttributeSwaps) return;
        assert this.interactionManager != null;
        assert this.player != null;

        G9utils.timeSinceLastAttack = 0;
        if(
            G9utils.timeSinceLastSwap <= G9utils.opt().hudOptions.attributeSwapTimeout &&
                G9utils.timeSinceLastSwap > 0
        ) {
            G9utils.lastAttributeSwap = new AttributeSwap.FailedAttributeSwap(
                G9utils.possibleAttributeSwapStack,
                this.player.getInventory().getSelectedStack(),
                G9utils.timeSinceLastSwap
            );
            G9utils.timeSinceLastSwap = -1;
            G9utils.timeSinceLastAttack = -1;
        } else if(G9utils.timeSinceLastSwap == 0) {
            G9utils.lastAttributeSwap = new AttributeSwap.SuccessfullAttributeSwap(
                G9utils.possibleAttributeSwapStack,
                this.player.getInventory().getSelectedStack()
            );
            G9utils.timeSinceLastSwap = -1;
            G9utils.timeSinceLastAttack = -1;
        }
    }

    @Inject(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/PacketApplyBatcher;apply()V",
            shift = At.Shift.AFTER
        )
    )
    public void postApply(boolean tick, CallbackInfo ci) {
        if(!G9utils.queuedLivingTagsUpdate.isEmpty()) {
            G9utils.runningQueuedTagUpdate = true;
            for(LivingEntity entity : G9utils.queuedLivingTagsUpdate) {
                entity.onTrackedDataSet(LivingEntity.LIVING_FLAGS);
            }
            G9utils.runningQueuedTagUpdate = false;
            G9utils.queuedLivingTagsUpdate.clear();
        }
    }

    @Inject(method = "handleInputEvents",at=@At("HEAD"))
    public void handleInputs(CallbackInfo ci) {
        G9utils.handleInputs((MinecraftClient) (Object) this);
    }

    @Inject(method = "tick",at=@At("HEAD"))
    public void tick(CallbackInfo ci) {
        G9utils.tick();
    }

    @Inject(method="<init>",at=@At("TAIL"))
    public void initRenderer(RunArgs args, CallbackInfo ci) {
        G9utils.onMCInitEnd();
        G9HudLayer.initHUD((MinecraftClient) (Object) this,(InGameHudDuck) this.inGameHud);
    }
}
