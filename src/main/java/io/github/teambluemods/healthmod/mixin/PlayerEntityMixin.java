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

package io.github.teambluemods.healthmod.mixin;

import io.github.teambluemods.healthmod.HealthMod;
import io.github.teambluemods.healthmod.registries.StatusEffectRegistries;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.ThreadLocalRandom;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    private PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);

        throw new UnsupportedOperationException("You cannot create instances of mixins!");
    }

    @Inject(method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", at = @At("TAIL"))
    private void applyInfectedOnDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (callbackInfoReturnable.getReturnValueZ() && amount > 2F) {
            int infectionChance = HealthMod.getConfig().getDamagedInfectionChance();

            if (this.hasStatusEffect(StatusEffectRegistries.HEALTHY)) {
                infectionChance = HealthMod.getConfig().getDamagedInfectionChanceWhenHealthy();
            }

            if (ThreadLocalRandom.current().nextInt(1, infectionChance + 1) == 1) {
                // 2 minutes effect
                this.addStatusEffect(new StatusEffectInstance(StatusEffectRegistries.WOUND_INFECTION, 2 * 60 * 20, 0));
            }
        }
    }
}
