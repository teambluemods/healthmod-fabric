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

package io.github.blueminecraftteam.healthmod.datagen.util.extensions

import io.github.blueminecraftteam.healthmod.HealthMod
import io.github.blueminecraftteam.healthmod.util.extensions.id
import io.github.blueminecraftteam.healthmod.util.extensions.toYRotation
import io.github.xf8b.utils.gson.json
import me.shedaniel.cloth.api.datagen.v1.ModelStateData
import net.minecraft.block.Block
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction
import java.util.*

fun ModelStateData.addBlockCubeModel(block: Block) {
    val blockId = block.id.path

    this.addBlockModel(
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

fun ModelStateData.addHorizontallyRotatingState(block: Block) {
    this.addState(
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