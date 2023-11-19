package com.github.argennova.createportal.compat;

import java.util.List;

import com.github.argennova.createportal.Config;
import com.simibubi.create.content.trains.track.AllPortalTracks;
import com.simibubi.create.foundation.utility.BlockFace;
import com.simibubi.create.foundation.utility.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import qouteall.imm_ptl.core.McHelper;
import qouteall.imm_ptl.core.portal.Portal;

public class ImmPtlPatch {
    public static void patch() {
        AllPortalTracks.registerIntegration(new ResourceLocation("imm_ptl_core", "nether_portal_block"), ImmPtlPatch::immPtlNether);
    }

    private static Pair<ServerLevel, BlockFace> immPtlNether(Pair<ServerLevel, BlockFace> inbound) {
        return ImmPtlPatch.immPtlPortalProvider(inbound);
    }

    public static Pair<ServerLevel, BlockFace> immPtlPortalProvider(Pair<ServerLevel, BlockFace> inbound) {
		ServerLevel level = inbound.getFirst();
		MinecraftServer server = level.getServer();
		BlockFace inboundTrack = inbound.getSecond();
		BlockPos portalPos = inboundTrack.getConnectedPos();

        if (Config.blacklistedLevels.contains(level.dimension().location()))
            return null;

        List<Portal> portals = McHelper.findEntitiesRough(Portal.class, level, new Vec3(portalPos.getX(), portalPos.getY(), portalPos.getZ()), 3, portal -> {
            ResourceKey<Level> destLevel = portal.getDestDim();

            if (Config.blacklistedLevels.contains(destLevel.location()))
                return false;

            Vec3 normal = portal.getNormal();
            if (!Direction.getNearest(normal.x, normal.y, normal.z).equals(inboundTrack.getOppositeFace()))
                return false;

            BlockPos pos = new BlockPos(portal.position());
            int dist, maxDist = (int) (portal.height / 2d);

            switch (inboundTrack.getOppositeFace()) {
                case NORTH:
                case SOUTH:
                    dist = Mth.abs(pos.getX() - portalPos.getX());

                    if (dist > maxDist)
                        return false;
                    
                    break;
                case EAST:
                case WEST:
                    dist = Mth.abs(pos.getZ() - portalPos.getZ());

                    if (dist <= maxDist)
                        break;
                default:
                    return false;
            }

            return true;
        });

        if (portals.isEmpty())
            return null;

        Portal portal = portals.get(0);
        ServerLevel otherLevel = server.getLevel(portal.getDestDim());
        BlockPos portalCenter = new BlockPos(portal.position());
        Vec3i offset = new Vec3i(portalPos.getX() - portalCenter.getX(), portalPos.getY() - portalCenter.getY(), portalPos.getZ() - portalCenter.getZ());
        BlockPos otherCenter = new BlockPos(portal.getDestPos());
        BlockPos otherPos = otherCenter.offset(offset);

        return Pair.of(otherLevel, new BlockFace(otherPos.relative(inboundTrack.getFace()), inboundTrack.getOppositeFace()));
	}
}
