package com.github.argennova.createportal;

import com.github.argennova.createportal.compat.ImmPtlPatch;
import com.mojang.logging.LogUtils;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CreatePortalEntry.MODID)
public class CreatePortalEntry
{

    public static final String MODID = "createportal";

    public static final Logger LOGGER = LogUtils.getLogger();

    public CreatePortalEntry()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        //Register config setup
        modEventBus.addListener(this::commonSetup);

        // Register imm_ptl portal provider
        ImmPtlPatch.patch();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        Config.blacklistedLevels.forEach((level) -> LOGGER.info("Blacklisted dimension: {}", level));
    }
}
