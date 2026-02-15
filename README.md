# [PWgO] Preconfiguired WorldGen Options

Mod was created specially for **Minecraft: Below Zero modpack**, because players rarely read the entire description.
This mod is intended to solve my problem: default world generation type doesn't feel snowy enough so to make it as i want player has to set world generation type to single biome with
snowy taiga and some structure generation settings additionally disabled.

# How to configure it?

In config directory of the game there will be file `preconfigured_worldgen_options-common.toml`, open it and there will be this:

```toml
#Use config preset by default instead of manual selection
useConfigPreset = false
#Makes 'Use Config Preset' button in world creation screen active/disabled. If false, field 'useConfigPreset' is always false!
IsUsePresetButtonActive = true
#if you wish, you can change hover button tooltip
buttonHoverTooltip = "If ON, world generation parameters will be set automatically according to the configuration settings. So if this mod is here, it means the developer has set the most recommended generation settings. You can enable it and check setting yourself. It only overrides currently set settings in GUI."
#Target biome. Works only if minecraft:single_biome_surface present in worldGenType
biome = "minecraft:snowy_taiga"
#minecraft:normal/minecraft:single_biome_surface/minecraft:amplified/minecraft:flat/minecraft:large_biomes
worldGenType = "minecraft:single_biome_surface"
#Enable vanilla structures spawn
enableStructuresSpawn = false
#Enable vanilla bonus chest spawn
enableBonusChest = false
```
Naming is pretty wacky, i know. Basically here is all the information you need to make it work.\
<br>


## Let's look at the main points in more detail

```toml
#Use config preset by default instead of manual selection
useConfigPreset = false
```
> [!NOTE]
> This flag determines whether the button will be immediately active when entering the world creation page.

> [!IMPORTANT]
> Flag is dynamic, so if you enter the screen then disable button "Use Config Preset", flag in the config will also change.

<br>
<br>

```toml
#Makes 'Use Config Preset' button in world creation screen active/disabled. If false, field 'useConfigPreset' is always false!
IsUsePresetButtonActive = true
```
> [!NOTE]
> This flag determines whether the button is active in the sense of being clickable. I don't know why I left it in, as it was done for debugging purposes.

<br>
<br>

```toml
#if you wish, you can change hover button tooltip
buttonHoverTooltip = "some text"
```

> [!NOTE]
> You can set any text here you want, so the default is some information for user.

<br>
<br>

```toml
#Target biome. Works only if minecraft:single_biome_surface present in worldGenType
biome = "minecraft:snowy_taiga"
```

> [!NOTE]
> Target biome that will be used in generation.

> [!IMPORTANT]
> This field only works if worldGenType = "minecraft:single_biome_surface" or, more simply, a single biome.

> [!TIP]
> If you want to ensure if the name you entered is correct, find the biome you want then via F3 on left side of the screen in center you can check biome name you're currently in.

<br>
<br>

```toml
#minecraft:normal/minecraft:single_biome_surface/minecraft:amplified/minecraft:flat/minecraft:large_biomes
worldGenType = "minecraft:single_biome_surface"
```

> [!NOTE]
> Describes what world type will be used. Vanilla world types:\
> - minecraft:normal
> - minecraft:single_biome_surface
> - minecraft:amplified
> - minecraft:flat
> - minecraft:large_biomes

> [!WARNING]
> I haven't tested if the mod can use modded world types. If you tried it, let me know.

<br>
<br>

```toml
#Enable vanilla structures spawn
enableStructuresSpawn = false
```

> [!NOTE]
> Enable/disable structure generation

<br>
<br>

```toml
#Enable vanilla bonus chest spawn
enableBonusChest = false
```

> [!NOTE]
> Does anyone use it?
