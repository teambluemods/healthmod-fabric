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

package io.github.blueminecraftteam.healthmod.inventories

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.util.collection.DefaultedList

/**
 * A simple `Inventory` implementation with only default methods + an item list getter.
 *
 * Taken from [the Fabric wiki](https://fabricmc.net/wiki/tutorial:inventory).
 *
 * Adapted to follow Kotlin conventions more closely.
 *
 * @author Juuz
 */
interface ImplementedInventory : Inventory {
    /**
     * Item list of this inventory.
     * Getter must return the same instance every time it's called.
     */
    val items: DefaultedList<ItemStack>

    /**
     * Returns the inventory size.
     */
    override fun size() = items.size

    /**
     * Returns if this inventory is empty (has only empty stacks)
     */
    override fun isEmpty(): Boolean = items.all { it.isEmpty }

    /**
     * Retrieves the item in the slot.
     */
    override fun getStack(slot: Int) = items[slot]

    /**
     * Removes items from an inventory slot.
     * Count to remove can be specified using [count].
     *
     * If there are less items in the slot than what are requested,
     * takes all items in that slot.
     */
    override fun removeStack(slot: Int, count: Int): ItemStack {
        val result = Inventories.splitStack(items, slot, count)

        if (!result.isEmpty) {
            this.markDirty()
        }

        return result
    }

    /**
     * Removes all items from an inventory slot.
     */
    override fun removeStack(slot: Int): ItemStack = Inventories.removeStack(items, slot)

    /**
     * Replaces the current stack in an inventory slot with the provided stack.
     *
     * If the stack is too big for
     * this inventory (see [Inventory.getMaxCountPerStack]),
     * it gets resized to this inventory's maximum amount.
     */
    override fun setStack(slot: Int, stack: ItemStack) {
        items[slot] = stack

        if (stack.count > maxCountPerStack) {
            stack.count = maxCountPerStack
        }
    }

    /**
     * Clears the inventory.
     */
    override fun clear() {
        items.clear()
    }

    override fun markDirty() {}

    /**
     * Returns if the player can use the inventory.
     */
    override fun canPlayerUse(player: PlayerEntity) = true

    companion object Factory {
        /**
         * Creates an inventory from the item list.
         */
        // why is this an object you ask? it's because intellijank wont let me do ImplementedInventory { items }
        fun of(items: DefaultedList<ItemStack>) = object : ImplementedInventory {
            override val items: DefaultedList<ItemStack> = items
        }

        /**
         * Creates a new inventory with the specified size.
         */
        fun ofSize(size: Int) = of(DefaultedList.ofSize(size, ItemStack.EMPTY))
    }
}
