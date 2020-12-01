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

@file:JvmName("HealthModConfigHolder")

package io.github.blueminecraftteam.healthmod.config

import io.github.blueminecraftteam.healthmod.HealthMod
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig
import me.sargunvohra.mcmods.autoconfig1u.ConfigData
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry

@Config(name = HealthMod.MOD_ID)
class HealthModConfig : ConfigData {
    @ConfigEntry.Category("wound_infection")
    @ConfigEntry.Gui.Tooltip
    var bandAidInfectionChance = 4

    @ConfigEntry.Category("wound_infection")
    @ConfigEntry.Gui.Tooltip
    var bandAidInfectionChanceWhenHealthy = 10

    @ConfigEntry.Category("wound_infection")
    @ConfigEntry.Gui.Tooltip
    var damagedInfectionChance = 10

    @ConfigEntry.Category("wound_infection")
    @ConfigEntry.Gui.Tooltip
    var damagedInfectionChanceWhenHealthy = 25

    @ConfigEntry.Category("other")
    @ConfigEntry.Gui.Tooltip
    var bacterialResistanceChance = 500
}

val config: HealthModConfig get() = AutoConfig.getConfigHolder(HealthModConfig::class.java).config