package me.steven.indrev.blockentities.crafters

import me.steven.indrev.components.CraftingComponent
import me.steven.indrev.components.TemperatureComponent
import me.steven.indrev.inventories.inventory
import me.steven.indrev.items.upgrade.Upgrade
import me.steven.indrev.mixin.MixinAbstractCookingRecipe
import me.steven.indrev.registry.MachineRegistry
import me.steven.indrev.utils.Tier
import net.minecraft.recipe.RecipeType
import net.minecraft.screen.ArrayPropertyDelegate

class ElectricFurnaceFactoryBlockEntity(tier: Tier) :
    CraftingMachineBlockEntity<MixinAbstractCookingRecipe>(tier, MachineRegistry.ELECTRIC_FURNACE_FACTORY_REGISTRY) {

    init {
        this.propertyDelegate = ArrayPropertyDelegate(15)
        this.temperatureComponent = TemperatureComponent({ this }, 0.1, 1300..1700, 2000.0)
        this.inventoryComponent = inventory(this) {
            input {
                slots = when (tier) {
                    Tier.MK1 -> intArrayOf(6, 8)
                    Tier.MK2 -> intArrayOf(6, 8, 10)
                    Tier.MK3 -> intArrayOf(6, 8, 10, 12)
                    else -> intArrayOf(6, 8, 10, 12, 14)
                }
            }
            output {
                slots = when (tier) {
                    Tier.MK1 -> intArrayOf(7, 9)
                    Tier.MK2 -> intArrayOf(7, 9, 11)
                    Tier.MK3 -> intArrayOf(7, 9, 11, 13)
                    else ->  intArrayOf(7, 9, 11, 13, 15)
                }
            }
        }
        this.craftingComponents = Array(5) { index ->
            CraftingComponent(index, this).apply {
                inputSlots = intArrayOf(6 + (index * 2))
                outputSlots = intArrayOf(6 + (index * 2) + 1)
            }
        }
    }

    override val type: RecipeType<MixinAbstractCookingRecipe>
        get() {
            val upgrades = getUpgrades(inventoryComponent!!.inventory)
            return when (upgrades.keys.firstOrNull { it == Upgrade.BLAST_FURNACE || it == Upgrade.SMOKER }) {
                Upgrade.BLAST_FURNACE -> RecipeType.BLASTING
                Upgrade.SMOKER -> RecipeType.SMOKING
                else -> RecipeType.SMELTING
            } as RecipeType<MixinAbstractCookingRecipe>
        }

    override fun getUpgradeSlots(): IntArray = intArrayOf(2, 3, 4, 5)

    override fun getAvailableUpgrades(): Array<Upgrade> = Upgrade.values()
}