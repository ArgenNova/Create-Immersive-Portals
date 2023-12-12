package me.argennova.createportal.network;

import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;

import com.mojang.datafixers.util.Pair;

import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;

public class PacketDeferrer<T extends Packet<V>, V extends PacketListener> {
    private Queue<Pair<T, V>> waitList = new LinkedTransferQueue<>();
    private boolean shouldDeferPackets = false;

    public boolean deferPacket(T packet, V listener) {
        if (shouldDeferPackets)
            waitList.add(new Pair<T, V>(packet, listener));
        return shouldDeferPackets;
    }

    public void startDeferring() {
        shouldDeferPackets = true;
    }

    public void handlePacketsNow() {
        shouldDeferPackets = false;

        while (!waitList.isEmpty()) {
            Pair<T, V> pair = waitList.remove();
            pair.getFirst().handle(pair.getSecond());
        }
    }
}
