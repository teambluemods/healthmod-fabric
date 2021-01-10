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

package io.github.blueminecraftteam.healthmod.blocks.entities

import io.github.blueminecraftteam.healthmod.client.guis.screens.BandageBoxScreenHandler
import io.github.blueminecraftteam.healthmod.client.guis.screens.BloodTestMachineScreenHandler
import io.github.blueminecraftteam.healthmod.client.guis.screens.FirstAidKitScreenHandler
import io.github.blueminecraftteam.healthmod.inventories.SimpleBlockEntityInventory
import io.github.blueminecraftteam.healthmod.registries.BlockEntityTypeRegistries

class BandageBoxBlockEntity : SimpleBlockEntityInventory(
    type = BlockEntityTypeRegistries.BANDAGE_BOX,
    menuFactory = ::BandageBoxScreenHandler,
    size = 6
)

class FirstAidKitBlockEntity : SimpleBlockEntityInventory(
    type = BlockEntityTypeRegistries.FIRST_AID_KIT,
    menuFactory = ::FirstAidKitScreenHandler,
    size = 6
)

// TODO functionality
class BloodTestMachineBlockEntity : SimpleBlockEntityInventory(
    type = BlockEntityTypeRegistries.BLOOD_TEST_MACHINE,
    menuFactory = ::BloodTestMachineScreenHandler,
    size = 3
)