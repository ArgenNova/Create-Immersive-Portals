package me.argennova.createportal.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.argennova.createportal.teleportation.EntityBlacklist;
import net.minecraft.world.entity.Entity;
import qouteall.imm_ptl.core.portal.Portal;

@Mixin(Portal.class)
public abstract class MixinPortal {
    @Inject(
        method = "Lqouteall/imm_ptl/core/portal/Portal;canTeleportEntity(Lnet/minecraft/world/entity/Entity;)Z",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onCanTeleportEntity(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (EntityBlacklist.isTeleportBlocked(entity))
            cir.setReturnValue(false);
    }
}