package me.argennova.createportal.override;

import me.argennova.createportal.CreatePortalEntry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import qouteall.imm_ptl.core.IPGlobal;
import qouteall.imm_ptl.core.McHelper;
import qouteall.imm_ptl.core.api.PortalAPI;
import qouteall.imm_ptl.core.platform_specific.IPRegistry;
import qouteall.imm_ptl.core.portal.Portal;
import qouteall.imm_ptl.core.portal.PortalManipulation;
import qouteall.imm_ptl.core.portal.nether_portal.BlockPortalShape;
import qouteall.imm_ptl.core.portal.nether_portal.BreakablePortalEntity;

public class PortalOverrideEntity extends BreakablePortalEntity {
    private static EntityType<PortalOverrideEntity> entityType = PortalOverrideRegister.PORTAL_OVERRIDE.get();

    public PortalOverrideEntity(EntityType<?> entityType, Level world) {
        super(entityType, world);
    }

    public static Portal overridePortal(Portal portal) {
        if (portal instanceof PortalOverrideEntity)
            return portal;
        
        if (portal instanceof BreakablePortalEntity breakablePortal) {
            ServerLevel world = McHelper.getServerWorld(breakablePortal.dimensionTo);
            BreakablePortalEntity otherPortal = (BreakablePortalEntity) world.getEntity(breakablePortal.reversePortalId);

            if(otherPortal == null)
                return null;

            BlockPortalShape otherFrame = otherPortal.blockPortalShape;

            PortalOverrideEntity newOverride = copyPortal(breakablePortal);
            PortalOverrideEntity newOtherSide = createReversePortal(newOverride, otherFrame);
            PortalOverrideEntity newOtherFace = createFlippedPortal(newOverride);
            PortalOverrideEntity newOtherFaceSide = createReversePortal(newOtherFace, otherFrame);

            CreatePortalEntry.LOGGER.info("Overriding Portal");
        
            PortalManipulation.removeConnectedPortals(portal, p -> {});
            portal.remove(RemovalReason.KILLED);
        
            CreatePortalEntry.LOGGER.info("Still good.");

            McHelper.spawnServerEntity(newOverride);
            McHelper.spawnServerEntity(newOtherSide);
            McHelper.spawnServerEntity(newOtherFace);
            McHelper.spawnServerEntity(newOtherFaceSide);
            
            CreatePortalEntry.LOGGER.info("Portal Spawned: {}", newOverride);

            return newOverride;
        }

        return null;
    }

    private static PortalOverrideEntity copyPortal(BreakablePortalEntity portal) {
        PortalOverrideEntity newPortal = PortalAPI.copyPortal(portal, entityType);
        newPortal.blockPortalShape = portal.blockPortalShape;
        return newPortal;
    }

    private static PortalOverrideEntity createReversePortal(PortalOverrideEntity portal, BlockPortalShape otherFrame) {
        PortalOverrideEntity newPortal = PortalManipulation.createReversePortal(portal, entityType);

       portal.reversePortalId = newPortal.uuid;
       newPortal.reversePortalId = portal.uuid;

       newPortal.blockPortalShape = otherFrame;

       return newPortal;
    }

    private static PortalOverrideEntity createFlippedPortal(PortalOverrideEntity portal) {
        PortalOverrideEntity newPortal = PortalManipulation.createFlippedPortal(portal, entityType);

       newPortal.blockPortalShape = portal.blockPortalShape;

       return newPortal;
    }

    @Override
    public String toString() {
        String string = super.toString();
        //string = string + String.format(", Portal Shape: %s", blockPortalShape);

        return string;
    }

    @Override
    public boolean isAlive() {
        return false;
    }

    @Override
    protected boolean isPortalIntactOnThisSide() {
        boolean areaIntact = blockPortalShape.area.stream()
            .allMatch(blockPos ->
                level.getBlockState(blockPos).getBlock() == IPRegistry.NETHER_PORTAL_BLOCK.get()
            );
        boolean frameIntact = blockPortalShape.frameAreaWithoutCorner.stream()
            .allMatch(blockPos -> !level.isEmptyBlock(blockPos));
        return areaIntact && frameIntact;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected void addSoundAndParticle() {
        if (level.dimension() == Level.NETHER || dimensionTo == Level.NETHER)
            useNetherEffects();
    }

    @OnlyIn(Dist.CLIENT)
    private void useNetherEffects() {
        if (!IPGlobal.enableNetherPortalEffect) {
            return;
        }
        
        RandomSource random = level.getRandom();
        
        for (int i = 0; i < (int) Math.ceil(width * height / 20); i++) {
            if (random.nextInt(10) == 0) {
                double px = (random.nextDouble() * 2 - 1) * (width / 2);
                double py = (random.nextDouble() * 2 - 1) * (height / 2);
                
                Vec3 pos = getPointInPlane(px, py);
                
                double speedMultiplier = 20;
                
                double vx = speedMultiplier * ((double) random.nextFloat() - 0.5D) * 0.5D;
                double vy = speedMultiplier * ((double) random.nextFloat() - 0.5D) * 0.5D;
                double vz = speedMultiplier * ((double) random.nextFloat() - 0.5D) * 0.5D;
                
                level.addParticle(
                    ParticleTypes.PORTAL,
                    pos.x, pos.y, pos.z,
                    vx, vy, vz
                );
            }
        }
        
        if (random.nextInt(800) == 0) {
            level.playLocalSound(
                getX(),
                getY(),
                getZ(),
                SoundEvents.PORTAL_AMBIENT,
                SoundSource.BLOCKS,
                0.5F,
                random.nextFloat() * 0.4F + 0.8F,
                false
            );
        }
    }
}
