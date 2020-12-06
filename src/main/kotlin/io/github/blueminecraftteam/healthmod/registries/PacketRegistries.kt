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

package io.github.blueminecraftteam.healthmod.registries

import io.github.blueminecraftteam.healthmod.HealthMod
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry
import net.minecraft.entity.LivingEntity
import net.minecraft.server.world.ServerWorld

object PacketRegistries {
    val FIRST_AID_KIT_USED = HealthMod.id("first_aid_kit_used")

    fun init() {
        ServerSidePacketRegistry.INSTANCE.register(FIRST_AID_KIT_USED) { context, byteBuf ->
            val world = context.player.world

            if (!world.isClient && world is ServerWorld) {
                val entity = world.getEntity(byteBuf.readUuid())

                if (entity is LivingEntity && entity.health < entity.maxHealth) {
                    entity.heal(entity.health * 1.8F)
                }
            }
        }
    }
}