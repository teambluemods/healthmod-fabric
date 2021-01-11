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

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import io.github.blueminecraftteam.healthmod.HealthMod
import io.github.blueminecraftteam.healthmod.compatibility.datagen.Model
import io.github.blueminecraftteam.healthmod.compatibility.datagen.State
import io.github.blueminecraftteam.healthmod.registries.BlockRegistries
import io.github.blueminecraftteam.healthmod.registries.ItemRegistries
import io.github.blueminecraftteam.healthmod.util.extensions.id
import io.github.xf8b.utils.gson.json
import me.shedaniel.cloth.api.datagen.v1.ModelStateData
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction
import java.io.Reader
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

object ModelDataGeneration : Generator<ModelStateData> {
    override fun generate(data: ModelStateData) {
        ItemRegistries::class.memberProperties.onEach { it.isAccessible = true }
            .map { it.get(ItemRegistries) }
            .filterIsInstance<Item>()
            .forEach(data::addGeneratedItemModel)

        BlockRegistries::class.memberProperties.onEach { it.isAccessible = true }
            .map { it.get(BlockRegistries) }
            .filterIsInstance<Block>()
            .forEach { block ->
                val modelType = block::class.annotations
                    .filterIsInstance<Model>()
                    .map(Model::type)
                    .getOrNull(0)
                    ?: Model.Type.CUBE_ALL
                val stateType = block::class.annotations
                    .filterIsInstance<State>()
                    .map(State::type)
                    .getOrNull(0)
                    ?: State.Type.SIMPLE

                when (modelType) {
                    Model.Type.CUBE -> data.addBlockCubeModel(block)
                    Model.Type.CUBE_ALL -> data.addSimpleBlockModel(block, Identifier("minecraft", "cube_all"))
                    Model.Type.CUSTOM -> Unit /* do nothing */
                }

                data.addSimpleItemModel(
                    block.asItem(),
                    block.id.run { Identifier(this.namespace, "block/${this.path}") }
                )

                if (stateType == State.Type.SIMPLE) {
                    data.addSimpleState(
                        block,
                        block.id.run { Identifier(this.namespace, "block/${this.path}") }
                    )
                } else {
                    data.addHorizontallyRotatingState(block)
                }
            }

        data.addBlockModelFromFile(
            block = BlockRegistries.BLOOD_TEST_MACHINE,
            path = Paths.get("../src/generated/overrided/assets/healthmod/models/block/blood_test_machine.json")
        )
    }
}

// extensions to make life easier

private fun ModelStateData.addBlockModelFromFile(block: Block, path: Path) {
    val normalized = path.toAbsolutePath().normalize()
    val reader = Files.newBufferedReader(normalized)
    val gson = GsonBuilder().setPrettyPrinting().create()
    val json = gson.fromJson<JsonElement>(reader)

    addBlockModel(block, json)
}

private inline fun <reified T> Gson.fromJson(reader: Reader) = fromJson(reader, T::class.java)

private fun ModelStateData.addBlockCubeModel(block: Block) {
    val blockId = block.id.path

    addBlockModel(
        block,
        json {
            property("parent", Identifier("minecraft", "block/cube").toString())

            `object`("textures") {
                property("down", HealthMod.id("block/${blockId}_bottom").toString())
                property("up", HealthMod.id("block/${blockId}_top").toString())
                property("north", HealthMod.id("block/${blockId}_front").toString())
                property("south", HealthMod.id("block/${blockId}_back").toString())
                property("east", HealthMod.id("block/${blockId}_side").toString())
                property("west", HealthMod.id("block/${blockId}_side").toString())
                property("particle", HealthMod.id("block/${blockId}_bottom").toString())
            }
        }.toJsonElement()
    )
}

private fun ModelStateData.addHorizontallyRotatingState(block: Block) {
    addState(
        block,
        json {
            `object`("variants") {
                val horizontalDirections = Direction.values().filter { it.axis.isHorizontal }

                for (direction in horizontalDirections) {
                    `object`("facing=${direction.name.toLowerCase(Locale.ROOT)}") {
                        property("model", HealthMod.id("block/${block.id.path}").toString())

                        direction.toYRotation().takeUnless(0::equals)?.let { property("y", it) }
                    }
                }
            }
        }.toJsonElement()
    )
}

private fun Direction.toYRotation() = when (this) {
    Direction.NORTH -> 0
    Direction.EAST -> 90
    Direction.SOUTH -> 180
    Direction.WEST -> 270
    else -> 0
}