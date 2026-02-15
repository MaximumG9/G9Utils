package com.maximumg9.g9utils.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import com.maximumg9.g9utils.G9utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.client.network.ClientConnectionState;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.EntityPosition;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
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
            EntityPosition entityPosition = EntityPosition.fromEntity(player);
            EntityPosition entityPosition2 = EntityPosition.apply(entityPosition, packet.change(), packet.relatives());

            MutableText text = Text.literal("[LB] ").styled(style -> style.withColor(Formatting.YELLOW));

            text.append(entityPosition2.position().toString())
                .append(":")
                .append(String.valueOf(entityPosition2.pitch()))
                .append(",")
                .append(String.valueOf(entityPosition2.yaw()));

            this.client.inGameHud.getChatHud().addMessage(text);
        }
    }
}
