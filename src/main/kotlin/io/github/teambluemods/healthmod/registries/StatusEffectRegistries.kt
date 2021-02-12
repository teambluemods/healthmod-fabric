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

import io.github.teambluemods.healthmod.mixin.StatusEffectAccessorMixin
import io.github.teambluemods.healthmod.effects.WoundInfectionStatusEffect
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectType
import net.minecraft.util.registry.Registry

object StatusEffectRegistries : ModRegistry<StatusEffect> {
    override val registry: Registry<StatusEffect> get() = Registry.STATUS_EFFECT

    @JvmField
    val WOUND_INFECTION = register(
        id = "wound_infection",
        toRegister = WoundInfectionStatusEffect(type = StatusEffectType.HARMFUL, color = 0x00FF00)
    )

    @JvmField
    val HEALTHY = register(
        id = "healthy",
        toRegister = StatusEffectAccessorMixin.create(StatusEffectType.BENEFICIAL, 0x67eb34)
    )
}