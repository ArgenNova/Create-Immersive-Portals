package me.argennova.createportal.mixin;

import me.argennova.createportal.PacketRegister;
import me.argennova.createportal.network.TeleportNotifyPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import com.simibubi.create.content.trains.entity.Carriage.DimensionalCarriageEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import qouteall.imm_ptl.core.IPGlobal;

@Mixin(DimensionalCarriageEntity.class)
public abstract class MixinDimensionalCarriageEntity {
    @Redirect(
        method = "dismountPlayer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/server/level/ServerPlayer;Ljava/lang/Integer;Z)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;m_8999_(Lnet/minecraft/server/level/ServerLevel;DDDFF)V")
    )
    public void teleportToProxy(ServerPlayer sp, ServerLevel level, double x, double y, double z, float xRot, float yRot) {
        ResourceKey<Level> tpLevel = level.dimension();
        Vec3 tpPos = new Vec3(x, y, z);
        IPGlobal.serverTeleportationManager.teleportPlayer(sp, tpLevel, tpPos);
        PacketRegister.getChannel().send(PacketDistributor.PLAYER.with(() -> sp), new TeleportNotifyPacket(tpLevel, x, y, z));
    }
}
