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

package io.github.blueminecraftteam.healthmod.compatibility.datagen

import net.minecraft.loot.LootTable

annotation class LootTable(val type: Type) {
    enum class Type {
        /**
         * No loot table should be generated.
         */
        NONE,

        /**
         * A loot table only dropping the block itself when mined with a silk touch tool.
         */
        SILK_TOUCH_ONLY,

        /**
         * A regular, self dropping loot table.
         */
        NORMAL,

        /**
         * Custom loot table. Set [tableBuilder] if you are using this type!
         */
        CUSTOM;


        lateinit var tableBuilder: LootTable.Builder
    }
}

annotation class CustomEnglishTranslation(val translation: String) {
    annotation class ConfigEntry(val translation: String, val tooltip: String)
}

annotation class Model(val type: Type) {
    enum class Type {
        /**
         * Creates a cube block model and a simple item model.
         */
        CUBE,

        /**
         * Creates a cube-all block model and simple item model.
         */
        CUBE_ALL,

        OVERRIDING,
    }
}

annotation class State(val type: Type) {
    enum class Type {
        SIMPLE,
        HORIZONTALLY_ROTATING
    }
}
