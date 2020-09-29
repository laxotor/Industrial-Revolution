package me.steven.indrev.gui.controllers.machines

import io.github.cottonmc.cotton.gui.widget.WBar
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment
import me.steven.indrev.IndustrialRevolution
import me.steven.indrev.blockentities.crafters.ElectricFurnaceFactoryBlockEntity
import me.steven.indrev.gui.PatchouliEntryShortcut
import me.steven.indrev.gui.controllers.IRGuiController
import me.steven.indrev.gui.widgets.machines.WProcess
import me.steven.indrev.gui.widgets.misc.WText
import me.steven.indrev.utils.add
import me.steven.indrev.utils.configure
import me.steven.indrev.utils.identifier
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.text.TranslatableText
import net.minecraft.util.Identifier

class ElectricFurnaceFactoryController(
    syncId: Int,
    playerInventory: PlayerInventory,
    ctx: ScreenHandlerContext
) :
    IRGuiController(
        IndustrialRevolution.ELECTRIC_FURNACE_FACTORY_HANDLER,
        syncId,
        playerInventory,
        ctx
    ), PatchouliEntryShortcut {
    init {
        val root = WGridPanel()
        setRootPanel(root)
        configure("block.indrev.electric_furnace", ctx, playerInventory, blockInventory, propertyDelegate)
        root.add(WText(TranslatableText("block.indrev.factory"), HorizontalAlignment.CENTER, 0x404040), 4.3, 0.5)
        ctx.run { world, pos ->
            val blockEntity = world.getBlockEntity(pos) as? ElectricFurnaceFactoryBlockEntity ?: return@run
            val slotsAmount = (blockEntity.tier.ordinal + 2).coerceAtMost(5)
            val offset = (10 - slotsAmount) / 2.0

            for ((index, slot) in blockEntity.inventoryComponent!!.inventory.inputSlots.withIndex()) {
                val inputSlot = WItemSlot.of(blockInventory, slot)
                root.add(inputSlot, offset + index, 1.2)
            }

            for (i in 0 until slotsAmount) {
                val processWidget = WBar(WProcess.PROCESS_VERTICAL_EMPTY, WProcess.PROCESS_VERTICAL_FULL, 3 + (i * 2), 4 + (i * 2), WBar.Direction.DOWN)
                root.add(processWidget, offset + i, 2.3)
            }

            for ((index, slot) in blockEntity.inventoryComponent!!.inventory.outputSlots.withIndex()) {
                val outputSlot = WItemSlot.of(blockInventory, slot)
                root.add(outputSlot, offset + index, 3.4)
                outputSlot.addChangeListener { _, _, _, _ ->
                    val player = playerInventory.player
                    if (!player.world.isClient) {
                        blockEntity.dropExperience(player)
                    }
                }
                outputSlot.isInsertingAllowed = false
            }

        }
        root.validate(this)
    }

    override fun canUse(player: PlayerEntity?): Boolean = true

    override fun getEntry(): Identifier = identifier("machines/basic_machines")

    override fun getPage(): Int = 1

    companion object {
        val SCREEN_ID = identifier("electric_furnace_factory_screen")
    }
}