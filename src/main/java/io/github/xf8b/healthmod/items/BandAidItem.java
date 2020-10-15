/*
 * Copyright (c) 2020 xf8b.
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

package io.github.xf8b.healthmod.items;

import io.github.xf8b.healthmod.registries.StatusEffectRegistries;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.concurrent.ThreadLocalRandom;

public class BandAidItem extends Item {
    public BandAidItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            ItemStack itemStack = user.getStackInHand(hand);
            if (itemStack.getDamage() == 1) {
                switch (ThreadLocalRandom.current().nextInt(4)) {
                    case 0:
                    case 1:
                    case 2:
                        user.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 15 * 20, 0));
                        break;
                    case 3:
                        user.addStatusEffect(new StatusEffectInstance(StatusEffectRegistries.WOUND_INFECTION, 15 * 20, 0));
                        break;
                    default:
                        throw new IllegalStateException("bruh");
                        break;
                }
            } else {
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 15 * 20, 0));
            }
            itemStack.damage(1, user, (playerEntity) -> playerEntity.sendToolBreakStatus(hand));
        }
        return super.use(world, user, hand);
    }
}
