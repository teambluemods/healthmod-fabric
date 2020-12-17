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

package io.github.blueminecraftteam.healthmod.entity

import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.passive.MerchantEntity
import net.minecraft.entity.passive.PassiveEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Items
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.village.TradeOffer
import net.minecraft.world.World


class DoctorEntity(entityType: EntityType<out MerchantEntity>?, world: World?) : MerchantEntity(entityType, world) {

    override fun tick() {
        super.tick()
        if (this.headRollingTimeLeft > 0) {
            this.headRollingTimeLeft -= 1
        }
    }

    override fun createChild(world: ServerWorld?, entity: PassiveEntity?): PassiveEntity? {
        return null
    }

    override fun afterUsing(offer: TradeOffer) {
        offer.use()
    }

    override fun interactMob(player: PlayerEntity, hand: Hand): ActionResult {
        val heldItem = player.getStackInHand(hand)
        if (heldItem.item == Items.NAME_TAG) {
            heldItem.useOnEntity(player, this, hand)
            return ActionResult.SUCCESS
        } else if (this.isAlive && !this.hasCustomer() && !this.isBaby) {
            if (this.getOffers().isEmpty()) {
                return super.interactMob(player, hand)
            } else if (!this.world.isClient && (this.attacker == null || this.attacker != player)) {
                this.currentCustomer = player
                this.sendOffers(player, this.displayName, 1)
            }
            return ActionResult.SUCCESS
        }
        return super.interactMob(player, hand)
    }

    override fun initGoals() {
        this.goalSelector.add(0, SwimGoal(this))
        this.goalSelector.add(6, LookAtCustomerGoal(this))
        this.goalSelector.add(7, WanderAroundFarGoal(this, 0.4))
        this.goalSelector.add(8, GoToWalkTargetGoal(this, 0.4))
        this.goalSelector.add(9, StopAndLookAtEntityGoal(this, PlayerEntity::class.java, 4.0f, 4.0f))
        this.goalSelector.add(10, LookAtEntityGoal(this, PlayerEntity::class.java, 8.0f))
    }

    override fun fillRecipes() {
        val offers = this.getOffers()
        this.fillRecipesFromPool(offers, Trades.DOCTOR.get(0), 2)
    }

    override fun isLeveledMerchant(): Boolean {
        return false
    }

    fun registerAttributes(): DefaultAttributeContainer.Builder {
        return LivingEntity.createLivingAttributes()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0)
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5)
            .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 20.0)
    }

}