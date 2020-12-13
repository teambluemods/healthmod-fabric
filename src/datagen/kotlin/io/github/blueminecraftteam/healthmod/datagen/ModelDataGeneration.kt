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

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import io.github.blueminecraftteam.healthmod.HealthMod
import io.github.blueminecraftteam.healthmod.registries.BlockRegistries
import io.github.blueminecraftteam.healthmod.registries.ItemRegistries
import io.github.blueminecraftteam.healthmod.util.id
import me.shedaniel.cloth.api.datagen.v1.ModelStateData
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction
import net.minecraft.util.registry.Registry
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.reflect.full.memberProperties


object ModelDataGeneration {
    private fun horizontalRotatingBlock(data: ModelStateData, block: Block) {
        val id = block.id.path

        data.addState(
            block,
            JsonObject().apply {
                add("variants", JsonObject().apply {
                    for (direction in Direction.values().filter { it.axis.isHorizontal }) {
                        add("facing=${direction.name.toLowerCase()}", JsonObject().apply {
                            addProperty("model", "${HealthMod.MOD_ID}:block/$id")

                            direction.asRotation()
                                .takeUnless(0::equals)
                                ?.let { addProperty("y", it) }
                        })
                    }
                })
            }
        )

        data.addBlockModel(
            block,
            JsonObject().apply {
                addProperty("parent", "minecraft:block/cube")

                add("textures", JsonObject().apply {
                    addProperty("down", "${HealthMod.MOD_ID}:block/${id}_bottom")
                    addProperty("up", "${HealthMod.MOD_ID}:block/${id}_top")
                    addProperty("north", "${HealthMod.MOD_ID}:block/${id}_front")
                    addProperty("south", "${HealthMod.MOD_ID}:block/${id}_back")
                    addProperty("east", "${HealthMod.MOD_ID}:block/${id}_side")
                    addProperty("west", "${HealthMod.MOD_ID}:block/${id}_side")
                    addProperty("particle", "${HealthMod.MOD_ID}:block/${id}_bottom")
                })
            }
        )
    }

    private fun override(data: ModelStateData, block: Block, path: Path) {
        val normalized = path.toAbsolutePath().normalize()
        val reader = Files.newBufferedReader(normalized)
        val gson = Gson().newBuilder().setPrettyPrinting().create()
        val json = gson.fromJson(reader, JsonElement::class.java)

        data.addBlockModel(block, json)
    }

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

        horizontalRotatingBlock(data, BlockRegistries.FIRST_AID_KIT)
        horizontalRotatingBlock(data, BlockRegistries.BANDAGE_BOX)
        override(
            data,
            BlockRegistries.BLOOD_TEST_MACHINE,
            Paths.get("../src/generated/overrided/assets/healthmod/models/block/blood_test_machine.json")
        )
    }
}