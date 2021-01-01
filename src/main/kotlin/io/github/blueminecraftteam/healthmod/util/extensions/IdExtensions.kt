package io.github.blueminecraftteam.healthmod.util.extensions

import net.minecraft.block.Block
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.item.Item
import net.minecraft.util.registry.Registry
import net.minecraft.village.VillagerProfession

val Item.id get() = Registry.ITEM.getId(this)

val Block.id get() = Registry.BLOCK.getId(this)

val StatusEffect.id get() = Registry.STATUS_EFFECT.getId(this)

val VillagerProfession.id get() = Registry.VILLAGER_PROFESSION.getId(this)