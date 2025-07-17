package com.maximumg9.g9utils.mixins;

import com.maximumg9.g9utils.ClientCommonNetworkHandlerMixinDuck;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientCommonNetworkHandler.class)
public class ClientCommonNetworkHandlerMixin implements ClientCommonNetworkHandlerMixinDuck {
    @Unique
    private boolean serverSideSprint = false;

    @Inject(method = "sendPacket",at=@At("HEAD"))
    public void captureSprintPackets(Packet<?> packet, CallbackInfo ci) {
        if(packet instanceof ClientCommandC2SPacket ccPacket) {
            if(ccPacket.getMode() == ClientCommandC2SPacket.Mode.START_SPRINTING) {
                serverSideSprint = true;
            }
            if(ccPacket.getMode() == ClientCommandC2SPacket.Mode.STOP_SPRINTING) {
                serverSideSprint = false;
            }
        }
    }

    @Override
    public boolean g9Utils$isServerSideSprinting() {
        return serverSideSprint;
    }

    @Override
    public void g9Utils$setServerSideSprinting(boolean sprinting) {
        this.serverSideSprint = sprinting;
    }
}
