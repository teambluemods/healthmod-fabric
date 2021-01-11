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

package io.github.blueminecraftteam.healthmod.datagen

import io.github.blueminecraftteam.healthmod.HealthMod
import io.github.blueminecraftteam.healthmod.compatibility.datagen.CustomEnglishTranslation
import io.github.blueminecraftteam.healthmod.registries.BlockRegistries
import io.github.blueminecraftteam.healthmod.registries.ItemRegistries
import io.github.blueminecraftteam.healthmod.registries.StatusEffectRegistries
import io.github.blueminecraftteam.healthmod.registries.VillagerProfessionRegistries
import io.github.blueminecraftteam.healthmod.util.extensions.capitalizeFully
import io.github.blueminecraftteam.healthmod.util.extensions.id
import io.github.xf8b.utils.gson.JsonDsl
import me.shedaniel.cloth.api.datagen.v1.SimpleData
import net.minecraft.block.Block
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import net.minecraft.village.VillagerProfession
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

object EnglishLanguageDataGeneration : Generator<SimpleData> {
    override fun generate(data: SimpleData) {
        val jsonDsl = JsonDsl()

        jsonDsl.apply {
            // items
            ItemRegistries::class.memberProperties.onEach { it.isAccessible = true }
                .map { it.get(ItemRegistries) }
                .filterIsInstance<Item>()
                .forEach { item ->
                    item::class.annotations
                        .filterIsInstance<CustomEnglishTranslation>()
                        .getOrNull(0)
                        ?.translation
                        ?.let { translation -> translateItem(item, translation) }
                        ?: translateItem(item)
                }

            // blocks
            BlockRegistries::class.memberProperties.onEach { it.isAccessible = true }
                .map { it.get(BlockRegistries) }
                .filterIsInstance<Block>()
                .forEach { block ->
                    block::class.annotations
                        .filterIsInstance<CustomEnglishTranslation>()
                        .getOrNull(0)
                        ?.translation
                        ?.let { translation -> translateBlock(block, translation) }
                        ?: translateBlock(block)
                }

            // status effects
            StatusEffectRegistries.javaClass.fields
                .map { it.get(StatusEffectRegistries) }
                .filterIsInstance<StatusEffect>()
                .forEach(this::translateStatusEffect)

            // villager professions
            VillagerProfessionRegistries::class.memberProperties.onEach { it.isAccessible = true }
                .map { it.get(VillagerProfessionRegistries) }
                .filterIsInstance<VillagerProfession>()
                .forEach(this::translateVillagerProfession)

            translateItemGroup(HealthMod.id("all"), "HealthMod")

            // misc text
            translateText("bandage.1", "Gives you regeneration for 15 seconds.")
            translateText("bandage.2", "Can only be used once.")
            translateText("bandage.3", "You have a 25% chance of getting an infection.")
            translateText("bandage.failed_apply", "Uh oh, you didn't put the bandage on correctly!")

            translateText("isopropyl_alcohol.apply", "You feel a slight pain where you were bleeding.")

            translateText("soap.apply", "You were cleansed.")

            translateText("antibiotics.resistant_bacteria", "Uh oh, the bacteria have become resistant!")

            // config
            translateConfig("title", "HealthMod Config")

            translateConfigCategory("woundInfection", "Wound Infection")
            translateConfigCategory("easterEggs", "Easter Eggs")
            translateConfigCategory("other", "Other")

            translateConfigOption(
                option = "bandageInfectionChance",
                translated = "Bandage Infection Chance",
                tooltip = "Chance for bandages to fail and give you a wound infection (1/config value)"
            )
            translateConfigOption(
                option = "bandageInfectionChanceWhenHealthy",
                translated = "Bandage Infection Chance (Healthy)",
                tooltip = "Chance for bandages to fail and give you a wound infection when healthy (1/config value)"
            )
            translateConfigOption(
                option = "damagedInfectionChance",
                translated = "Damaged Infection Chance",
                tooltip = "Chance for you to get a wound infection when damaged by 2 or more (1/config value)"
            )
            translateConfigOption(
                option = "damagedInfectionChanceWhenHealthy",
                translated = "Damaged Infection Chance (Healthy)",
                tooltip = "Chance for you to get a wound infection when healthy and damaged by 2 or more (1/config value)"
            )
            translateConfigOption(
                option = "extraSplashTexts",
                translated = "Extra Splash Texts",
                tooltip = """If extra splash texts (e.g. "helth goes brrrr") should be added"""
            )
            translateConfigOption(
                option = "bacterialResistanceChance",
                translated = "Bacterial Resistance Chance",
                tooltip = "Chance for antibiotics to fail and make harmful effects stronger (1/config value)"
            )

            // death messages
            property("death.attack.wound_infection", "%1\$s died from a wound infection")
            property("death.attack.wound_infection.player", "%1\$s died from a wound infection")
        }

        data.addJson("assets/${HealthMod.MOD_ID}/lang/en_us.json", jsonDsl.toJsonElement())
    }
}

// extensions to make life easier

private fun JsonDsl.translateText(name: String, translated: String) {
    property("text.${HealthMod.MOD_ID}.$name", translated)
}

private fun JsonDsl.translateConfig(text: String, translated: String) {
    property("text.autoconfig.${HealthMod.MOD_ID}.$text", translated)
}

private fun JsonDsl.translateConfigCategory(category: String, translated: String) {
    translateConfig("category.$category", translated)
}

private fun JsonDsl.translateConfigOption(option: String, translated: String, tooltip: String? = null) {
    translateConfig("option.$option", translated)

    if (tooltip != null) {
        translateConfig("option.$option.@Tooltip", tooltip)
    }
}

private fun JsonDsl.translateItem(item: Item, translation: String = item.id.path.registryNameToLang()) {
    property(item.translationKey, translation)
}

private fun JsonDsl.translateBlock(block: Block, translation: String = block.id.path.registryNameToLang()) {
    property(block.translationKey, translation)
}

private fun JsonDsl.translateItemGroup(id: Identifier, name: String) {
    property("itemGroup.${id.namespace}.${id.path}", name)
}

private fun JsonDsl.translateStatusEffect(effect: StatusEffect) {
    property(effect.translationKey, effect.id!!.path.registryNameToLang())
}

private fun JsonDsl.translateVillagerProfession(villagerProfession: VillagerProfession) {
    val id = villagerProfession.id.path

    property("entity.minecraft.villager.$id", id.capitalizeFully())
    property("subtitles.entity.villager.$id", "${id.capitalizeFully()} works")
}

private fun String.registryNameToLang() = this.replace("_", " ").capitalizeFully()