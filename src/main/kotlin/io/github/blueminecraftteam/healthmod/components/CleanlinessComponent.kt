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

package io.github.blueminecraftteam.healthmod.components

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent
import io.github.blueminecraftteam.healthmod.registries.ComponentRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.network.ServerPlayerEntity

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