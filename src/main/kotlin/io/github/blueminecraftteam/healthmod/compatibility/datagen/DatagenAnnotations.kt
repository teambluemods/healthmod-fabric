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

package io.github.blueminecraftteam.healthmod.compatibility.datagen

/**
 * Tells the language file generator to use [translation] for the English translation instead of using the registry name.
 */
@Target(AnnotationTarget.CLASS)
annotation class CustomEnglishTranslation(val translation: String)

/**
 * Tells the model generator to generate a model consistent with the [type] provided.
 */
@Target(AnnotationTarget.CLASS)
annotation class Model(val type: Type) {
    enum class Type {
        /**
         * Generate a cube block model and a simple item model.
         */
        CUBE,

        /**
         * Generate a cube-all block model and simple item model.
         */
        CUBE_ALL,

        /**
         * Generate a simple item model but no block model.
         *
         * This is so that you can manually add a custom block model in the generator class.
         */
        CUSTOM,
    }
}

/**
 * Tells the block state generator to generate a block state consistent with the [type] provided.
 */
@Target(AnnotationTarget.CLASS)
annotation class State(val type: Type) {
    enum class Type {
        /**
         * Generate a simple block state with only one possible model.
         */
        SIMPLE,

        /**
         * Generate a block state with 4 variations all pointing to the same model, except using different `y` rotations.
         */
        HORIZONTALLY_ROTATING
    }
}

/**
 * Tells the loot table generator to generate a loot table consistent with the [type] provided.
 */
@Target(AnnotationTarget.CLASS)
annotation class LootTable(val type: Type) {
    enum class Type {
        /**
         * Generate no loot table.
         */
        NONE,

        /**
         * Generate a loot table only dropping the block itself when mined with a silk touch tool.
         */
        SILK_TOUCH_ONLY,

        /**
         * Generate a regular, self dropping loot table.
         */
        NORMAL,

        /**
         * Generate no loot table.
         *
         * This is so that you can manually add a custom loot table in the generator class.
         */
        CUSTOM,
    }
}
