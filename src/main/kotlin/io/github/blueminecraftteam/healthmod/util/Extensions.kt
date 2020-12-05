package io.github.blueminecraftteam.healthmod.util

import net.minecraft.block.Block
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.item.Item
import net.minecraft.util.registry.Registry
import org.apache.commons.lang3.text.WordUtils

fun String.capitalizeFully(): String = WordUtils.capitalizeFully(this)

val Item.id get() = Registry.ITEM.getId(this)

val Block.id get() = Registry.BLOCK.getId(this)

val StatusEffect.id get() = Registry.STATUS_EFFECT.getId(this)