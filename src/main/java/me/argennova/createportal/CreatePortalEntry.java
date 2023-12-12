package me.argennova.createportal;

import me.argennova.createportal.compat.CreateImmPtlIntegration;
import me.argennova.createportal.network.DimensionChangeNotifyPacket;
import me.argennova.createportal.teleportation.EntityBlacklist;

import com.mojang.logging.LogUtils;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.PacketDistributor;

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

        MinecraftForge.EVENT_BUS.addListener(CreatePortalEntry::onDimensionChange);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        Config.blacklistedLevels.forEach((level) -> LOGGER.info("Blacklisted dimension: {}", level));
    }

    private static void onDimensionChange(PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player && EntityBlacklist.isTeleportBlocked(player))
            PacketRegister.getChannel().send(PacketDistributor.PLAYER.with(() -> player), new DimensionChangeNotifyPacket());
    }
}
