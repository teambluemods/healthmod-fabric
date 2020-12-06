package io.github.blueminecraftteam.healthmod.blocks.entities

import io.github.blueminecraftteam.healthmod.client.gui.screen.BloodTestMachineScreenHandler
import io.github.blueminecraftteam.healthmod.inventories.ImplementedInventory
import io.github.blueminecraftteam.healthmod.registries.BlockEntityTypeRegistries
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventories
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.text.TranslatableText
import net.minecraft.util.Tickable
import net.minecraft.util.collection.DefaultedList

class BloodTestMachineBlockEntity : BlockEntity(BlockEntityTypeRegistries.BLOOD_TEST_MACHINE), NamedScreenHandlerFactory, Tickable,
    ImplementedInventory {
    override val items: DefaultedList<ItemStack> = DefaultedList.ofSize(3, ItemStack.EMPTY)

    override fun createMenu(syncId: Int, inv: PlayerInventory, player: PlayerEntity) =
        BloodTestMachineScreenHandler(syncId, inv, this)

    override fun getDisplayName() = TranslatableText(cachedState.block.translationKey)

    override fun fromTag(state: BlockState, tag: CompoundTag) {
        super.fromTag(state, tag)

        Inventories.fromTag(tag, this.items)
    }

    override fun toTag(tag: CompoundTag): CompoundTag {
        Inventories.toTag(tag, this.items)

        return super.toTag(tag)
    }

    override fun markDirty() {}

    override fun tick() {

    }
}