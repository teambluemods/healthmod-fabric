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
import io.github.teambluemods.healthmod.professions.trades.ProfessionTrades
import net.fabricmc.fabric.api.`object`.builder.v1.villager.VillagerProfessionBuilder
import net.fabricmc.fabric.api.`object`.builder.v1.world.poi.PointOfInterestHelper
import net.minecraft.sound.SoundEvents
import net.minecraft.util.registry.Registry
import net.minecraft.village.TradeOffers
import net.minecraft.village.VillagerProfession
import net.minecraft.world.poi.PointOfInterestType

@Suppress("MemberVisibilityCanBePrivate") // datagen wont work if i do this
object VillagerProfessionRegistries : ModRegistry<VillagerProfession> {
    override val registry: Registry<VillagerProfession> get() = Registry.VILLAGER_PROFESSION

    val DOCTOR_POINT_OF_INTEREST: PointOfInterestType = PointOfInterestHelper.register(
        HealthMod.id("doctor_point_of_interest"),
        /* ticket count of */ 1,
        /* search distance of */ 1,
        BlockRegistries.FIRST_AID_KIT,
        BlockRegistries.BANDAGE_BOX
    )

    val DOCTOR: VillagerProfession = register(
        id = "doctor",
        toRegister = VillagerProfessionBuilder.create()
            .id(HealthMod.id("doctor"))
            .workstation(DOCTOR_POINT_OF_INTEREST)
            .workSound(SoundEvents.BLOCK_BREWING_STAND_BREW)
            .build()
    )

    override fun init() {
        super.init()

        TradeOffers.PROFESSION_TO_LEVELED_TRADE[DOCTOR] = ProfessionTrades.DOCTOR
    }
}