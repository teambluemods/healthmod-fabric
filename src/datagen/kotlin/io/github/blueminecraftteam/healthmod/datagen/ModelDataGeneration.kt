/*
 * Copyright (c) 2020 Blue Minecraft Team.
 *
 * This file is part of HealthMod.
 *
 * HealthMod is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * HealthMod is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with HealthMod.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.blueminecraftteam.healthmod.datagen

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import io.github.blueminecraftteam.healthmod.compatibility.datagen.Model
import io.github.blueminecraftteam.healthmod.compatibility.datagen.State
import io.github.blueminecraftteam.healthmod.registries.BlockRegistries
import io.github.blueminecraftteam.healthmod.registries.ItemRegistries
import io.github.blueminecraftteam.healthmod.util.addBlockCubeModel
import io.github.blueminecraftteam.healthmod.util.addHorizontallyRotatingState
import io.github.blueminecraftteam.healthmod.util.extensions.id
import me.shedaniel.cloth.api.datagen.v1.ModelStateData
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

object ModelDataGeneration : Generator<ModelStateData> {
    private fun override(data: ModelStateData, block: Block, path: Path) {
        val normalized = path.toAbsolutePath().normalize()
        val reader = Files.newBufferedReader(normalized)
        val gson = GsonBuilder().setPrettyPrinting().create()
        val json = gson.fromJson(reader, JsonElement::class.java)

        data.addBlockModel(block, json)
    }

    override fun generate(data: ModelStateData) {
        val itemRegistriesClass = ItemRegistries::class
        val blockRegistriesClass = BlockRegistries::class

        itemRegistriesClass.memberProperties.onEach { it.isAccessible = true }
            .map { it.get(itemRegistriesClass.objectInstance!!) }
            .filterIsInstance<Item>()
            .forEach(data::addGeneratedItemModel)

        blockRegistriesClass.memberProperties.onEach { it.isAccessible = true }
            .map { it.get(blockRegistriesClass.objectInstance!!) }
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

                    Model.Type.OVERRIDING -> Unit /* do nothing, override manually */
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

        override(
            data,
            BlockRegistries.BLOOD_TEST_MACHINE,
            Paths.get("../src/generated/overrided/assets/healthmod/models/block/blood_test_machine.json")
        )
    }
}