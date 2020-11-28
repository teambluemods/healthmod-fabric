package io.github.blueminecraftteam.healthmod.registries

import io.github.blueminecraftteam.healthmod.HealthMod
import net.minecraft.util.registry.Registry

interface ModRegistry<T> {
    val registry: Registry<T>

    fun init() {}

    fun register(id: String, toRegister: T): T = Registry.register(registry, HealthMod.id(id), toRegister)
}