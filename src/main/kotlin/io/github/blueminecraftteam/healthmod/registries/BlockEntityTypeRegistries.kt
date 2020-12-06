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

import io.github.blueminecraftteam.healthmod.blocks.entities.BandageBoxBlockEntity
import io.github.blueminecraftteam.healthmod.blocks.entities.BloodTestMachineBlockEntity
import io.github.blueminecraftteam.healthmod.blocks.entities.FirstAidKitBlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.registry.Registry

object BlockEntityTypeRegistries : ModRegistry<BlockEntityType<*>> {
    override val registry: Registry<BlockEntityType<*>> get() = Registry.BLOCK_ENTITY_TYPE

    val BANDAGE_BOX = register(
        id = "bandage_box",
        toRegister = BlockEntityType.Builder
            .create(::BandageBoxBlockEntity, BlockRegistries.BANDAGE_BOX)
            .build(null)
    )

    val BLOOD_TEST_MACHINE = register(
        id = "blood_test_machine",
        toRegister = BlockEntityType.Builder
            .create(::BloodTestMachineBlockEntity, BlockRegistries.BLOOD_TEST_MACHINE)
            .build(null)
    )

    val FIRST_AID_KIT = register(
        id = "first_aid_kit",
        toRegister = BlockEntityType.Builder
            .create(::FirstAidKitBlockEntity, BlockRegistries.FIRST_AID_KIT)
            .build(null)
    )
}