package org.progamer.preconfigured_worldgen_options;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

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


    static final ForgeConfigSpec SPEC = BUILDER.build();

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
            if (preferredLocation.equals(worldPresetResourceKey.location())) {
                return true;
            }
        }
        return false;
    }
}
