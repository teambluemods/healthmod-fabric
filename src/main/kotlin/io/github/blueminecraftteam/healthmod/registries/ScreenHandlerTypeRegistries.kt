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

package io.github.blueminecraftteam.healthmod.registries

import io.github.blueminecraftteam.healthmod.HealthMod
import io.github.blueminecraftteam.healthmod.client.gui.screen.BandageBoxScreenHandler
import io.github.blueminecraftteam.healthmod.client.gui.screen.BloodTestMachineScreenHandler
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.util.registry.Registry

object ScreenHandlerTypeRegistries : ModRegistry<ScreenHandlerType<*>> {
    override val registry: Registry<ScreenHandlerType<*>> get() = Registry.SCREEN_HANDLER


    val BANDAGE_BOX: ScreenHandlerType<BandageBoxScreenHandler> =
        ScreenHandlerRegistry.registerSimple(HealthMod.id("bandage_box"), ::BandageBoxScreenHandler)
  
    val BLOOD_TEST_MACHINE: ScreenHandlerType<BloodTestMachineScreenHandler> =
        ScreenHandlerRegistry.registerSimple(HealthMod.id("blood_test_machine"), ::BloodTestMachineScreenHandler)
}