package me.argennova.createportal.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.argennova.createportal.network.EntityBlacklistUpdatePacket;
import me.argennova.createportal.teleportation.EntityBlacklist;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

@Mixin(Player.class)
public abstract class MixinPlayer extends LivingEntity {
    private MixinPlayer() {
        super(null, null);
    }

    @Inject(
        method = "Lnet/minecraft/world/entity/player/Player;m_6083_()V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;m_8127_()V")
    )
    private void onRemoveVehicle(CallbackInfo ci) {
        EntityBlacklist.setEntityTeleportable(uuid, true);
        new EntityBlacklistUpdatePacket(uuid, true).send();
    }
}
