package com.maximumg9.g9utils.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import com.maximumg9.g9utils.G9utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.client.network.ClientConnectionState;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin extends ClientCommonNetworkHandler {

    protected ClientPlayNetworkHandlerMixin(MinecraftClient client, ClientConnection connection, ClientConnectionState connectionState) {
        super(client, connection, connectionState);
    }

    @Inject(method="onPlayerPositionLook",at=@At("TAIL"))
    public void onLagBack(PlayerPositionLookS2CPacket packet, CallbackInfo ci, @Local PlayerEntity player) {
        if(G9utils.opt().seeLagBack) {
            boolean xRelat = packet.getFlags().contains(PositionFlag.X);
            boolean yRelat = packet.getFlags().contains(PositionFlag.Y);
            boolean zRelat = packet.getFlags().contains(PositionFlag.Z);

            boolean yawRelat = packet.getFlags().contains(PositionFlag.X_ROT);
            boolean pitchRelat = packet.getFlags().contains(PositionFlag.Y_ROT);

            MutableText text = Text.literal("[LB] ").styled(style -> style.withColor(Formatting.YELLOW));

            double x;
            double y;
            double z;

            if(xRelat) { x = player.getX() + packet.getX();}
            else { x = packet.getX(); }
            if(yRelat) { y = player.getY() + packet.getY();}
            else { y = packet.getY(); }
            if(zRelat) { z = player.getZ() + packet.getZ();}
            else { z = packet.getZ(); }

            float yaw;
            float pitch;

            if(yawRelat) { yaw = player.getYaw() + packet.getYaw();}
            else { yaw = packet.getYaw(); }
            if(pitchRelat) { pitch = player.getPitch() + packet.getPitch();}
            else { pitch = packet.getPitch(); }

            text.append(String.valueOf(x))
                .append(",")
                .append(String.valueOf(y))
                .append(",")
                .append(String.valueOf(z))
                .append(":")
                .append(String.valueOf(pitch))
                .append(",")
                .append(String.valueOf(yaw));

            this.client.inGameHud.getChatHud().addMessage(text);
        }
    }
}
