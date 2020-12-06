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

import io.github.blueminecraftteam.healthmod.client.gui.screen.BandageBoxScreenHandler
import io.github.blueminecraftteam.healthmod.inventories.SimpleBlockEntityInventory
import io.github.blueminecraftteam.healthmod.registries.BlockEntityTypeRegistries
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.text.TranslatableText

class BandageBoxBlockEntity : SimpleBlockEntityInventory(type = BlockEntityTypeRegistries.BANDAGE_BOX, size = 6),
    NamedScreenHandlerFactory {
    override fun createMenu(syncId: Int, inv: PlayerInventory, player: PlayerEntity) =
        BandageBoxScreenHandler(syncId, inv, this)

    override fun getDisplayName() = TranslatableText(cachedState.block.translationKey)
}