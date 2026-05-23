package com.possible_triangle.arboretum

import com.possible_triangle.com.possible_triangle.arboretum.Constants
import com.possible_triangle.tree_shapes.foliage.AdjustingBlobFoliagePlacer
import com.possible_triangle.tree_shapes.trunk.RootedTrunkPlacer
import net.minecraft.core.Vec3i
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.random.SimpleWeightedRandomList
import net.minecraft.util.valueproviders.ConstantInt
import net.minecraft.util.valueproviders.IntProvider
import net.minecraft.util.valueproviders.WeightedListInt
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.levelgen.Heightmap
import net.minecraft.world.level.levelgen.blockpredicates.WouldSurvivePredicate
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration.TreeConfigurationBuilder
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider
import net.minecraft.world.level.levelgen.placement.BiomeFilter
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter
import net.minecraft.world.level.levelgen.placement.CountPlacement
import net.minecraft.world.level.levelgen.placement.HeightmapPlacement
import net.minecraft.world.level.levelgen.placement.InSquarePlacement
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.minecraft.world.level.levelgen.placement.SurfaceWaterDepthFilter
import java.util.*

fun weightedIntList(vararg entries: Pair<Int, Int>) = WeightedListInt(
    SimpleWeightedRandomList.builder<IntProvider>().apply {
        entries.forEach { (value, weight) ->
            add(ConstantInt.of(value), weight)
        }
    }.build()
)


fun FeatureOutput.tree(path: String) {
    val id = ResourceLocation.fromNamespaceAndPath(Constants.NAMESPACE, "tree/$path")
    val configured = registerConfigured(
        id,
        ConfiguredFeature(
            Feature.TREE,
            TreeConfigurationBuilder(
                BlockStateProvider.simple(Blocks.OAK_LOG),
                RootedTrunkPlacer(10, 3, 2, Optional.of(BlockStateProvider.simple(Blocks.OAK_WOOD))),
                BlockStateProvider.simple(Blocks.OAK_LEAVES),
                AdjustingBlobFoliagePlacer(ConstantInt.of(3), ConstantInt.of(0), 4),
                TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4))
            ).ignoreVines().build()
        )
    )

    registerPlaced(
        id,
        PlacedFeature(
            configured,
            listOf(
                CountPlacement.of(weightedIntList(10 to 9, 11 to 1)),
                InSquarePlacement.spread(),
                SurfaceWaterDepthFilter.forMaxDepth(0),
                HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR),
                BiomeFilter.biome(),
                BlockPredicateFilter.forPredicate(
                    WouldSurvivePredicate(Vec3i.ZERO, Blocks.OAK_SAPLING.defaultBlockState())
                )
            )
        )
    )
}

fun registerFeatures(output: FeatureOutput) {
    output.tree("test")
}
