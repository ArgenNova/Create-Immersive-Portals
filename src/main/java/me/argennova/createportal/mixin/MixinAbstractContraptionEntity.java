package me.argennova.createportal.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;

import me.argennova.createportal.network.EntityBlacklistUpdatePacket;
import me.argennova.createportal.teleportation.EntityBlacklist;
import net.minecraft.world.entity.Entity;

@Mixin(AbstractContraptionEntity.class)
public abstract class MixinAbstractContraptionEntity {
    @Redirect(
        method = "Lcom/simibubi/create/content/contraptions/AbstractContraptionEntity;handlePlayerInteraction(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Lnet/minecraft/world/InteractionHand;)Z",
        at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/contraptions/AbstractContraptionEntity;addSittingPassenger(Lnet/minecraft/world/entity/Entity;I)V")
    )
    private void addSittingPassengerRedirect(AbstractContraptionEntity ace, Entity entity, int seat) {
        EntityBlacklist.setEntityTeleportable(entity.getUUID(), false);
        new EntityBlacklistUpdatePacket(entity.getUUID(), false).send();

        ace.addSittingPassenger(entity, seat);
    }
}
