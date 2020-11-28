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
package io.github.blueminecraftteam.healthmod.items

import io.github.blueminecraftteam.healthmod.HealthMod
import io.github.blueminecraftteam.healthmod.registries.StatusEffectRegistries
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
import java.util.concurrent.ThreadLocalRandom

class BandAidItem(settings: Settings) : Item(settings) {
    @Environment(EnvType.CLIENT)
    override fun appendTooltip(stack: ItemStack, world: World?, tooltip: MutableList<Text>, context: TooltipContext) {
        tooltip.add(TranslatableText("text." + HealthMod.MOD_ID + ".band_aid.1"))
        tooltip.add(TranslatableText("text." + HealthMod.MOD_ID + ".band_aid.2"))
        tooltip.add(TranslatableText("text." + HealthMod.MOD_ID + ".band_aid.3"))
    }

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (!world.isClient) {
            val itemStack = user.getStackInHand(hand)

            if (itemStack.damage == 1) {
                when (ThreadLocalRandom.current().nextInt(4)) {
                    0, 1, 2 -> user.addStatusEffect(StatusEffectInstance(StatusEffects.REGENERATION, 15 * 20, 0))
                    3 -> user.addStatusEffect(StatusEffectInstance(StatusEffectRegistries.WOUND_INFECTION, 15 * 20, 0))
                    else -> throw IllegalStateException("bruh")
                }
            } else {
                user.addStatusEffect(StatusEffectInstance(StatusEffects.REGENERATION, 15 * 20, 0))
            }

            itemStack.damage(1, user) { it.sendToolBreakStatus(hand) }
        }

        return super.use(world, user, hand)
    }
}