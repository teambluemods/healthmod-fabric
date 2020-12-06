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

package io.github.blueminecraftteam.healthmod.blocks.entities

import io.github.blueminecraftteam.healthmod.client.gui.screen.BloodTestMachineScreenHandler
import io.github.blueminecraftteam.healthmod.inventories.ImplementedInventory
import io.github.blueminecraftteam.healthmod.registries.BlockEntityTypeRegistries
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventories
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.text.TranslatableText
import net.minecraft.util.Tickable
import net.minecraft.util.collection.DefaultedList

class BloodTestMachineBlockEntity : BlockEntity(BlockEntityTypeRegistries.BLOOD_TEST_MACHINE), NamedScreenHandlerFactory, Tickable,
    ImplementedInventory {
    override val items: DefaultedList<ItemStack> = DefaultedList.ofSize(3, ItemStack.EMPTY)

    override fun createMenu(syncId: Int, inv: PlayerInventory, player: PlayerEntity) =
        BloodTestMachineScreenHandler(syncId, inv, this)

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

    override fun tick() {

    }
}