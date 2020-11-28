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

package io.github.blueminecraftteam.healthmod

import io.github.blueminecraftteam.healthmod.config.HealthModConfig
import io.github.blueminecraftteam.healthmod.registries.*
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig
import me.sargunvohra.mcmods.autoconfig1u.serializer.Toml4jConfigSerializer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier

object HealthMod : ModInitializer {
    const val MOD_ID = "healthmod"
    val ITEM_GROUP: ItemGroup = FabricItemGroupBuilder
        .create(id("all"))
        .icon { ItemStack(ItemRegistries.BAND_AID) }
        .build()

    fun id(path: String) = Identifier(MOD_ID, path)

    override fun onInitialize() {
        AutoConfig.register(HealthModConfig::class.java, ::Toml4jConfigSerializer)

        initRegistries()
    }

    /**
     * Force loads and registers all items, blocks, etc.
     *
     * This is to work around it not being registered until the class has been referenced.
     */
    fun initRegistries() {
        ItemRegistries.init()
        BlockRegistries.init()
        BlockEntityTypeRegistries.init()
        ScreenHandlerTypeRegistries.init()
        StatusEffectRegistries.init()
    }
}