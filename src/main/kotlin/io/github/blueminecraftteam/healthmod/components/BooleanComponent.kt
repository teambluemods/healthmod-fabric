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

package io.github.blueminecraftteam.healthmod.components

import dev.onyxstudios.cca.api.v3.component.ComponentV3
import net.minecraft.nbt.CompoundTag

/**
 * Simple boolean component containing only one boolean.
 */
interface BooleanComponent : ComponentV3 {
    var value: Boolean

    override fun readFromNbt(tag: CompoundTag) {
        value = tag.getBoolean("value")
    }

    override fun writeToNbt(tag: CompoundTag) {
        tag.putBoolean("value", value)
    }
}