package me.argennova.createportal;

import me.argennova.createportal.override.PortalOverrideEntity;
import me.argennova.createportal.override.PortalOverrideRegister;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import qouteall.imm_ptl.core.render.PortalEntityRenderer;

public class CreatePortalClientEntry {
    @SubscribeEvent
    public static void initPortalRenderer(EntityRenderersEvent.RegisterRenderers event) {
        EntityType<PortalOverrideEntity> portal = PortalOverrideRegister.PORTAL_OVERRIDE.get();

        if (portal != null) {
            event.registerEntityRenderer(portal, (EntityRendererProvider) PortalEntityRenderer::new);
        }
    }

    public static void initialiseClient() {
        FMLJavaModLoadingContext.get().getModEventBus().register(CreatePortalClientEntry.class);
    }
}
