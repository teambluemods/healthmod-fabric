package io.github.blueminecraftteam.healthmod.util

import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.screen.ScreenHandler

typealias MenuFactory = (Int, PlayerInventory, Inventory) -> ScreenHandler