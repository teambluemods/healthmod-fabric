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

import com.google.gson.JsonObject
import io.github.blueminecraftteam.healthmod.registries.BlockRegistries
import io.github.blueminecraftteam.healthmod.registries.ItemRegistries
import me.shedaniel.cloth.api.datagen.v1.ModelStateData
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction
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
                data.addSimpleItemModel(
                    block.asItem(),
                    Registry.BLOCK
                        .getId(block)
                        .run { Identifier(this.namespace, "block/${this.path}") }
                )
            }

        data.addState(
            BlockRegistries.BANDAGE_BOX,
            JsonObject().apply {
                add("variants", JsonObject().apply {
                    for (direction in Direction.values().filter { it.axis.isHorizontal }) {
                        add("facing=${direction.name.toLowerCase()}", JsonObject().apply {
                            addProperty("model", "healthmod:block/bandage_box")

                            when (direction) {
                                Direction.NORTH -> {
                                }
                                Direction.SOUTH -> addProperty("y", 180)
                                Direction.EAST -> addProperty("y", 90)
                                Direction.WEST -> addProperty("y", 270)
                                else -> error("what")
                            }
                        })
                    }
                })
            }
        )

        data.addBlockModel(
            BlockRegistries.BANDAGE_BOX,
            JsonObject().apply {
                addProperty("parent", "minecraft:block/cube")
                add("textures", JsonObject().apply {
                    addProperty("down", "healthmod:block/bandage_box_bottom")
                    addProperty("up", "healthmod:block/bandage_box_top")
                    addProperty("north", "healthmod:block/bandage_box_front")
                    addProperty("south", "healthmod:block/bandage_box_back")
                    addProperty("east", "healthmod:block/bandage_box_side")
                    addProperty("west", "healthmod:block/bandage_box_side")
                    addProperty("particle", "minecraft:block/acacia_planks")
                })
            }
        )
    }
}