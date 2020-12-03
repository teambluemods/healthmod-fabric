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

package io.github.blueminecraftteam.healthmod.mixin;

import io.github.blueminecraftteam.healthmod.config.HealthModConfigHolder;
import io.github.blueminecraftteam.healthmod.registries.StatusEffectRegistries;
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

        throw new UnsupportedOperationException("Cannot create instance of mixin!");
    }

    @Inject(method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", at = @At("TAIL"))
    private void applyInfectedOnDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ() && amount > 2F) {
            int infectionChance = HealthModConfigHolder.getConfig().getDamagedInfectionChance();

            if (this.hasStatusEffect(StatusEffectRegistries.INSTANCE.getHEALTHY())) {
                infectionChance = HealthModConfigHolder.getConfig().getDamagedInfectionChanceWhenHealthy();
            }

            if (ThreadLocalRandom.current().nextInt(1, infectionChance + 1) == 1) {
                // cursed getter name
                this.addStatusEffect(new StatusEffectInstance(StatusEffectRegistries.getWOUND_INFECTION(), 15 * 20, 0));
            }
        }
    }
}
