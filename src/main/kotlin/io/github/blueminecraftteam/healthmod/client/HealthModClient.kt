/*
 * Copyright (c) 2020, 2021 Blue Minecraft Team.
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

package io.github.blueminecraftteam.healthmod.client

import io.github.blueminecraftteam.healthmod.HealthMod
import io.github.blueminecraftteam.healthmod.client.guis.screens.SimpleInventoryScreen
import io.github.blueminecraftteam.healthmod.registries.ScreenHandlerTypeRegistries
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry
import net.minecraft.util.Identifier

@Environment(EnvType.CLIENT)
object HealthModClient : ClientModInitializer {
    override fun onInitializeClient() {
        ScreenRegistry.register(ScreenHandlerTypeRegistries.BANDAGE_BOX) { handler, inventory, title ->
            SimpleInventoryScreen(
                handler,
                inventory,
                title,
                texture = HealthMod.id("textures/gui/bandage_box.png")
            )
        }

        ScreenRegistry.register(ScreenHandlerTypeRegistries.FIRST_AID_KIT) { handler, inventory, title ->
            SimpleInventoryScreen(
                handler,
                inventory,
                title,
                texture = HealthMod.id("textures/gui/first_aid_kit.png")
            )
        }

        ScreenRegistry.register(ScreenHandlerTypeRegistries.BLOOD_TEST_MACHINE) { handler, inventory, title ->
            // TODO: use own texture?
            SimpleInventoryScreen(
                handler,
                inventory,
                title,
                texture = Identifier("minecraft", "textures/gui/container/brewing_stand.png")
            )
        }
    }
}