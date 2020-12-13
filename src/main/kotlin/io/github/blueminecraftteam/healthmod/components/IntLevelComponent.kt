package io.github.blueminecraftteam.healthmod.components

import dev.onyxstudios.cca.api.v3.component.ComponentV3
import net.minecraft.nbt.CompoundTag

interface IntLevelComponent : ComponentV3 {
    var value: Int
    val max get() = Int.MAX_VALUE
    val min get() = Int.MIN_VALUE

    operator fun plus(tagToAmount: Pair<CompoundTag, Int>) {
        readFromNbt(tagToAmount.first)
        this.value += tagToAmount.second
        writeToNbt(tagToAmount.first)
    }

    operator fun minus(tagToAmount: Pair<CompoundTag, Int>) {
        readFromNbt(tagToAmount.first)
        this.value -= tagToAmount.second
        writeToNbt(tagToAmount.first)
    }
}