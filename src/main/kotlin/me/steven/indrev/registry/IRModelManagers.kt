package me.steven.indrev.registry

import me.steven.indrev.blocks.PumpPipeBakedModel
import me.steven.indrev.blocks.containers.LazuliFluxContainerBakedModel
import me.steven.indrev.blocks.machine.DrillHeadModel
import me.steven.indrev.utils.SimpleBlockModel
import me.steven.indrev.utils.identifier
import net.fabricmc.fabric.api.client.model.ModelAppender
import net.fabricmc.fabric.api.client.model.ModelProviderContext
import net.fabricmc.fabric.api.client.model.ModelVariantProvider
import net.minecraft.client.render.model.UnbakedModel
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.resource.ResourceManager
import java.util.function.Consumer

object IRModelManagers : ModelVariantProvider, ModelAppender {

    private val LFC_OVERLAY_REGEX = Regex("lazuli_flux_container_(input|output|item_lf_level|mk[1-4]_overlay)")

    override fun loadModelVariant(resourceId: ModelIdentifier, ctx: ModelProviderContext?): UnbakedModel? {
        if (resourceId.namespace != "indrev") return null
        val path = resourceId.path
        return when {
            path == "drill_head" -> DrillHeadModel(resourceId.variant)
            path == "pump_pipe" -> PumpPipeBakedModel()
            LFC_OVERLAY_REGEX.matches(path) -> SimpleBlockModel(path)
            path.startsWith("lazuli_flux_container") -> LazuliFluxContainerBakedModel(path.replace("creative", "mk4"))
            MaterialHelper.MATERIAL_PROVIDERS.containsKey(resourceId) -> MaterialHelper.MATERIAL_PROVIDERS[resourceId]
            else -> return null
        }
    }

    override fun appendAll(manager: ResourceManager?, out: Consumer<ModelIdentifier>) {
        out.accept(ModelIdentifier(identifier("drill_head"), "stone"))
        out.accept(ModelIdentifier(identifier("drill_head"), "iron"))
        out.accept(ModelIdentifier(identifier("drill_head"), "diamond"))
        out.accept(ModelIdentifier(identifier("drill_head"), "netherite"))
        out.accept(ModelIdentifier(identifier("pump_pipe"), ""))
        out.accept(ModelIdentifier(identifier("lazuli_flux_container_input"), ""))
        out.accept(ModelIdentifier(identifier("lazuli_flux_container_output"), ""))
        out.accept(ModelIdentifier(identifier("lazuli_flux_container_item_lf_level"), ""))
        out.accept(ModelIdentifier(identifier("lazuli_flux_container_mk1_overlay"), ""))
        out.accept(ModelIdentifier(identifier("lazuli_flux_container_mk2_overlay"), ""))
        out.accept(ModelIdentifier(identifier("lazuli_flux_container_mk3_overlay"), ""))
        out.accept(ModelIdentifier(identifier("lazuli_flux_container_mk4_overlay"), ""))
        out.accept(ModelIdentifier(identifier("ingot_base"), "inventory"))
        out.accept(ModelIdentifier(identifier("ingot_shadow"), "inventory"))
        out.accept(ModelIdentifier(identifier("ingot_highlight"), "inventory"))
    }
}