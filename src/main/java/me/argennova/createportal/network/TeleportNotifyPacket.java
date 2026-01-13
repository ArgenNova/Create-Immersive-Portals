package me.argennova.createportal.network;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import me.argennova.createportal.teleportation.TeleportControlUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent.Context;
import qouteall.imm_ptl.core.ClientWorldLoader;
import qouteall.imm_ptl.core.IPCGlobal;

import java.util.List;
import java.util.UUID;

public class TeleportNotifyPacket {
    private final UUID carriageUUID;
    private final ResourceKey<Level> level;
    private final double x, y, z;

    public TeleportNotifyPacket(UUID carriageUUID, ResourceKey<Level> level, double x, double y, double z) {
        this.carriageUUID = carriageUUID;
        this.level = level;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public TeleportNotifyPacket(FriendlyByteBuf buffer) {
        carriageUUID = buffer.readUUID();
        level = buffer.readResourceKey(Registry.DIMENSION_REGISTRY);
        x = buffer.readDouble();
        y = buffer.readDouble();
        z = buffer.readDouble();
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeUUID(carriageUUID);
        buffer.writeResourceKey(level);
        buffer.writeDouble(x);
        buffer.writeDouble(y);
        buffer.writeDouble(z);
    }

    public void handle(Context context) {
        context.enqueueWork(() -> {
            Vec3 pos = new Vec3(x, y, z);
            IPCGlobal.clientTeleportationManager.acceptSynchronizationDataFromServer(level, pos, true);

            // Find the carriage we are riding
            ClientLevel cLevel = ClientWorldLoader.getWorld(level);
            List<AbstractContraptionEntity> entities = cLevel.getEntitiesOfClass(AbstractContraptionEntity.class, AABB.ofSize(pos, 10, 10, 10), (entity) -> entity.getUUID().equals(carriageUUID));

            // Resend interaction so we can control train after going through portal
            TeleportControlUtil.replayInteraction(entities.get(0));
        });

        context.setPacketHandled(true);
    }
}
