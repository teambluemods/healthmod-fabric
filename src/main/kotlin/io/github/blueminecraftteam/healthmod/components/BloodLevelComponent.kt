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

package io.github.blueminecraftteam.healthmod.components

import dev.onyxstudios.cca.api.v3.component.ComponentV3
import net.minecraft.nbt.CompoundTag

public interface IntLevelComponent : ComponentV3 {
    var value: Int
    var max: Int
    var min: Int
}

class BloodLevelComponent : IntLevelComponent{
    override var value: Int = 20
    override var max: Int = 20
    override var min: Int = 0

    override fun readFromNbt(tag: CompoundTag) {
        value = tag.getInt("value")
    }

    override fun writeToNbt(tag: CompoundTag) {
        tag.putInt("value", value)
    }

    fun increase(tag: CompoundTag, amount: Int) {
        readFromNbt(tag)
        value += amount
        writeToNbt(tag)
    }
    fun decrease(tag: CompoundTag, amount: Int) {
        readFromNbt(tag)
        value -= amount
        writeToNbt(tag)
    }
}