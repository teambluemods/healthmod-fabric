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

package io.github.teambluemods.healthmod.items

import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ArmorItem
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Wearable
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

/**
 * A wearable item, for example a mask.
 *
 * The function/method [use]'s code is similar to how [ArmorItem] equipping works.
 *
 * *Note: for this to work correctly, you must use [FabricItemSettings.equipmentSlot].*
 */
class WearableItem(settings: Settings) : Item(settings), Wearable {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val stackInHand = user.getStackInHand(hand)
        val preferredEquipmentSlot = MobEntity.getPreferredEquipmentSlot(stackInHand)
        val equippedStack = user.getEquippedStack(preferredEquipmentSlot)

        return if (equippedStack.isEmpty) {
            user.equipStack(preferredEquipmentSlot, stackInHand.copy())
            stackInHand.count = 0

            TypedActionResult.success(stackInHand, world.isClient())
        } else {
            TypedActionResult.fail(stackInHand)
        }
    }
}