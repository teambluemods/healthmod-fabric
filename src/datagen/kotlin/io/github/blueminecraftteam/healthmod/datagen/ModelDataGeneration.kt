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

package io.github.blueminecraftteam.healthmod.datagen

import io.github.blueminecraftteam.healthmod.registries.BlockRegistries
import io.github.blueminecraftteam.healthmod.registries.ItemRegistries
import me.shedaniel.cloth.api.datagen.v1.ModelStateData
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.util.registry.Registry
import kotlin.reflect.full.memberProperties

object ModelDataGeneration {
    fun generate(data: ModelStateData) {
        val itemRegistriesClass = ItemRegistries::class
        val blockRegistriesClass = BlockRegistries::class

        itemRegistriesClass.memberProperties
            .map { it.get(itemRegistriesClass.objectInstance!!) }
            .filterIsInstance<Item>()
            .forEach { item -> data.addGeneratedItemModel(item) }

        blockRegistriesClass.memberProperties
            .map { it.get(blockRegistriesClass.objectInstance!!) }
            .filterIsInstance<Block>()
            .forEach { block ->
                data.addSingletonCubeAll(block)
                data.addSimpleItemModel(block.asItem(), Registry.BLOCK.getId(block))
            }
    }
}