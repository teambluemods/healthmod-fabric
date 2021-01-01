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

package io.github.blueminecraftteam.healthmod.registries

import io.github.blueminecraftteam.healthmod.mixin.StatusEffectAccessorMixin
import io.github.blueminecraftteam.healthmod.statuseffects.WoundInfectionStatusEffect
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectType
import net.minecraft.util.registry.Registry

@Suppress("UNUSED")
object StatusEffectRegistries : ModRegistry<StatusEffect> {
    override val registry: Registry<StatusEffect> get() = Registry.STATUS_EFFECT

    @JvmField
    val WOUND_INFECTION = register(
        id = "wound_infection",
        toRegister = WoundInfectionStatusEffect(StatusEffectType.HARMFUL, 0x00FF00)
    )

    @JvmField
    val HEALTHY = register(
        id = "healthy",
        toRegister = StatusEffectAccessorMixin.healthmod_create(StatusEffectType.BENEFICIAL, 0x67eb34)
    )
}