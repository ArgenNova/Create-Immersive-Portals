package me.argennova.createportal.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import com.simibubi.create.content.trains.entity.Carriage.DimensionalCarriageEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import qouteall.imm_ptl.core.api.PortalAPI;

@Mixin(DimensionalCarriageEntity.class)
public abstract class MixinDimensionalCarriageEntity {
    @Redirect(
        method = "Lcom/simibubi/create/content/trains/entity/Carriage$DimensionalCarriageEntity;dismountPlayer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/server/level/ServerPlayer;Ljava/lang/Integer;Z)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;m_8999_(Lnet/minecraft/server/level/ServerLevel;DDDFF)V")
    )
    public void teleportToProxy(ServerPlayer sp, ServerLevel level, double x, double y, double z, float xRot, float yRot) {
        Vec3 tpPos = new Vec3(x, y, z);
        PortalAPI.teleportEntity(sp, level, tpPos);
    }
}
