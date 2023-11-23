package me.argennova.createportal;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = CreatePortalEntry.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    // a list of strings that are treated as resource locations for levels
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> LEVEL_STRINGS = BUILDER
            .comment("Dimensions that Create trains are not allowed to travel to/from. The End is blacklisted automatically.")
            .defineListAllowEmpty(Collections.singletonList("level"), () -> List.of("minecraft:the_end"), Config::validateLevelName);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static Set<ResourceLocation> blacklistedLevels;

    private static boolean validateLevelName(final Object obj)
    {
        return obj instanceof final String levelName;
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        // Convert strings to ResourceLocations
        blacklistedLevels = LEVEL_STRINGS.get().stream()
                .map(levelName -> {
                    return new ResourceLocation(levelName);
                }).collect(Collectors.toSet());
    }
}
