/*
 * Copyright (c) 2020, 2021 Team Blue.
 *
 * This file is part of HealthMod Fabric.
 *
 * HealthMod Fabric is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * HealthMod Fabric is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with HealthMod Fabric.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.teambluemods.healthmod.registries

import io.github.teambluemods.healthmod.blocks.BandageBoxBlock
import io.github.teambluemods.healthmod.blocks.BloodTestMachineBlock
import io.github.teambluemods.healthmod.blocks.FirstAidKitBlock
import io.github.teambluemods.healthmod.util.LoggerDelegate
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.Material
import net.minecraft.block.MaterialColor
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.Rarity
import net.minecraft.util.registry.Registry

@Suppress("UNUSED")
object BlockRegistries : ModRegistry<Block> {
    override val registry: Registry<Block> get() = Registry.BLOCK

    private val LOGGER by LoggerDelegate()

    val BANDAGE_BOX = register(
        id = "bandage_box",
        toRegister = BandageBoxBlock(
            AbstractBlock.Settings
                .of(Material.CARPET, MaterialColor.WHITE)
                .sounds(BlockSoundGroup.WOOL)
                .breakInstantly()
        ),
        itemSettings = ItemRegistries.DEFAULT_SETTINGS
            .maxCount(1)
            .rarity(Rarity.UNCOMMON)
    )

    val BLOOD_TEST_MACHINE = register(
        id = "blood_test_machine",
        toRegister = BloodTestMachineBlock(
            AbstractBlock.Settings
                .of(Material.GLASS, MaterialColor.WHITE)
                .sounds(BlockSoundGroup.GLASS)
                .breakInstantly()
                .nonOpaque()
        ),
        itemSettings = ItemRegistries.DEFAULT_SETTINGS
            .maxCount(1)
            .rarity(Rarity.UNCOMMON)
    )

    val FIRST_AID_KIT = register(
        id = "first_aid_kit",
        toRegister = FirstAidKitBlock(
            AbstractBlock.Settings
                .of(Material.AGGREGATE, MaterialColor.ORANGE)
                .breakInstantly()
        ),
        itemSettings = ItemRegistries.DEFAULT_SETTINGS
            .maxCount(1)
            .rarity(Rarity.UNCOMMON)
    )

    fun register(
        id: String,
        toRegister: Block,
        itemSettings: Item.Settings = ItemRegistries.DEFAULT_SETTINGS
    ): Block {
        val blockItem = ItemRegistries.register(id, BlockItem(toRegister, itemSettings)) as BlockItem

        blockItem.appendBlocks(Item.BLOCK_ITEMS, blockItem)

        LOGGER.debug("Automatically registered block item $blockItem with custom item properties $itemSettings.")

        return this.register(id, toRegister)
    }
}