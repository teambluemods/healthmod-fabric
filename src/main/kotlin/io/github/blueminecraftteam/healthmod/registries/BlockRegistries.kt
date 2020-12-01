/*
 * Copyright (c) 2020 Blue Minecraft Team.
 *
 * This file is part of HealthMod.
 *
 * HealthMod is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * HealthMod is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with HealthMod.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.blueminecraftteam.healthmod.registries

import io.github.blueminecraftteam.healthmod.HealthMod
import io.github.blueminecraftteam.healthmod.blocks.BandAidBoxBlock
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.Material
import net.minecraft.block.MaterialColor
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.Rarity
import net.minecraft.util.registry.Registry

object BlockRegistries : ModRegistry<Block> {
    override val registry: Registry<Block>
        get() = Registry.BLOCK

    val BAND_AID_BOX = register(
        id = "band_aid_box",
        toRegister = BandAidBoxBlock(
            AbstractBlock.Settings.of(Material.WOOL, MaterialColor.WHITE)
                .nonOpaque()
                .dropsNothing()
                .sounds(BlockSoundGroup.WOOL)
                .strength(0F, 0F)
        ),
        customItemProperties = Item.Settings()
            .group(HealthMod.ITEM_GROUP)
            .maxCount(1)
            .rarity(Rarity.UNCOMMON)
    )

    private fun register(id: String, toRegister: Block, customItemProperties: Item.Settings? = null): Block {
        ItemRegistries
            .register(
                id,
                BlockItem(toRegister, customItemProperties ?: Item.Settings().group(HealthMod.ITEM_GROUP))
            )
            .run { this as BlockItem }
            .apply { appendBlocks(Item.BLOCK_ITEMS, this) }

        return register(id, toRegister)
    }

    override fun register(id: String, toRegister: Block) = register(id, toRegister, customItemProperties = null)
}