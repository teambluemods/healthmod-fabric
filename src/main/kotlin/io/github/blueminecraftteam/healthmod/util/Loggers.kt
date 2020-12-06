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

package io.github.blueminecraftteam.healthmod.util

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

inline fun <reified T> logger(): Logger = LogManager.getLogger(T::class.java)

inline fun <reified T> warn(message: String) = logger<T>().warn(message)

inline fun <reified T> info(message: String) = logger<T>().info(message)

inline fun <reified T> debug(message: String) = logger<T>().debug(message)
