package me.argennova.createportal.network;

import me.argennova.createportal.Globals;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent.Context;

public class DimensionChangeNotifyPacket {
    private boolean stub; // Only here so packet isn't discarded

    public DimensionChangeNotifyPacket() {
        stub = true;
    }

    public DimensionChangeNotifyPacket(FriendlyByteBuf buffer) {
        stub = buffer.readBoolean();
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeBoolean(stub);
    }

    public void handle(Context context) {
        context.enqueueWork(() -> Globals.PASSENGER_PACKET_DEFERRER.startDeferring());
        context.setPacketHandled(true);
    }
}
