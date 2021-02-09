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

package io.github.teambluemods.healthmod.effects

import io.github.teambluemods.healthmod.mixin.DamageSourceAccessorMixin
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectType

class WoundInfectionStatusEffect(type: StatusEffectType, color: Int) : StatusEffect(type, color) {
    override fun applyUpdateEffect(entity: LivingEntity, amplifier: Int) {
        entity.damage(DAMAGE_SOURCE, 0.5F)
    }

    override fun canApplyUpdateEffect(duration: Int, amplifier: Int): Boolean {
        val k = 5 * 20 shr amplifier

        return if (k > 0) duration % k == 0 else true
    }

    companion object {
        private val DAMAGE_SOURCE = DamageSourceAccessorMixin.create("wound_infection")
    }
}