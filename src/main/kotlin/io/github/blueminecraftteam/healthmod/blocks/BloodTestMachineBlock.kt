/*
 * Copyright (c) 2020 Blue Minecraft Team.
 *
 * This file is part of HealthMod.
 *
 * HealthMod is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * HealthMod is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with HealthMod.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.blueminecraftteam.healthmod.blocks

import io.github.blueminecraftteam.healthmod.blocks.entities.BloodTestMachineBlockEntity
import io.github.blueminecraftteam.healthmod.compatibility.datagen.LootTable
import io.github.blueminecraftteam.healthmod.compatibility.datagen.Model
import io.github.blueminecraftteam.healthmod.compatibility.datagen.State
import io.github.blueminecraftteam.healthmod.util.isServer
import net.minecraft.block.*
import net.minecraft.block.HorizontalFacingBlock.FACING
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.screen.ScreenHandler
import net.minecraft.state.StateManager
import net.minecraft.util.*
import net.minecraft.util.function.BooleanBiFunction
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World
import java.util.stream.Stream

// TODO fix hitbox and collision box
@LootTable(LootTable.Type.SILK_TOUCH_ONLY)
@Model(Model.Type.OVERRIDING)
@State(State.Type.HORIZONTALLY_ROTATING)
class BloodTestMachineBlock(settings: Settings) : BlockWithEntity(settings) {
    init {
        this.defaultState = this.stateManager.defaultState.with(FACING, Direction.NORTH)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(FACING)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState =
        defaultState.with(FACING, ctx.playerFacing.opposite)


    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState =
        state.with(FACING, rotation.rotate(state[FACING]))

    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState =
        state.rotate(mirror.getRotation(state[FACING]))

    override fun getOutlineShape(state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext) = SHAPE

    override fun getCollisionShape(state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext) = SHAPE

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
        if (world.isServer) {
            state.createScreenHandlerFactory(world, pos)?.let(player::openHandledScreen)
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
            (world.getBlockEntity(pos) as? BloodTestMachineBlockEntity)?.let {
                ItemScatterer.spawn(world, pos, it)
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
        ).reduce { first, second -> VoxelShapes.combineAndSimplify(first, second, BooleanBiFunction.OR) }.get()
    }
}