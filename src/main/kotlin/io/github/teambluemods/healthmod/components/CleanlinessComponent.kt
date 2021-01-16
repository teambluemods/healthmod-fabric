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

package io.github.teambluemods.healthmod.components

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent
import io.github.teambluemods.healthmod.registries.ComponentRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.network.ServerPlayerEntity

/**
 * Component for how clean a player is. Automatically synced with the player for TODO client-side display.
 *
 * TODO: implement.
 */
class CleanlinessComponent(private val provider: Any) : IntComponent, AutoSyncedComponent {
    override var value = 0
        set(value) {
            field = value
            ComponentRegistries.CLEANLINESS.sync(provider)
        }

    override fun readFromNbt(tag: CompoundTag) {
        value = tag.getInt("value")
    }

    override fun writeToNbt(tag: CompoundTag) {
        tag.putInt("value", value)
    }

    override fun writeSyncPacket(buf: PacketByteBuf, recipient: ServerPlayerEntity) {
        buf.writeVarInt(value)
    }

    override fun applySyncPacket(buf: PacketByteBuf) {
        this.value = buf.readVarInt()
    }

    override fun shouldSyncWith(player: ServerPlayerEntity) = player == provider
}