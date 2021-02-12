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
import io.github.teambluemods.healthmod.client.guis.screens.BandageBoxScreenHandler
import io.github.teambluemods.healthmod.client.guis.screens.BloodTestMachineScreenHandler
import io.github.teambluemods.healthmod.client.guis.screens.FirstAidKitScreenHandler
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.util.registry.Registry

object ScreenHandlerTypeRegistries : ModRegistry<ScreenHandlerType<*>> {
    override val registry: Registry<ScreenHandlerType<*>> = Registry.SCREEN_HANDLER

    val BANDAGE_BOX: ScreenHandlerType<BandageBoxScreenHandler> = ScreenHandlerRegistry.registerSimple(
        HealthMod.id("bandage_box"),
        ::BandageBoxScreenHandler
    )

    val FIRST_AID_KIT: ScreenHandlerType<FirstAidKitScreenHandler> = ScreenHandlerRegistry.registerSimple(
        HealthMod.id("first_aid_kit"),
        ::FirstAidKitScreenHandler
    )

    val BLOOD_TEST_MACHINE: ScreenHandlerType<BloodTestMachineScreenHandler> = ScreenHandlerRegistry.registerSimple(
        HealthMod.id("blood_test_machine"),
        ::BloodTestMachineScreenHandler
    )
}