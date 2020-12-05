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

import com.google.gson.JsonObject
import io.github.blueminecraftteam.healthmod.HealthMod
import io.github.blueminecraftteam.healthmod.registries.BlockRegistries
import io.github.blueminecraftteam.healthmod.registries.ItemRegistries
import io.github.blueminecraftteam.healthmod.registries.StatusEffectRegistries
import io.github.blueminecraftteam.healthmod.util.capitalizeFully
import io.github.blueminecraftteam.healthmod.util.id
import me.shedaniel.cloth.api.datagen.v1.SimpleData
import net.minecraft.block.Block
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import kotlin.reflect.full.memberProperties

class LanguageFileDsl {
    val json = JsonObject()

    fun simple(translationKey: String, translated: String) {
        json.addProperty(translationKey, translated)
    }

    fun override(translationKey: String, translated: String) {
        json.remove(translationKey)
        json.addProperty(translationKey, translated)
    }

    fun text(name: String, translated: String) = simple("text.${HealthMod.MOD_ID}.$name", translated)

    fun config(text: String, translated: String) {
        simple("text.autoconfig.${HealthMod.MOD_ID}.$text", translated)
    }

    fun configCategory(category: String, translated: String) {
        config("category.$category", translated)
    }

    fun configOption(option: String, translated: String, tooltip: String? = null) {
        config("option.$option", translated)

        if (tooltip != null) {
            config("option.$option.@Tooltip", tooltip)
        }
    }

    fun item(item: Item) {
        json.addProperty(
            item.translationKey,
            item.id.path.replace("_", " ").capitalizeFully()
        )
    }

    fun block(block: Block) {
        json.addProperty(
            block.translationKey,
            block.id.path.replace("_", " ").capitalizeFully()
        )
    }

    fun itemGroup(id: Identifier, name: String) {
        json.addProperty("itemGroup.${id.namespace}.${id.path}", name)
    }

    fun statusEffect(effect: StatusEffect) {
        json.addProperty(
            effect.translationKey,
            effect.id!!.path.replace("_", " ").capitalizeFully()
        )
    }
}

sealed class LanguageDataGeneration(
    private val locale: String,
    private val languageFileDslClosure: LanguageFileDsl.() -> Unit
) {
    fun generate(data: SimpleData) {
        val languageFileDsl = LanguageFileDsl()
        languageFileDslClosure(languageFileDsl)
        data.addJson("assets/${HealthMod.MOD_ID}/lang/$locale.json", languageFileDsl.json)
    }
}

object English : LanguageDataGeneration(locale = "en_us", languageFileDslClosure = {
    val itemRegistriesClass = ItemRegistries::class

    itemRegistriesClass.memberProperties
        .map { it.get(itemRegistriesClass.objectInstance!!) }
        .filterIsInstance<Item>()
        .forEach { item ->
            item(item)
        }

    val blockRegistriesClass = BlockRegistries::class

    blockRegistriesClass.memberProperties
        .map { it.get(blockRegistriesClass.objectInstance!!) }
        .filterIsInstance<Block>()
        .forEach { block ->
            block(block)
        }

    val statusEffectRegistriesClass = StatusEffectRegistries::class

    statusEffectRegistriesClass.memberProperties
        .map { it.get(statusEffectRegistriesClass.objectInstance!!) }
        .filterIsInstance<StatusEffect>()
        .forEach { effect ->
            statusEffect(effect)
        }

    itemGroup(HealthMod.id("all"), "HealthMod")

    text("band_aid.1", "Gives you regeneration for 15 seconds.")
    text("band_aid.2", "Can only be used once.")
    text("band_aid.3", "You have a 25% chance of getting an infection.")
    text("band_aid.failed_apply", "Uh oh, you didn't put the band aid on correctly!")

    text("antibiotics.resistant_bacteria", "Uh oh, the bacteria has become resistant!")

    config("title", "HealthMod Config")

    configCategory("woundInfection", "Wound Infection")
    configCategory("other", "Other")

    configOption(
        option = "bacterialResistanceChance",
        translated = "Bacterial Resistance Chance",
        tooltip = "Chance for antibiotics to fail and make harmful effects stronger (1/config value)"
    )
    configOption(
        option = "bandAidInfectionChance",
        translated = "Band Aid Infection Chance",
        tooltip = "Chance for band aids to fail and give you a wound infection (1/config value)"
    )
    configOption(
        option = "bandAidInfectionChanceWhenHealthy",
        translated = "Band Aid Infection Chance (Healthy)",
        tooltip = "Chance for band aids to fail and give you a wound infection when healthy (1/config value)"
    )
    configOption(
        option = "damagedInfectionChance",
        translated = "Damaged Infection Chance",
        tooltip = "Chance for you to get a wound infection when damaged by 2 or more (1/config value)"
    )
    configOption(
        option = "damagedInfectionChanceWhenHealthy",
        translated = "Damaged Infection Chance (Healthy)",
        tooltip = "Chance for you to get a wound infection when healthy and damaged by 2 or more (1/config value)"
    )

    override(BlockRegistries.BAND_AID_BOX.translationKey, "Box of Band Aids")

    simple("death.attack.wound_infection", "%1\$s died from a wound infection")
    simple("death.attack.wound_infection.player", "%1\$s died from a wound infection")
})