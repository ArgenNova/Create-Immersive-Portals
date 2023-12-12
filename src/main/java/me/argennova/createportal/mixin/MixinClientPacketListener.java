package me.argennova.createportal.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import me.argennova.createportal.Globals;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;

@Mixin(ClientPacketListener.class)
public abstract class MixinClientPacketListener implements ClientGamePacketListener {
    @Inject(
        method = "Lnet/minecraft/client/multiplayer/ClientPacketListener;m_6403_(Lnet/minecraft/network/protocol/game/ClientboundSetPassengersPacket;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/protocol/PacketUtils;m_131363_(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/util/thread/BlockableEventLoop;)V",
            shift = At.Shift.AFTER
        ),
        cancellable = true
    )
    private void onHandleSetEntityPassengersPacket(ClientboundSetPassengersPacket packet, CallbackInfo ci) {
        if (Globals.PASSENGER_PACKET_DEFERRER.deferPacket(packet, this)) {
            ci.cancel();
        }
    }
}
