/*
 * Copyright (c) 2020, 2021 Blue Minecraft Team.
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

package io.github.blueminecraftteam.healthmod.inventories

import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.text.TranslatableText
import net.minecraft.util.collection.DefaultedList

/**
 * Simple block entity and inventory to reduce boilerplate.
 */
open class SimpleBlockEntityInventory(
    type: BlockEntityType<*>,
    private val menuFactory: (Int, PlayerInventory, Inventory) -> ScreenHandler,
    size: Int
) : BlockEntity(type), ImplementedInventory, NamedScreenHandlerFactory {
    override val items: DefaultedList<ItemStack> = DefaultedList.ofSize(size, ItemStack.EMPTY)

    override fun createMenu(syncId: Int, inv: PlayerInventory, player: PlayerEntity) = menuFactory(syncId, inv, this)

    override fun getDisplayName() = TranslatableText(cachedState.block.translationKey)

    override fun fromTag(state: BlockState, tag: CompoundTag) {
        super.fromTag(state, tag)

        Inventories.fromTag(tag, this.items)
    }

    override fun toTag(tag: CompoundTag): CompoundTag {
        Inventories.toTag(tag, this.items)

        return super.toTag(tag)
    }

    override fun markDirty() {}
}