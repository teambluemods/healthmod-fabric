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

import io.github.teambluemods.healthmod.HealthMod
import io.github.teambluemods.healthmod.config.config
import io.github.teambluemods.healthmod.mixin.StatusEffectInstanceAccessorMixin
import io.github.teambluemods.healthmod.util.LoggerDelegate
import io.github.teambluemods.healthmod.util.extensions.isServer
import io.github.teambluemods.healthmod.util.extensions.minusAssign
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
import org.apache.logging.log4j.Logger
import java.util.*
import kotlin.math.roundToInt

class AntibioticsItem(settings: Settings) : Item(settings) {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (user.activeStatusEffects.keys.none { it.type == StatusEffectType.HARMFUL }) {
            return TypedActionResult.pass(user.getStackInHand(hand))
        }

        if (world.isServer) {
            val stack = user.getStackInHand(hand)

            if ((1..config.bacterialResistanceChance).random() != 1) {
                LOGGER.debug("No resistant bacteria, clearing harmful status effects.")

                Collections.synchronizedMap(user.activeStatusEffects).keys
                    .filter { it.type == StatusEffectType.HARMFUL }
                    .filter { it != StatusEffects.POISON }
                    .forEach(user::removeStatusEffect)
            } else {
                LOGGER.debug("Resistant bacteria, amplifying harmful status effects.")

                user.sendMessage(
                    TranslatableText("text.${HealthMod.MOD_ID}.antibiotics.resistant_bacteria"),
                    true
                )

                Collections.synchronizedMap(user.activeStatusEffects)
                    .filter { (statusEffect, _) -> statusEffect.type == StatusEffectType.HARMFUL }
                    .filter { (statusEffect, _) -> statusEffect != StatusEffects.POISON }
                    .forEach { (statusEffect, statusEffectInstance) ->
                        statusEffectInstance.apply {
                            this.upgrade(
                                StatusEffectInstance(
                                    statusEffect,
                                    (duration * 1.5F).roundToInt(),
                                    amplifier + 1,
                                    (this as StatusEffectInstanceAccessorMixin).healthmod_isAmbient(),
                                    (this as StatusEffectInstanceAccessorMixin).healthmod_shouldShowParticles(),
                                    (this as StatusEffectInstanceAccessorMixin).healthmod_shouldShowIcon(),
                                    (this as StatusEffectInstanceAccessorMixin).healthmod_getHiddenEffect()
                                )
                            )
                        }
                    }
            }

            stack -= 1
        }

        return TypedActionResult.consume(user.getStackInHand(hand))
    }

    companion object {
        private val LOGGER: Logger by LoggerDelegate()
    }
}