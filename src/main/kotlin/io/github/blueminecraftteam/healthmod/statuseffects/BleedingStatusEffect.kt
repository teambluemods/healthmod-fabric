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

package io.github.blueminecraftteam.healthmod.statuseffects

import io.github.blueminecraftteam.healthmod.config.config
import io.github.blueminecraftteam.healthmod.mixin.DamageSourceAccessorMixin
import io.github.blueminecraftteam.healthmod.registries.ComponentRegistries
import io.github.blueminecraftteam.healthmod.registries.StatusEffectRegistries
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffectType
import net.minecraft.entity.player.PlayerEntity

class BleedingStatusEffect(type: StatusEffectType, color: Int) : StatusEffect(type, color) {
    override fun applyUpdateEffect(entity: LivingEntity, amplifier: Int) {
        entity.damage(DAMAGE_SOURCE, 0.25f)

        if (entity is PlayerEntity) {
            ComponentRegistries.BLOOD_LEVEL.get(entity).value -= 1

            if ((1..config.woundInfectionChanceWhenBleeding + 1).random() == 1
                && !ComponentRegistries.SANITIZED_WOUND.get(entity).value
            ) {
                entity.applyStatusEffect(StatusEffectInstance(StatusEffectRegistries.WOUND_INFECTION, 60 * 20))
                entity.removeStatusEffect(StatusEffectRegistries.BLEEDING)
            }
        }
    }

    override fun canApplyUpdateEffect(duration: Int, amplifier: Int): Boolean {
        val k = 50 shr amplifier

        return if (k > 0) duration % k == 0 else true
    }

    companion object {
        private val DAMAGE_SOURCE = DamageSourceAccessorMixin.healthmod_newDamageSource("bleeding")
    }
}