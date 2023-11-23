package me.argennova.createportal.override;

import me.argennova.createportal.CreatePortalEntry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PortalOverrideRegister {
    public static final DeferredRegister<EntityType<?>> ENTITY_REGISTER = DeferredRegister.create(ForgeRegistries.Keys.ENTITY_TYPES, CreatePortalEntry.MODID);

    public static final RegistryObject<EntityType<PortalOverrideEntity>> PORTAL_OVERRIDE = ENTITY_REGISTER.register("portal_override", () -> 
        EntityType.Builder.of(PortalOverrideEntity::new, MobCategory.MISC)
        .sized(1f, 1f)
        .fireImmune()
        .clientTrackingRange(96)
        .build(CreatePortalEntry.MODID + "portal_override"));
    
    public static void register(IEventBus bus) {
        ENTITY_REGISTER.register(bus);
    }
}
