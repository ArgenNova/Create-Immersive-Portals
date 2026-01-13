package me.argennova.createportal.teleportation;

import com.simibubi.create.AllPackets;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.sync.ContraptionInteractionPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;

public class TeleportControlUtil {
    public static AbstractContraptionEntity lastInteractionEntity = null;
    public static BlockPos lastInteractionPos = null;

    public static void replayInteraction(AbstractContraptionEntity entity) {
        if (lastInteractionEntity == null || !entity.getUUID().equals(lastInteractionEntity.getUUID()))
            return;

        entity.handlePlayerInteraction(Minecraft.getInstance().player, lastInteractionPos, Direction.NORTH, InteractionHand.MAIN_HAND);

        //Use Create's packet channel since that's what the InteractionPacket is registered to
        AllPackets.getChannel().sendToServer(new ContraptionInteractionPacket(entity, InteractionHand.MAIN_HAND, lastInteractionPos, Direction.NORTH));
    }
}
