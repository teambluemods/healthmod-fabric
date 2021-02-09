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
import io.github.teambluemods.healthmod.items.*
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.item.FoodComponent
import net.minecraft.item.Item
import net.minecraft.util.Rarity
import net.minecraft.util.registry.Registry

@Suppress("UNUSED")
object ItemRegistries : ModRegistry<Item> {
    override val registry: Registry<Item> get() = Registry.ITEM
    val DEFAULT_SETTINGS: Item.Settings get() = Item.Settings().group(HealthMod.ITEM_GROUP)

    val BANDAGE = register(
        id = "bandage",
        toRegister = BandageItem(DEFAULT_SETTINGS.maxCount(8).rarity(Rarity.UNCOMMON))
    )

    val ANTIBIOTICS = register(
        id = "antibiotics",
        toRegister = AntibioticsItem(DEFAULT_SETTINGS.maxCount(16))
    )

    val BROCCOLI = register(
        id = "broccoli",
        toRegister = Item(
            DEFAULT_SETTINGS.food(
                FoodComponent.Builder()
                    .hunger(3)
                    .saturationModifier(2F)
                    .statusEffect(StatusEffectInstance(StatusEffectRegistries.HEALTHY, 60 * 20), 1F)
                    .build()
            )
        )
    )

    val BLOOD_VIAL = register(
        id = "blood_vial",
        toRegister = Item(DEFAULT_SETTINGS)
    )

    val ISOPROPYL_ALCOHOL = register(
        id = "isopropyl_alcohol",
        toRegister = IsopropylAlcoholItem(DEFAULT_SETTINGS.maxCount(1))
    )

    val SOAP = register(
        id = "soap",
        toRegister = SoapItem(DEFAULT_SETTINGS.maxDamage(30))
    )

    val SYRINGE = register(
        id = "syringe",
        toRegister = SyringeItem(DEFAULT_SETTINGS)
    )
}
