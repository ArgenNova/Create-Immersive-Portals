package me.argennova.createportal;

import me.argennova.createportal.compat.CreateImmPtlIntegration;

import com.mojang.logging.LogUtils;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

import org.slf4j.Logger;

@Mod(CreatePortalEntry.MODID)
public class CreatePortalEntry
{
    public static final String MODID = "createportal";

    public static final Logger LOGGER = LogUtils.getLogger();

    public CreatePortalEntry()
    {
        if (FMLEnvironment.dist == Dist.CLIENT)
            CreatePortalClientEntry.initialiseClient();

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        modEventBus.addListener(this::commonSetup);

        PortalRegister.register(modEventBus);
        PacketRegister.register();

        CreateImmPtlIntegration.register();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        Config.blacklistedLevels.forEach((level) -> LOGGER.info("Blacklisted dimension: {}", level));
    }
}
