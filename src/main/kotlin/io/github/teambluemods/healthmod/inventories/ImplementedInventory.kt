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

package io.github.teambluemods.healthmod.inventories

import io.github.teambluemods.healthmod.util.extensions.isNotEmpty
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.Direction

/**
 * A simple [SidedInventory] implementation with only implemented methods and an [item list][items].
 *
 * ## Reading and writing to tags
 *
 * Use [Inventories.fromTag] and [Inventories.toTag] on [the item list][items].
 *
 * ## License
 *
 * Original license: [CC0](https://creativecommons.org/publicdomain/zero/1.0/)
 *
 * License: [LGPL v3][https://www.gnu.org/licenses/lgpl-3.0]
 *
 * Taken from [this GitHub Gist][https://gist.github.com/Juuxel/32ea65a4c820b63c20798917f0f73706].
 *
 * @author Juuz
 */
interface ImplementedInventory : SidedInventory {
    /**
     * The item list of this inventory.
     */
    val items: DefaultedList<ItemStack>

    /**
     * Gets the available slots to automation on the side.
     */
    override fun getAvailableSlots(side: Direction) = IntArray(items.size).apply {
        for (i in indices) this[i] = i
    }

    /**
     * Returns if the stack can be inserted in the slot at the side.
     */
    override fun canInsert(slot: Int, stack: ItemStack, side: Direction?) = true

    /**
     * Returns if the stack can be extracted from the slot at the side.
     */
    override fun canExtract(slot: Int, stack: ItemStack, side: Direction) = true

    /**
     * Returns the inventory size.
     */
    override fun size() = items.size

    /**
     * Returns if this inventory has only empty stacks.
     */
    override fun isEmpty() = items.all { it.isEmpty }

    /**
     * Gets the item stack in the [slot].
     */
    override fun getStack(slot: Int) = items[slot]

    /**
     * Takes a stack of the size from the slot.
     */
    override fun removeStack(slot: Int, amount: Int): ItemStack = Inventories.splitStack(items, slot, amount)
        .also { if (it.isNotEmpty) this.markDirty() }

    /**
     * Removes the current stack in the [slot] and returns it.
     */
    override fun removeStack(slot: Int): ItemStack = Inventories.removeStack(items, slot)

    /**
     * Replaces the current stack in the [slot] with the [stack].
     *
     * If the stack is too big for this inventory (see [getMaxCountPerStack]),
     * it gets resized to this inventory's maximum amount.
     */
    override fun setStack(slot: Int, stack: ItemStack) {
        items[slot] = stack

        if (stack.count > maxCountPerStack) stack.count = maxCountPerStack
    }

    /**
     * Clears [the item list][items].
     */
    override fun clear() {
        items.clear()
    }

    override fun markDirty() {}

    override fun canPlayerUse(player: PlayerEntity) = true

    companion object Factory {
        /**
         * Creates an inventory from the [item list][items].
         */
        fun of(items: DefaultedList<ItemStack>) = object : ImplementedInventory {
            override val items = items
        }

        /**
         * Creates a new inventory with the [size] provided.
         */
        fun ofSize(size: Int) = of(DefaultedList.ofSize(size, ItemStack.EMPTY))
    }
}
