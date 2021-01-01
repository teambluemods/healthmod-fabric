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

package io.github.blueminecraftteam.healthmod.util

import com.google.gson.JsonObject
import io.github.blueminecraftteam.healthmod.HealthMod
import io.github.blueminecraftteam.healthmod.util.extensions.toYRotation
import me.shedaniel.cloth.api.datagen.v1.ModelStateData
import net.minecraft.block.Block
import net.minecraft.util.math.Direction
import java.util.*

fun ModelStateData.addBlockCubeModel(block: Block) {
    val id = block.id.path

    this.addBlockModel(
        block,
        JsonObject().apply {
            addProperty("parent", "minecraft:block/cube")

            add("textures", JsonObject().apply {
                addProperty("down", HealthMod.id("block/${id}_bottom").toString())
                addProperty("up", HealthMod.id("block/${id}_top").toString())
                addProperty("north", HealthMod.id("block/${id}_front").toString())
                addProperty("south", HealthMod.id("block/${id}_back").toString())
                addProperty("east", HealthMod.id("block/${id}_side").toString())
                addProperty("west", HealthMod.id("block/${id}_side").toString())
                addProperty("particle", HealthMod.id("block/${id}_bottom").toString())
            })
        }
    )
}

fun ModelStateData.addHorizontallyRotatingState(block: Block) {
    val id = block.id.path

    this.addState(
        block,
        JsonObject().apply {
            add("variants", JsonObject().apply {
                for (direction in Direction.values().filter { it.axis.isHorizontal }) {
                    add("facing=${direction.name.toLowerCase(Locale.ROOT)}", JsonObject().apply {
                        addProperty("model", HealthMod.id("block/$id").toString())

                        direction.toYRotation().takeUnless(0::equals)?.let {
                            addProperty("y", it)
                        }
                    })
                }
            })
        }
    )
}