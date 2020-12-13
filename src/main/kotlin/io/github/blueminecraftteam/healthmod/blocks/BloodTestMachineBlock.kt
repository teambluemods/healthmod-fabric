/*
 * Copyright (c) 2020 Blue Minecraft Team.
 *
 * This file is part of HealthMod.
 *
 * HealthMod is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * HealthMod is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with HealthMod.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.blueminecraftteam.healthmod.blocks

import io.github.blueminecraftteam.healthmod.blocks.entities.BloodTestMachineBlockEntity
import net.minecraft.block.*
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.screen.ScreenHandler
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.ItemScatterer
import net.minecraft.util.function.BooleanBiFunction
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World
import java.util.stream.Stream

// TODO make this rotate
class BloodTestMachineBlock(settings: Settings) : BlockWithEntity(settings) {
    override fun getOutlineShape(
        state: BlockState?,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape = SHAPE

    override fun getCollisionShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape = SHAPE

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

            @Suppress("DEPRECATION")
            super.onStateReplaced(state, world, pos, newState, moved)
        }
    }

    override fun hasComparatorOutput(state: BlockState) = true

    override fun getComparatorOutput(state: BlockState, world: World, pos: BlockPos) =
        ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos))

    companion object {
        private val SHAPE = Stream.of(
            Block.createCuboidShape(1.0, 0.0, 10.0, 11.0, 13.0, 15.0),
            Block.createCuboidShape(1.0, 0.0, 7.0, 11.0, 9.0, 10.0),
            Block.createCuboidShape(11.0, 0.0, 7.0, 15.0, 13.0, 15.0),
            Block.createCuboidShape(12.0, 4.0, 6.0, 14.0, 10.0, 7.0),
            Block.createCuboidShape(12.0, 0.0, 5.0, 14.0, 1.0, 7.0),
            Block.createCuboidShape(11.0, 0.0, 1.0, 15.0, 1.0, 5.0),
            Block.createCuboidShape(12.0, 1.0, 2.0, 14.0, 3.0, 4.0),
            Block.createCuboidShape(13.0, 2.0, 1.0, 14.0, 5.0, 2.0),
            Block.createCuboidShape(14.0, 2.0, 4.0, 15.0, 5.0, 5.0),
            Block.createCuboidShape(11.0, 2.0, 3.0, 12.0, 5.0, 4.0),
            Block.createCuboidShape(6.0, 8.0, 15.0, 10.0, 13.0, 16.0)
        )
            .reduce { first, second -> VoxelShapes.combineAndSimplify(first, second, BooleanBiFunction.OR) }
            .get()
    }
}