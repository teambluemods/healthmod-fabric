package io.github.blueminecraftteam.healthmod.blocks

import io.github.blueminecraftteam.healthmod.blocks.entities.BloodTestMachineBlockEntity
import io.github.blueminecraftteam.healthmod.registries.ItemRegistries
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.screen.ScreenHandler
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.ItemScatterer
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.World

class BloodTestMachineBlock(settings: Settings) : BlockWithEntity(settings) {
    override fun createBlockEntity(world: BlockView) = BloodTestMachineBlockEntity()

    override fun getRenderType(state: BlockState) = BlockRenderType.MODEL

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand,
        hit: BlockHitResult
    ): ActionResult {
        if (!world.isClient) {
            val factory = state.createScreenHandlerFactory(world, pos)

            if (factory != null) {
                player.openHandledScreen(factory)
            }
        }

        return ActionResult.SUCCESS
    }

    override fun onPlaced(
        world: World,
        pos: BlockPos,
        state: BlockState,
        placer: LivingEntity?,
        itemStack: ItemStack
    ) {
        val blockEntity = world.getBlockEntity(pos)

        if (blockEntity is BloodTestMachineBlockEntity) {
            for (i in 0 until 3) {
                blockEntity.setStack(i, ItemRegistries.BLOOD_VIAL.defaultStack)
            }
        }
    }

    override fun onStateReplaced(
        state: BlockState,
        world: World,
        pos: BlockPos,
        newState: BlockState,
        moved: Boolean
    ) {
        if (state.block != newState.block) {
            val blockEntity = world.getBlockEntity(pos)

            if (blockEntity is BloodTestMachineBlockEntity) {
                ItemScatterer.spawn(world, pos, blockEntity)
                // update comparators
                world.updateComparators(pos, this)
            }

            super.onStateReplaced(state, world, pos, newState, moved)
        }
    }

    override fun hasComparatorOutput(state: BlockState) = true

    override fun getComparatorOutput(state: BlockState, world: World, pos: BlockPos) =
        ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos))
}