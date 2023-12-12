package me.argennova.createportal.network;

import java.util.UUID;

import me.argennova.createportal.PacketRegister;
import me.argennova.createportal.teleportation.EntityBlacklist;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.NetworkEvent.Context;

public class EntityBlacklistUpdatePacket {
    private UUID uuid;
    private boolean remove;

    public EntityBlacklistUpdatePacket(UUID uuid, boolean remove) {
        this.uuid = uuid;
        this.remove = remove;
    }

    public EntityBlacklistUpdatePacket(FriendlyByteBuf buffer) {
        uuid = buffer.readUUID();
        remove = buffer.readBoolean();
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeUUID(uuid);
        buffer.writeBoolean(remove);
    }

    public void handle(Context context) {
        context.enqueueWork(() -> {
            EntityBlacklist.setEntityTeleportable(uuid, remove);
        });
        context.setPacketHandled(true);
    }

    public void send() {
        PacketRegister.getChannel().send(PacketDistributor.ALL.noArg(), this);
    }
}
