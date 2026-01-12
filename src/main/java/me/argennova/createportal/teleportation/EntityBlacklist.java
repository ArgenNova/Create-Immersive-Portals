package me.argennova.createportal.teleportation;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import net.minecraft.world.entity.Entity;

public class EntityBlacklist {
    private static final Set<UUID> entityBlacklist = new HashSet<>();
    
    public static void setEntityTeleportable(UUID uuid, boolean teleportable) {
        if (teleportable)
            entityBlacklist.remove(uuid);
        else
            entityBlacklist.add(uuid);
    }

    public static boolean isTeleportBlocked(Entity entity) {
        return entityBlacklist.contains(entity.getUUID());
    }
}
