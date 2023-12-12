package me.argennova.createportal;

import me.argennova.createportal.network.PacketDeferrer;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;

public class Globals {
    public static PacketDeferrer<ClientboundSetPassengersPacket, ClientGamePacketListener> PASSENGER_PACKET_DEFERRER = new PacketDeferrer<>();
}
