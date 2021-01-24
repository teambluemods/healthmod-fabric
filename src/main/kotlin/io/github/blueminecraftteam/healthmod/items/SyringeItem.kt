package io.github.blueminecraftteam.healthmod.items

import io.github.blueminecraftteam.healthmod.registries.ItemRegistries
import io.github.blueminecraftteam.healthmod.util.extensions.isServer
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class SyringeItem(settings: Item.Settings): Item(settings) {

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (world.isServer) {
            val stackInHand = user.getStackInHand(hand)


            if (world.isServer) {
                val setStackInHand = user.giveItemStack(ItemRegistries.SYRINGE.defaultStack)
                user.damage(DamageSource.OUT_OF_WORLD, 2.0f)
            }
        }
        return super.use(world, user, hand)
    }
}