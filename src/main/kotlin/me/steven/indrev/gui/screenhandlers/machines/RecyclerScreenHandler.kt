package me.steven.indrev.gui.screenhandlers.machines

import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import me.steven.indrev.IndustrialRevolution
import me.steven.indrev.gui.PatchouliEntryShortcut
import me.steven.indrev.gui.screenhandlers.IRGuiScreenHandler
import me.steven.indrev.utils.add
import me.steven.indrev.utils.configure
import me.steven.indrev.utils.createProcessBar
import me.steven.indrev.utils.identifier
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.util.Identifier

class RecyclerScreenHandler(syncId: Int, playerInventory: PlayerInventory, ctx: ScreenHandlerContext) :
    IRGuiScreenHandler(
        IndustrialRevolution.RECYCLER_HANDLER,
        syncId,
        playerInventory,
        ctx
    ), PatchouliEntryShortcut {
    init {
        val root = WGridPanel()
        setRootPanel(root)
        configure("block.indrev.recycler", ctx, playerInventory, blockInventory)

        val inputSlot = WItemSlot.of(blockInventory, 2)
        root.add(inputSlot, 3.0, 2.0)

        val processWidget = createProcessBar()
        root.add(processWidget, 4.15, 2.0)

        val outputSlot = WItemSlot.outputOf(blockInventory, 3)
        outputSlot.isInsertingAllowed = false
        root.add(outputSlot, 5.64, 2.0)

        root.validate(this)
    }

    override fun canUse(player: PlayerEntity?): Boolean = true

    override fun getEntry(): Identifier = identifier("machines/basic_machines")

    override fun getPage(): Int = 8

    companion object {
        val SCREEN_ID = identifier("recycler")
    }
}