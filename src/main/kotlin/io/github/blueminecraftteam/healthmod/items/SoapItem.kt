package io.github.blueminecraftteam.healthmod.items

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.LiteralText
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

//This was just for some testing ya know
class SoapItem(settings: Settings) : Item(settings) {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val stack = user.getStackInHand(hand)
        user.sendMessage(LiteralText("You were cleansed."), true)

        return TypedActionResult.consume(stack)
    }
}