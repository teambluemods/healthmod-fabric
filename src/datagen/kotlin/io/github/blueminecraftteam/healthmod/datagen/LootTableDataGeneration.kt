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

package io.github.blueminecraftteam.healthmod.datagen

import io.github.blueminecraftteam.healthmod.compatibility.datagen.LootTable
import io.github.blueminecraftteam.healthmod.registries.BlockRegistries
import me.shedaniel.cloth.api.datagen.v1.LootTableData
import net.minecraft.block.Block
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

object LootTableDataGeneration : Generator<LootTableData> {
    override fun generate(data: LootTableData) {
        BlockRegistries::class.memberProperties.onEach { it.isAccessible = true }
            .map { it.get(BlockRegistries) }
            .filterIsInstance<Block>()
            .forEach { block ->
                val type = block::class.annotations
                    .filterIsInstance<LootTable>()
                    .map(LootTable::type)
                    .getOrNull(0)
                    ?: LootTable.Type.NORMAL

                when (type) {
                    LootTable.Type.NONE -> Unit /* do nothing */
                    LootTable.Type.NORMAL -> data.registerBlockDropSelf(block)
                    LootTable.Type.SILK_TOUCH_ONLY -> data.registerBlockDropSelfRequiresSilkTouch(block)
                    LootTable.Type.CUSTOM -> Unit /* manually override in class */
                }
            }
    }
}