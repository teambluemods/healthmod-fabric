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

package io.github.blueminecraftteam.healthmod.util

import net.minecraft.block.Block
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.item.Item
import net.minecraft.util.registry.Registry
import org.apache.commons.lang3.text.WordUtils

fun <T> T?.throwIfNull(message: String? = null) = this ?: error(message ?: "No message")

fun String.capitalizeFully(): String = WordUtils.capitalizeFully(this)

val Item.id get() = Registry.ITEM.getId(this)

val Block.id get() = Registry.BLOCK.getId(this)

val StatusEffect.id get() = Registry.STATUS_EFFECT.getId(this)