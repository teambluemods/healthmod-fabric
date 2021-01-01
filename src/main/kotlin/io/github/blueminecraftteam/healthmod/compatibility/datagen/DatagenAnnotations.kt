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

annotation class CustomEnglishTranslation(val translation: String)

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

        /**
         * Generates a simple item model but no block model so that you can manually override in the generator class.
         */
        OVERRIDING,
    }
}

annotation class State(val type: Type) {
    enum class Type {
        /**
         * Simple state with only one possible model.
         */
        SIMPLE,

        /**
         * State with 4 variations all pointing to the same model, except using different `y` rotations.
         */
        HORIZONTALLY_ROTATING
    }
}

/**
 * Tells the generator to generate a loot table consistent with the [type] provided.
 */
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
         * Custom loot table. Manually set in the generator class.
         */
        CUSTOM,
    }
}
