package io.github.blueminecraftteam.healthmod.util.extensions

import net.minecraft.world.World

val World.isServer get() = !isClient