package org.progamer.preconfigured_worldgen_options;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.stream.Collectors;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = Preconfigured_worldgen_options.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.ConfigValue<Boolean> USE_CONFIG_PRESET = BUILDER
            .comment("Use config preset by default instead of manual selection")
            .define("useConfigPreset", true);

    public static final ForgeConfigSpec.BooleanValue ENABLE_BUTTON = BUILDER
            .comment("Makes 'Use Config Preset' button in world creation screen active/disabled. If false, field 'useConfigPreset' is always false!")
            .define("IsUsePresetButtonActive", true);

    public static final ForgeConfigSpec.ConfigValue<String> BUTTON_HOVER_TOOLTIP = BUILDER
            .comment("if you wish, you can change hover button tooltip")
            .define("buttonHoverTooltip", "If ON, world generation parameters will be set automatically" +
                    " according to the configuration settings. So if this mod is here, it means the developer has set" +
                    " the most recommended generation settings. You can enable it and check setting yourself. It only" +
                    " overrides currently set settings in GUI.");

    public static final ForgeConfigSpec.ConfigValue<String> BIOME_STRING = BUILDER
            .comment("Target biome. Works only if minecraft:single_biome_surface present in worldGenType")
            .define("biome", "minecraft:snowy_taiga");

    public static final ForgeConfigSpec.ConfigValue<String> WORLD_GEN_TYPE = BUILDER
            .comment("minecraft:normal/minecraft:single_biome_surface/minecraft:amplified/minecraft:flat/minecraft:large_biomes")
            .define("worldGenType", "minecraft:single_biome_surface", Config::validateWorldGenType);


    public static final ForgeConfigSpec.BooleanValue ENABLE_STRUCTURES_SPAWN = BUILDER
            .comment("Enable vanilla structures spawn")
            .define("enableStructuresSpawn", true);

    public static final ForgeConfigSpec.BooleanValue ENABLE_BONUS_CHEST = BUILDER
            .comment("Enable vanilla bonus chest spawn")
            .define("enableBonusChest", false);

//    private static final ForgeConfigSpec.BooleanValue LOG_DIRT_BLOCK = BUILDER
//            .comment("Whether to log the dirt block on common setup")
//            .define("logDirtBlock", true);
//
//    private static final ForgeConfigSpec.IntValue MAGIC_NUMBER = BUILDER
//            .comment("A magic number")
//            .defineInRange("magicNumber", 42, 0, Integer.MAX_VALUE);
//
//    public static final ForgeConfigSpec.ConfigValue<String> MAGIC_NUMBER_INTRODUCTION = BUILDER
//            .comment("What you want the introduction message to be for the magic number")
//            .define("magicNumberIntroduction", "The magic number is... ");
//
//    // a list of strings that are treated as resource locations for items
//    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS = BUILDER
//            .comment("A list of items to log on common setup.")
//            .defineListAllowEmpty("items", List.of("minecraft:iron_ingot"), Config::validateItemName);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean logDirtBlock;
    public static int magicNumber;
    public static String magicNumberIntroduction;
    public static Set<Item> items;

    private static boolean validateItemName(final Object obj)
    {
        return obj instanceof final String itemName && ForgeRegistries.ITEMS.containsKey(ResourceLocation.parse(itemName));
    }

//    private static boolean validateBiomeName(final Object obj) {
////        if (!(obj instanceof String biomeStr)) {
////            return false;
////        }
////
////        ResourceLocation biomeResourceLocation = ResourceLocation.tryParse(biomeStr);
////
////        return biomeResourceLocation != null && ForgeRegistries.BIOMES.containsKey(biomeResourceLocation);
//
//        return obj instanceof final String biomeName && ForgeRegistries.BIOMES.containsKey(ResourceLocation.parse(biomeName));
//    }

    private static boolean validateWorldGenType(final Object input) {

        if (!(input instanceof String preferredWorldPreset)) {
            return false;
        }

        List<ResourceKey<WorldPreset>> WORLD_PRESETS = new ArrayList<>();
        WORLD_PRESETS.add(WorldPresets.SINGLE_BIOME_SURFACE);
        WORLD_PRESETS.add(WorldPresets.FLAT);
        WORLD_PRESETS.add(WorldPresets.AMPLIFIED);
        WORLD_PRESETS.add(WorldPresets.NORMAL);
        WORLD_PRESETS.add(WorldPresets.LARGE_BIOMES);

        ResourceLocation preferredLocation = ResourceLocation.tryParse(preferredWorldPreset);

        if (preferredLocation == null) {
            return false;
        }

        for (ResourceKey<WorldPreset> worldPresetResourceKey : WORLD_PRESETS) {
//            Preconfigured_worldgen_options.LOGGER.info(worldPresetResourceKey.location().toString());
            if (preferredLocation.equals(worldPresetResourceKey.location())) {
                return true;
            }
        }
        return false;
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
//        logDirtBlock = LOG_DIRT_BLOCK.get();
//        magicNumber = MAGIC_NUMBER.get();
//        magicNumberIntroduction = MAGIC_NUMBER_INTRODUCTION.get();
//
//
//        // convert the list of strings into a set of items
//        items = ITEM_STRINGS.get().stream()
//                .map(itemName -> ForgeRegistries.ITEMS.getValue(ResourceLocation.parse(itemName)))
//                .collect(Collectors.toSet());
    }
}
