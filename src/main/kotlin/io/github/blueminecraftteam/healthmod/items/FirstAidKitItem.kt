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

package io.github.blueminecraftteam.healthmod.items

import io.github.blueminecraftteam.healthmod.registries.PacketRegistries
import io.github.blueminecraftteam.healthmod.util.throwIfNull
import io.netty.buffer.Unpooled
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.world.World


class FirstAidKitItem(settings: Settings) : Item(settings) {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (world.isClient) {
            val client = MinecraftClient.getInstance()
            val hit = client.crosshairTarget.throwIfNull("Expected non-null crosshair target")

            if (hit.type == HitResult.Type.ENTITY) {
                val entityHit = hit as EntityHitResult
                val entity = entityHit.entity

                val passedData = PacketByteBuf(Unpooled.buffer())
                passedData.writeUuid(entity.uuid)

                ClientSidePacketRegistry.INSTANCE.sendToServer(PacketRegistries.FIRST_AID_KIT_USED, passedData)
            } else {
                val passedData = PacketByteBuf(Unpooled.buffer())
                passedData.writeUuid(user.uuid)

                ClientSidePacketRegistry.INSTANCE.sendToServer(PacketRegistries.FIRST_AID_KIT_USED, passedData)
            }
        }

        return TypedActionResult.consume(user.getStackInHand(hand))
    }
}