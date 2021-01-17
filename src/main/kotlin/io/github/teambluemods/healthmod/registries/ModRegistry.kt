/*
 * Copyright (c) 2020, 2021 Team Blue.
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

package io.github.teambluemods.healthmod.registries

import io.github.teambluemods.healthmod.HealthMod
import io.github.teambluemods.healthmod.util.LoggerDelegate
import net.minecraft.util.registry.Registry

interface ModRegistry<T> {
    /**
     * The [Registry] to register any [T] to.
     */
    val registry: Registry<T>

    /**
     * Force loads this class.
     */
    fun init() {
        LOGGER.debug("Initialized registry ${this::class.java.simpleName}!")
    }

    /**
     * Registers [T] to the [registry] with the specified [id]. Will have a namespace of `healthmod`.
     */
    fun register(id: String, toRegister: T): T = Registry.register(registry, HealthMod.id(id), toRegister)

    companion object {
        private val LOGGER by LoggerDelegate()
    }
}