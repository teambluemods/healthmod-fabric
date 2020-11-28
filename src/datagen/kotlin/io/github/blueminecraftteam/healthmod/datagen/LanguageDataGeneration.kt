package io.github.blueminecraftteam.healthmod.datagen

import com.google.gson.JsonObject
import io.github.blueminecraftteam.healthmod.HealthMod
import io.github.blueminecraftteam.healthmod.registries.ItemRegistries
import io.github.blueminecraftteam.healthmod.registries.StatusEffectRegistries
import me.shedaniel.cloth.api.datagen.v1.SimpleData
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import org.apache.commons.lang3.text.WordUtils
import kotlin.reflect.full.memberProperties

class LanguageFileDsl {
    val json = JsonObject()

    fun simple(translationKey: String, translated: String) {
        json.addProperty(translationKey, translated)
    }

    fun item(item: Item) {
        json.addProperty(
            item.translationKey,
            WordUtils.capitalizeFully(
                Registry.ITEM
                    .getId(item)
                    .path
                    .replace("_", " ")
            )
        )
    }

    fun itemGroup(id: Identifier, name: String) {
        json.addProperty("itemGroup.${id.namespace}.${id.path}", name)
    }

    fun statusEffect(effect: StatusEffect) {
        json.addProperty(
            effect.translationKey,
            WordUtils.capitalizeFully(
                Registry.STATUS_EFFECT
                    .getId(effect)!!
                    .path
                    .replace("_", " ")
            )
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

    for (item in itemRegistriesClass.memberProperties
        .map { it.get(itemRegistriesClass.objectInstance!!) }
        .filterIsInstance<Item>()) {
        item(item)
    }

    val statusEffectRegistriesClass = StatusEffectRegistries::class

    for (effect in statusEffectRegistriesClass.memberProperties
        .map { it.get(statusEffectRegistriesClass.objectInstance!!) }
        .filterIsInstance<StatusEffect>()) {
        statusEffect(effect)
    }

    itemGroup(HealthMod.id("all"), "HealthMod")

    simple("text.${HealthMod.MOD_ID}.band_aid.1", "Gives you regeneration for 15 seconds.")
    simple("text.${HealthMod.MOD_ID}.band_aid.2", "Only has 2 uses.")
    simple("text.${HealthMod.MOD_ID}.band_aid.3", "After the first use, you have a 25% chance of getting an infection.")
    simple("death.attack.wound_infection", "%1\$s died from a wound infection")
})