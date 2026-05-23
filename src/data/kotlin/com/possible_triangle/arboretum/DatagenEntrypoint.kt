package com.possible_triangle.arboretum

import com.possible_triangle.com.possible_triangle.arboretum.Constants
import net.minecraft.DetectedVersion
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.data.metadata.PackMetadataGenerator
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.metadata.pack.PackMetadataSection
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider
import net.neoforged.neoforge.data.event.GatherDataEvent
import java.util.Optional

@EventBusSubscriber(modid = Constants.NAMESPACE)
object DatagenEntrypoint {

    @SubscribeEvent
    fun gatherData(event: GatherDataEvent) {
        event.generator.addProvider(
            true, PackMetadataGenerator(event.generator.packOutput).add(
                PackMetadataSection.TYPE, PackMetadataSection(
                    Component.literal("Bigger & Fancier trees for more convincing forests"),
                    DetectedVersion.BUILT_IN.getPackVersion(PackType.CLIENT_RESOURCES),
                    Optional.empty()
                )
            )
        )

        event.generator.addProvider(
            true, DatapackBuiltinEntriesProvider(
                event.generator.packOutput, event.lookupProvider,
                RegistrySetBuilder().apply {
                    add(Registries.CONFIGURED_FEATURE) { registerFeatures(it.configuredOutput()) }
                    add(Registries.PLACED_FEATURE) { registerFeatures(it.placedOutput()) }
                },
                setOf(ResourceLocation.DEFAULT_NAMESPACE, Constants.NAMESPACE)
            )
        )
    }

}
