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

package io.github.blueminecraftteam.healthmod.datagen.util.extensions

import io.github.blueminecraftteam.healthmod.HealthMod
import io.github.blueminecraftteam.healthmod.util.extensions.capitalizeFully
import io.github.blueminecraftteam.healthmod.util.extensions.id
import io.github.xf8b.utils.gson.JsonDsl
import net.minecraft.block.Block
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import net.minecraft.village.VillagerProfession

// this is purely for the language json

fun JsonDsl.translateText(name: String, translated: String) {
    property("text.${HealthMod.MOD_ID}.$name", translated)
}

fun JsonDsl.translateConfig(text: String, translated: String) {
    property("text.autoconfig.${HealthMod.MOD_ID}.$text", translated)
}

fun JsonDsl.translateConfigCategory(category: String, translated: String) {
    translateConfig("category.$category", translated)
}

fun JsonDsl.translateConfigOption(option: String, translated: String, tooltip: String? = null) {
    translateConfig("option.$option", translated)

    if (tooltip != null) {
        translateConfig("option.$option.@Tooltip", tooltip)
    }
}

fun JsonDsl.translateItem(
    item: Item,
    translation: String = item.id.path.replace("_", " ").capitalizeFully()
) {
    property(item.translationKey, translation)
}

fun JsonDsl.translateBlock(
    block: Block,
    translation: String = block.id.path.replace("_", " ").capitalizeFully()
) {
    property(block.translationKey, translation)
}

fun JsonDsl.translateItemGroup(id: Identifier, name: String) {
    property("itemGroup.${id.namespace}.${id.path}", name)
}

fun JsonDsl.translateStatusEffect(effect: StatusEffect) {
    property(effect.translationKey, effect.id!!.path.replace("_", " ").capitalizeFully())
}

fun JsonDsl.translateVillagerProfession(villagerProfession: VillagerProfession) {
    val id = villagerProfession.id.path

    property("entity.minecraft.villager.$id", id.capitalizeFully())
    property("subtitles.entity.villager.$id", "${id.capitalizeFully()} works")
}