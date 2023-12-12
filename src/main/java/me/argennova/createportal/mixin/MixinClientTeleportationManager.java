package me.argennova.createportal.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.argennova.createportal.Globals;
import qouteall.imm_ptl.core.teleportation.ClientTeleportationManager;

@Mixin(ClientTeleportationManager.class)
public abstract class MixinClientTeleportationManager {
    @Inject(
        method = "Lqouteall/imm_ptl/core/teleportation/ClientTeleportationManager;changePlayerDimension(Lnet/minecraft/client/player/LocalPlayer;Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/world/phys/Vec3;)V",
        at = @At("RETURN")
    )
    private void onChangePlayerDimension(CallbackInfo ci) {
        Globals.PASSENGER_PACKET_DEFERRER.handlePacketsNow();
    }
}
