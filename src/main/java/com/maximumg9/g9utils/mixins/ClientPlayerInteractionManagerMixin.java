package com.maximumg9.g9utils.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import com.maximumg9.g9utils.G9utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.network.SequencedPacketCreator;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin {

    @Shadow private BlockPos currentBreakingPos;

    @Shadow protected abstract void sendSequencedPacket(ClientWorld world, SequencedPacketCreator packetCreator);

    @Shadow @Final private ClientPlayNetworkHandler networkHandler;

    @Shadow @Final private MinecraftClient client;

    @Redirect(method = "attackBlock",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;sendSequencedPacket(Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/client/network/SequencedPacketCreator;)V"))
    public void sendSequencedPacket(ClientPlayerInteractionManager instance, ClientWorld world, SequencedPacketCreator packetCreator, @Local(argsOnly = true) BlockPos pos, @Local(argsOnly = true) Direction dir) {
        if(pos.equals(this.currentBreakingPos) && G9utils.getOptions().instaMineSameBlock) {
            this.sendSequencedPacket(this.client.world, sequence ->
                new PlayerActionC2SPacket(
                    PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK,
                    pos,
                    dir,
                    sequence
                )
            );
        } else {
            this.sendSequencedPacket(world,packetCreator);
        }
    }
}
