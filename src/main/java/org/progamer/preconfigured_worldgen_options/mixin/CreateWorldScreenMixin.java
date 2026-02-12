package org.progamer.preconfigured_worldgen_options.mixin;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.components.tabs.TabNavigationBar;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.PresetEditor;
import net.minecraft.client.gui.screens.worldselection.WorldCreationContext;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState;

import org.progamer.preconfigured_worldgen_options.Config;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;


@SuppressWarnings("JavaReflectionMemberAccess")
@Mixin(value = CreateWorldScreen.class)
public abstract class CreateWorldScreenMixin {

    @Shadow @Final
    WorldCreationUiState uiState;
    @Shadow @Nullable private TabNavigationBar tabNavigationBar;

    @Unique
    private boolean preconfigured_worldgen_options$useConfigPreset = Config.USE_CONFIG_PRESET.get();

    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        CreateWorldScreen screen = (CreateWorldScreen) (Object) this;

        Button preconfigured_worldgen_options$configToggleButton = Button.builder(
                        Component.literal("Use Config Preset: " + (preconfigured_worldgen_options$useConfigPreset ? "ON" : "OFF")),
                        button -> {
                            preconfigured_worldgen_options$useConfigPreset = !preconfigured_worldgen_options$useConfigPreset;
                            Config.USE_CONFIG_PRESET.set(preconfigured_worldgen_options$useConfigPreset);
                            button.setMessage(Component.literal("Use Config Preset: " + (preconfigured_worldgen_options$useConfigPreset ? "ON" : "OFF")));

                            if (preconfigured_worldgen_options$useConfigPreset) {
                                preconfigured_worldgen_options$applyConfigPreset();
                                preconfigured_worldgen_options$changeButtonsActiveState(false);
                            } else {
                                preconfigured_worldgen_options$changeButtonsActiveState(true);
                            }
                        }
                )
                .bounds(screen.width / 2 - 105, screen.height / 2 + screen.height / 4, 210, 20)
                .tooltip(Tooltip.create(Component.literal(Config.BUTTON_HOVER_TOOLTIP.get())))
                .build();

        try {
            Method superMethod = Screen.class.getDeclaredMethod("m_142416_", GuiEventListener.class);
            superMethod.setAccessible(true);
            superMethod.invoke(screen, preconfigured_worldgen_options$configToggleButton);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Config.ENABLE_BUTTON.get()) {
            if (preconfigured_worldgen_options$useConfigPreset) {
                preconfigured_worldgen_options$applyConfigPreset();
                preconfigured_worldgen_options$changeButtonsActiveState(false);
            }
        } else {
            preconfigured_worldgen_options$configToggleButton.active = false;
            preconfigured_worldgen_options$configToggleButton.setMessage(Component.literal("Use Config Preset: OFF"));
            preconfigured_worldgen_options$configToggleButton.setTooltip(Tooltip.create(Component.literal("Button is disabled from configs, showUsePresetButton = false")));
        }
    }

    @Unique
    private void preconfigured_worldgen_options$changeButtonsActiveState(boolean active) {
        try {
            if (tabNavigationBar != null) {
                Field tabButtonsField = TabNavigationBar.class.getDeclaredField("f_267495_");
                tabButtonsField.setAccessible(true);
                ImmutableList<TabButton> tabButtons = (ImmutableList<TabButton>) tabButtonsField.get(tabNavigationBar);

                for (int i = 0; i < tabButtons.size(); i++) {
                    TabButton tabButton = tabButtons.get(i);
                    if (i == 0) {
                        tabButton.active = true;
                        tabButton.setTooltip(null);
                    } else {
                        tabButton.active = active;
                        if (!active) {
                            tabButton.setTooltip(Tooltip.create(
                                    Component.literal("This tab is disabled when config preset is active")
                            ));
                        } else {
                            tabButton.setTooltip(null);
                        }
                    }
                }

                if (!active) {
                    tabNavigationBar.selectTab(0, false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Unique
    private void preconfigured_worldgen_options$applyConfigPreset() {
        try {
            String presetName = Config.WORLD_GEN_TYPE.get();
            ResourceLocation presetLocation = ResourceLocation.tryParse(presetName);

            if (presetLocation == null) {
                return;
            }

            WorldCreationUiState uiState = this.uiState;

            List<WorldCreationUiState.WorldTypeEntry> presetList = uiState.getNormalPresetList();

            Object foundEntry = null;
            for (Object entry : presetList) {
                Field presetField = entry.getClass().getDeclaredField("f_267398_");
                presetField.setAccessible(true);
                Object entryPresetHolder = presetField.get(entry);

                if (entryPresetHolder instanceof Holder.Reference<?> holder) {
                    Optional<? extends ResourceKey<?>> optionalKey = holder.unwrapKey();

                    if (optionalKey.isPresent()) {
                        ResourceKey<?> key = optionalKey.get();

                        if (key.isFor(Registries.WORLD_PRESET) &&
                                key.location().equals(presetLocation)) {

                            Method setWorldTypeMethod = uiState.getClass().getMethod(
                                    "m_267576_",
                                    entry.getClass()
                            );

                            setWorldTypeMethod.invoke(uiState, entry);
                            foundEntry = entry;
                            break;
                        }
                    }
                }
            }

            uiState.setBonusChest(Config.ENABLE_BONUS_CHEST.get());
            uiState.setGenerateStructures(Config.ENABLE_STRUCTURES_SPAWN.get());

            if (foundEntry != null && "minecraft:single_biome_surface".equals(presetName)) {
                preconfigured_worldgen_options$applyBiomeForSingleBiomeSurface(uiState, uiState.getSettings(), ResourceLocation.tryParse(Config.BIOME_STRING.get()));
            }

        } catch (Exception e) {
            System.err.println("[PWgO] Failed to apply config preset: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Unique
    private void preconfigured_worldgen_options$applyBiomeForSingleBiomeSurface(WorldCreationUiState worldCreationUiState, WorldCreationContext worldCreationContext, ResourceLocation targetBiome) {
        try {
            RegistryAccess.Frozen registryAccess = worldCreationContext.worldgenLoadContext();

            Registry<Biome> biomeRegistry = registryAccess.registryOrThrow(Registries.BIOME);
            ResourceKey<Biome> biomeKey = ResourceKey.create(Registries.BIOME, targetBiome);
            Holder<Biome> biomeHolder = biomeRegistry.getHolderOrThrow(biomeKey);

            PresetEditor presetEditor = worldCreationUiState.getPresetEditor();

            if (presetEditor != null) {
                Method fixedBiomeConfiguratorMethod = PresetEditor.class.getDeclaredMethod(
                        "m_232952_",
                        Holder.class
                );
                fixedBiomeConfiguratorMethod.setAccessible(true);

                WorldCreationContext.DimensionsUpdater dimensionsUpdater =
                        (WorldCreationContext.DimensionsUpdater) fixedBiomeConfiguratorMethod.invoke(
                                null,
                                biomeHolder
                        );

                WorldCreationContext newContext = worldCreationContext.withDimensions(dimensionsUpdater);

                Method setSettingsMethod = worldCreationUiState.getClass().getMethod(
                        "m_267692_",
                        WorldCreationContext.class
                );
                setSettingsMethod.invoke(worldCreationUiState, newContext);
            }

        } catch (Exception e) {
            System.err.println("[PWgO] Failed to apply biome: " + e.getMessage());
            e.printStackTrace();
        }
    }
}