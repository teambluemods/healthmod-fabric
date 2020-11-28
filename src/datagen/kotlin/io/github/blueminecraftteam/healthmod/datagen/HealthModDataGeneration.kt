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

package io.github.blueminecraftteam.healthmod.datagen

import io.github.blueminecraftteam.healthmod.registries.ItemRegistries
import io.github.blueminecraftteam.healthmod.registries.StatusEffectRegistries
import me.shedaniel.cloth.api.datagen.v1.DataGeneratorHandler
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint
import org.apache.logging.log4j.LogManager
import java.nio.file.Paths
import kotlin.system.exitProcess

object HealthModDataGeneration : PreLaunchEntrypoint {
    private val LOGGER = LogManager.getLogger()

    /**
     * Runs data generation.
     */
    override fun onPreLaunch() {
        try {
            val handler = DataGeneratorHandler.create(Paths.get("../src/generated/resources"))

            ItemRegistries.init()
            StatusEffectRegistries.init()

            English.generate(handler.simple)
            ModelDataGeneration.generate(handler.modelStates)

            handler.run()
        } catch (throwable: Throwable) {
            LOGGER.fatal("Error happened during datagen!", throwable)
            exitProcess(1)
        }

        LOGGER.info("thanks for flying on datagen airways™, we are approaching the runway")

        exitProcess(0)

        @Suppress("UNREACHABLE_CODE") // this is for the haha funi
        LOGGER.fatal("oh shit")
    }
}