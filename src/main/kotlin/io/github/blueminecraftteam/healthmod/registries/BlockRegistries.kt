/*
 * Copyright (c) 2020 Blue Minecraft Team.
 *
 * This file is part of HealthMod.
 *
 * HealthMod is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * HealthMod is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with HealthMod.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.blueminecraftteam.healthmod.registries

import io.github.blueminecraftteam.healthmod.HealthMod
import io.github.blueminecraftteam.healthmod.blocks.BandageBoxBlock
import io.github.blueminecraftteam.healthmod.blocks.BloodTestMachineBlock
import io.github.blueminecraftteam.healthmod.blocks.FirstAidKitBlock
import io.github.blueminecraftteam.healthmod.util.LoggerDelegate
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.Material
import net.minecraft.block.MaterialColor
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.Rarity
import net.minecraft.util.registry.Registry
import org.apache.logging.log4j.Logger

@Suppress("UNUSED")
object BlockRegistries : ModRegistry<Block> {
    override val registry: Registry<Block> get() = Registry.BLOCK

    val BANDAGE_BOX = register(
        id = "bandage_box",
        toRegister = BandageBoxBlock(
            AbstractBlock.Settings
                .of(Material.CARPET, MaterialColor.WHITE)
                .sounds(BlockSoundGroup.WOOL)
                .strength(0F, 0F)
        ),
        customItemProperties = Item.Settings()
            .group(HealthMod.ITEM_GROUP)
            .maxCount(1)
            .rarity(Rarity.UNCOMMON)
    )

    val BLOOD_TEST_MACHINE = register(
        id = "blood_test_machine",
        toRegister = BloodTestMachineBlock(
            AbstractBlock.Settings
                .of(Material.GLASS, MaterialColor.WHITE)
                .sounds(BlockSoundGroup.GLASS)
                .strength(0F, 0F)
                .nonOpaque()
        ),
        customItemProperties = Item.Settings()
            .group(HealthMod.ITEM_GROUP)
            .maxCount(1)
            .rarity(Rarity.UNCOMMON)
    )

    val FIRST_AID_KIT = register(
        id = "first_aid_kit",
        toRegister = FirstAidKitBlock(
            AbstractBlock.Settings
                .of(Material.AGGREGATE, MaterialColor.ORANGE)
                .strength(0F, 0F)
        ),
        customItemProperties = Item.Settings()
            .group(HealthMod.ITEM_GROUP)
            .maxCount(1)
            .rarity(Rarity.UNCOMMON)
    )

    private val logger: Logger by LoggerDelegate()

    private fun register(id: String, toRegister: Block, customItemProperties: Item.Settings? = null): Block {
        val blockItem = ItemRegistries.register(
            id,
            BlockItem(toRegister, customItemProperties ?: Item.Settings().group(HealthMod.ITEM_GROUP))
        ) as BlockItem

        blockItem.appendBlocks(Item.BLOCK_ITEMS, blockItem)

        logger.debug("Automatically registered block item $blockItem with custom item properties $customItemProperties.")

        return register(id, toRegister)
    }
}