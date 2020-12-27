package io.github.blueminecraftteam.healthmod.components

import net.minecraft.nbt.CompoundTag

class WoundedComponent : BooleanComponent {
    override var value = true

    override fun readFromNbt(tag: CompoundTag) {
        value = tag.getBoolean("value")
    }

    override fun writeToNbt(tag: CompoundTag) {
        tag.putBoolean("value", value)
    }
}