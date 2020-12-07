package io.github.blueminecraftteam.healthmod.statuseffects

import io.github.blueminecraftteam.healthmod.config.config
import io.github.blueminecraftteam.healthmod.mixin.DamageSourceAccessorMixin
import io.github.blueminecraftteam.healthmod.registries.StatusEffectRegistries
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffectType
import kotlin.math.roundToInt

class BleedingStatusEffect(type: StatusEffectType, color: Int) : StatusEffect(type, color) {
    override fun applyUpdateEffect(entity: LivingEntity, amplifier: Int) {
        entity.damage(DAMAGE_SOURCE, 0.25f)
        if((1..config.woundInfectionChanceWhenBleeding + 1).random() != 1) {
            entity.applyStatusEffect(StatusEffectInstance(StatusEffectRegistries.WOUND_INFECTION))
            entity.removeStatusEffect(StatusEffectRegistries.BLEEDING)
        }
    }

    override fun canApplyUpdateEffect(duration: Int, amplifier: Int): Boolean {
        val k = (2.5 * 20).roundToInt() shr amplifier

        return if (k > 0) duration % k == 0 else true
    }

    companion object {
        private val DAMAGE_SOURCE = DamageSourceAccessorMixin.healthmod_newDamageSource("bleeding")
    }
}