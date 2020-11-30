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
import io.github.blueminecraftteam.healthmod.mixin.StatusEffectInstanceAccessorMixin
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffectType
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.TranslatableText
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.roundToInt

class AntibioticsItem(settings: Settings) : Item(settings) {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (!world.isClient) {
            for ((statusEffect, statusEffectInstance) in Collections.synchronizedMap(user.activeStatusEffects)) {
                if (ThreadLocalRandom.current().nextInt(1, 500 + 1) != 1) {
                    if (statusEffect.type == StatusEffectType.HARMFUL && statusEffect != StatusEffects.POISON) {
                        user.removeStatusEffect(statusEffect)
                    }
                } else {
                    user.sendMessage(TranslatableText("text.${HealthMod.MOD_ID}.antibiotics.resistant_bacteria"), true)

                    if (statusEffect.type == StatusEffectType.HARMFUL) {
                        user.removeStatusEffect(statusEffect)

                        user.applyStatusEffect(statusEffectInstance.apply {
                            this.upgrade(
                                StatusEffectInstance(
                                    statusEffect,
                                    (duration * 1.5F).roundToInt(),
                                    amplifier + 1,
                                    (this as StatusEffectInstanceAccessorMixin).healthmod_isAmbient(),
                                    (this as StatusEffectInstanceAccessorMixin).healthmod_showsParticles(),
                                    (this as StatusEffectInstanceAccessorMixin).healthmod_showsIcon(),
                                    (this as StatusEffectInstanceAccessorMixin).healthmod_getHiddenEffect()
                                )
                            )
                        })
                    }
                }
            }
        }

        val stack = user.getStackInHand(hand)

        stack.decrement(1)

        return TypedActionResult.consume(user.getStackInHand(hand))
    }
}