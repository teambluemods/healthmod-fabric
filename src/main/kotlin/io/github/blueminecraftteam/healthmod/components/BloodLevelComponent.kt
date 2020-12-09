package io.github.blueminecraftteam.healthmod.components

import dev.onyxstudios.cca.api.v3.component.ComponentV3
import net.minecraft.nbt.CompoundTag

public interface IntLevelComponent : ComponentV3 {
    var value: Int
    var max: Int
    var min: Int
}

class BloodLevelComponent : IntLevelComponent{
    override var value: Int = 20
    override var max: Int = 20
    override var min: Int = 0

    override fun readFromNbt(tag: CompoundTag) {
        value = tag.getInt("value")
    }

    override fun writeToNbt(tag: CompoundTag) {
        tag.putInt("value", value)
    }

    fun increase(tag: CompoundTag, amount: Int) {
        readFromNbt(tag)
        value += amount
        writeToNbt(tag)
    }
    fun decrease(tag: CompoundTag, amount: Int) {
        readFromNbt(tag)
        value -= amount
        writeToNbt(tag)
    }
}