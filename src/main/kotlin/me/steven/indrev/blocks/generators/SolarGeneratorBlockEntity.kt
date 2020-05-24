package me.steven.indrev.blocks.generators

import me.steven.indrev.content.MachineRegistry
import me.steven.indrev.inventories.DefaultSidedInventory
import net.minecraft.block.BlockState
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SidedInventory
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorld

class SolarGeneratorBlockEntity : GeneratorBlockEntity(MachineRegistry.SOLAR_GENERATOR_BLOCK_ENTITY, 0.1, 32.0) {
    private val inventory = DefaultSidedInventory(0, intArrayOf(), intArrayOf())

    override fun shouldGenerate(): Boolean = this.world?.isSkyVisible(pos.up()) == true && this.world?.isDay == true

    override fun getInventory(): Inventory = inventory

    override fun getInventory(state: BlockState?, world: IWorld?, pos: BlockPos?): SidedInventory = inventory

    override fun getMaxOutput(): Double = 32.0
}