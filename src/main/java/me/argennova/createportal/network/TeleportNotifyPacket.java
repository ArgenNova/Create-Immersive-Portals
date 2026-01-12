package me.argennova.createportal.network;

import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent.Context;
import qouteall.imm_ptl.core.IPCGlobal;

public class TeleportNotifyPacket {
    private final ResourceKey<Level> level;
    private final double x, y, z;

    public TeleportNotifyPacket(ResourceKey<Level> level, double x, double y, double z) {
        this.level = level;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public TeleportNotifyPacket(FriendlyByteBuf buffer) {
        level = buffer.readResourceKey(Registry.DIMENSION_REGISTRY);
        x = buffer.readDouble();
        y = buffer.readDouble();
        z = buffer.readDouble();
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeResourceKey(level);
        buffer.writeDouble(x);
        buffer.writeDouble(y);
        buffer.writeDouble(z);
    }

    public void handle(Context context) {
        context.enqueueWork(() -> IPCGlobal.clientTeleportationManager.acceptSynchronizationDataFromServer(level, new Vec3(x, y, z), true));
        context.setPacketHandled(true);
    }
}
