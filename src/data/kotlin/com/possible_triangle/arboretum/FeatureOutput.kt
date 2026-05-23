package com.possible_triangle.arboretum

import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.placement.PlacedFeature

data class FeatureOutput(
    val registerConfigured: (ResourceLocation, ConfiguredFeature<*, *>) -> Holder<ConfiguredFeature<*, *>>,
    val registerPlaced: (ResourceLocation, PlacedFeature) -> Unit,
)

fun BootstrapContext<ConfiguredFeature<*, *>>.configuredOutput() = FeatureOutput(
    registerConfigured = { id, value ->
        register(
            ResourceKey.create(Registries.CONFIGURED_FEATURE, id),
            value
        )
    },
    registerPlaced = { _, _ -> }
)

fun BootstrapContext<PlacedFeature>.placedOutput() = FeatureOutput(
    registerConfigured = { id, _ ->
        lookup(Registries.CONFIGURED_FEATURE).getOrThrow(
            ResourceKey.create(Registries.CONFIGURED_FEATURE, id)
        )
    },
    registerPlaced = { id, value ->
        register(
            ResourceKey.create(Registries.PLACED_FEATURE, id),
            value
        )
    },
)
