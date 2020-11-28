package io.github.blueminecraftteam.healthmod.datagen

import io.github.blueminecraftteam.healthmod.registries.ItemRegistries
import me.shedaniel.cloth.api.datagen.v1.ModelStateData
import net.minecraft.item.Item
import kotlin.reflect.full.memberProperties

object ModelDataGeneration {
    fun generate(data: ModelStateData) {
        val itemRegistriesClass = ItemRegistries::class

        itemRegistriesClass.memberProperties
            .map { it.get(itemRegistriesClass.objectInstance!!) }
            .filterIsInstance<Item>()
            .forEach { item ->
                data.addGeneratedItemModel(item)
            }
    }
}