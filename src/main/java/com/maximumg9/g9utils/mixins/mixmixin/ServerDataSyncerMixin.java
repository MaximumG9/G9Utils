package com.maximumg9.g9utils.mixins.mixmixin;

import com.maximumg9.g9utils.G9utils;
import fi.dy.masa.tweakeroo.data.DataManager;
import fi.dy.masa.tweakeroo.data.EntityDataManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityDataManager.class)
public class ServerDataSyncerMixin {
    @Redirect(
        method = "onWorldPre",
        at = @At(
            value = "INVOKE",
            target = "Lfi/dy/masa/tweakeroo/data/DataManager;hasIntegratedServer()Z"
        ),
        remap = false
    )
    public boolean preventServuxLeaking(DataManager instance) {
        return instance.hasIntegratedServer() || G9utils.opt().stopTweakerooLeak;
    }
}
