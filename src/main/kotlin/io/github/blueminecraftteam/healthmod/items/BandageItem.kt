/*
 * Copyright (c) 2020 Blue Minecraft Team.
 *
 * This file is part of HealthMod.
 *
 * HealthMod is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * HealthMod is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with HealthMod.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.blueminecraftteam.healthmod.items

import io.github.blueminecraftteam.healthmod.HealthMod
import io.github.blueminecraftteam.healthmod.config.config
import io.github.blueminecraftteam.healthmod.registries.StatusEffectRegistries
import io.github.blueminecraftteam.healthmod.util.extensions.isServer
import io.github.blueminecraftteam.healthmod.util.extensions.minusAssign
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class BandageItem(settings: Settings) : Item(settings) {
    @Environment(EnvType.CLIENT)
    override fun appendTooltip(stack: ItemStack, world: World?, tooltip: MutableList<Text>, context: TooltipContext) {
        tooltip.add(TranslatableText("text.${HealthMod.MOD_ID}.bandage.1"))
        tooltip.add(TranslatableText("text.${HealthMod.MOD_ID}.bandage.2"))
        tooltip.add(TranslatableText("text.${HealthMod.MOD_ID}.bandage.3"))
    }

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (world.isServer) {
            val stackInHand = user.getStackInHand(hand)

            if (user.health < user.maxHealth) {
                val chance = if (user.hasStatusEffect(StatusEffectRegistries.HEALTHY)) {
                    config.bandageInfectionChanceWhenHealthy
                } else {
                    config.bandageInfectionChance
                }

                // 1 in 4 chance (or 1 in 10 if healthy) to have it not apply correct
                if ((1..chance).random() == 1) {
                    // 2 minutes effect
                    user.addStatusEffect(StatusEffectInstance(StatusEffectRegistries.WOUND_INFECTION, 2 * 60 * 20))
                    user.sendMessage(TranslatableText("text.${HealthMod.MOD_ID}.bandage.failed_apply"), true)
                } else {
                    user.addStatusEffect(StatusEffectInstance(StatusEffects.REGENERATION, 15 * 20))
                }

                stackInHand -= 1
            }
        }

        return super.use(world, user, hand)
    }
}