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

package io.github.teambluemods.healthmod

import io.github.teambluemods.healthmod.config.HealthModConfig
import io.github.teambluemods.healthmod.registries.*
import io.github.teambluemods.healthmod.util.LoggerDelegate
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig
import me.sargunvohra.mcmods.autoconfig1u.ConfigHolder
import me.sargunvohra.mcmods.autoconfig1u.serializer.Toml4jConfigSerializer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier

object HealthMod : ModInitializer {
    const val MOD_ID = "healthmod"
    val ITEM_GROUP: ItemGroup = FabricItemGroupBuilder.create(id("all"))
        .icon { ItemRegistries.BANDAGE.defaultStack }
        .build()
    private lateinit var configHolder: ConfigHolder<HealthModConfig>

    @JvmStatic
    val config: HealthModConfig
        get() {
            if (!this::configHolder.isInitialized) {
                configHolder = AutoConfig.register(HealthModConfig::class.java, ::Toml4jConfigSerializer)
                LOGGER.debug("Registered config!")
            }

            return configHolder.get()
        }
    private val LOGGER by LoggerDelegate()

    fun id(path: String) = Identifier(MOD_ID, path)

    override fun onInitialize() {
        initRegistries()

        LOGGER.debug("Initialized all registries!")
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
        VillagerProfessionRegistries.init()
    }
}