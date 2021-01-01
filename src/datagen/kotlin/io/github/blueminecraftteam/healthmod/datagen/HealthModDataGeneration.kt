/*
 * Copyright (c) 2020 Blue Minecraft Team.
 *
 * This file is part of HealthMod.
 *
 * HealthMod is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * HealthMod is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with HealthMod.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.blueminecraftteam.healthmod.datagen

import io.github.blueminecraftteam.healthmod.HealthMod
import io.github.blueminecraftteam.healthmod.util.LoggerDelegate
import me.shedaniel.cloth.api.datagen.v1.DataGeneratorHandler
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint
import org.apache.logging.log4j.Logger
import java.nio.file.Paths
import kotlin.system.exitProcess

object HealthModDataGeneration : PreLaunchEntrypoint {
    private val LOGGER: Logger by LoggerDelegate()

    /**
     * Runs data generation.
     */
    override fun onPreLaunch() {
        HealthMod.initRegistries()

        try {
            val handler = DataGeneratorHandler.create(Paths.get("../src/generated/resources"))

            English.generate(handler.simple)
            ModelDataGeneration.generate(handler.modelStates)
            LootTableDataGeneration.generate(handler.lootTables)

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