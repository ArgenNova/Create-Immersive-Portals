package me.argennova.createportal;

import me.argennova.createportal.network.TeleportNotifyPacket;
import me.argennova.createportal.network.EntityBlacklistUpdatePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketRegister {
    public static final ResourceLocation CHANNEL_NAME = new ResourceLocation(CreatePortalEntry.MODID, "main");
    public static final String NETWORK_VERSION = "3";

    private static SimpleChannel channel;

    public static void register() {
        channel = NetworkRegistry.ChannelBuilder.named(CHANNEL_NAME).clientAcceptedVersions(NETWORK_VERSION::equals).serverAcceptedVersions(NETWORK_VERSION::equals).networkProtocolVersion(() -> NETWORK_VERSION).simpleChannel();

        channel.messageBuilder(EntityBlacklistUpdatePacket.class, 0, NetworkDirection.PLAY_TO_CLIENT).encoder(EntityBlacklistUpdatePacket::write).decoder(EntityBlacklistUpdatePacket::new).consumerNetworkThread((packet, contextSupplier) -> {packet.handle(contextSupplier.get());}).add();
        channel.messageBuilder(TeleportNotifyPacket.class, 1, NetworkDirection.PLAY_TO_CLIENT).encoder(TeleportNotifyPacket::write).decoder(TeleportNotifyPacket::new).consumerNetworkThread((packet, contextSupplier) -> {packet.handle(contextSupplier.get());}).add();
    }

    public static SimpleChannel getChannel() {
        return channel;
    }
}
