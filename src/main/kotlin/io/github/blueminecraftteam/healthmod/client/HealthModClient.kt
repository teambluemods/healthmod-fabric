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

package io.github.blueminecraftteam.healthmod.client

import io.github.blueminecraftteam.healthmod.client.gui.screen.BandageBoxScreen
import io.github.blueminecraftteam.healthmod.registries.ScreenHandlerTypeRegistries
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry

@Environment(EnvType.CLIENT)
object HealthModClient : ClientModInitializer {
    override fun onInitializeClient() {
        ScreenRegistry.register(ScreenHandlerTypeRegistries.BANDAGE_BOX, ::BandageBoxScreen)
    }
}