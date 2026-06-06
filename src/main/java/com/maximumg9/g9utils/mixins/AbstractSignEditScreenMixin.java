package com.maximumg9.g9utils.mixins;

import com.maximumg9.g9utils.G9utils;
import com.maximumg9.g9utils.Util;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.block.entity.SignText;
import net.minecraft.client.gui.screen.ingame.AbstractSignEditScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(AbstractSignEditScreen.class)
public class AbstractSignEditScreenMixin {
    @Mutable
    @Shadow
    @Final
    private String[] messages;

    @Shadow
    private SignText text;

    @Inject(
        method="<init>(Lnet/minecraft/block/entity/SignBlockEntity;ZZLnet/minecraft/text/Text;)V",
        at=@At("TAIL")
    )
    public void wrap(SignBlockEntity blockEntity, boolean front, boolean filtered, Text title, CallbackInfo ci) {
        if(G9utils.opt().cheats.dontBeEvilAndWrong) {
            this.messages = Arrays.stream(this.text.getMessages(filtered))
                .map(Util::getSafeString)
                .toArray(String[]::new);
        }
    }
}
