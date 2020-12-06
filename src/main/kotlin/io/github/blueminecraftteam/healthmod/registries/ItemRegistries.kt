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
import io.github.blueminecraftteam.healthmod.items.AntibioticsItem
import io.github.blueminecraftteam.healthmod.items.BandageItem
import io.github.blueminecraftteam.healthmod.items.BloodVialItem
import io.github.blueminecraftteam.healthmod.items.FirstAidKitItem
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.item.FoodComponent
import net.minecraft.item.Item
import net.minecraft.util.Rarity
import net.minecraft.util.registry.Registry

@Suppress("UNUSED")
object ItemRegistries : ModRegistry<Item> {
    override val registry: Registry<Item> get() = Registry.ITEM

    val BANDAGE = register(
        id = "bandage",
        toRegister = BandageItem(
            Item.Settings()
                .group(HealthMod.ITEM_GROUP)
                .maxCount(16)
                .maxDamage(1)
                .rarity(Rarity.UNCOMMON)
        )
    )

    val ANTIBIOTICS = register(
        id = "antibiotics",
        toRegister = AntibioticsItem(
            Item.Settings()
                .group(HealthMod.ITEM_GROUP)
                .maxCount(16)
        )
    )

    val BROCCOLI = register(
        id = "broccoli",
        toRegister = Item(
            Item.Settings()
                .group(HealthMod.ITEM_GROUP)
                .food(
                    FoodComponent.Builder()
                        .hunger(3)
                        .saturationModifier(2F)
                        .statusEffect(StatusEffectInstance(StatusEffectRegistries.HEALTHY, 60 * 20), 1F)
                        .build()
                )
        )
    )

    val FIRST_AID_KIT = register(
        id = "first_aid_kit",
        toRegister = FirstAidKitItem(
            Item.Settings()
                .group(HealthMod.ITEM_GROUP)
                .maxDamage(5)
                .rarity(Rarity.UNCOMMON)
        )
    )

    val BLOOD_VIAL = register(
        id = "blood_vial",
        toRegister = BloodVialItem(
            Item.Settings()
                .group(HealthMod.ITEM_GROUP)
                .maxDamage(2000000)
                .rarity(Rarity.UNCOMMON)
        )
    )
}