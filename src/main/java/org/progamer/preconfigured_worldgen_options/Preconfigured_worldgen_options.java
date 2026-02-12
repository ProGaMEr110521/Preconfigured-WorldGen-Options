package org.progamer.preconfigured_worldgen_options;

import com.mojang.logging.LogUtils;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import org.slf4j.Logger;

@SuppressWarnings("removal")
@Mod(Preconfigured_worldgen_options.MODID)
public class Preconfigured_worldgen_options {

    public static final String MODID = "preconfigured_worldgen_options";

    public static final Logger LOGGER = LogUtils.getLogger();

    public Preconfigured_worldgen_options() {
        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
