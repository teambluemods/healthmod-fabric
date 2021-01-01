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

import io.github.blueminecraftteam.healthmod.blocks.entities.BandageBoxBlockEntity
import io.github.blueminecraftteam.healthmod.compatibility.datagen.CustomEnglishTranslation
import io.github.blueminecraftteam.healthmod.compatibility.datagen.LootTable
import io.github.blueminecraftteam.healthmod.compatibility.datagen.Model
import io.github.blueminecraftteam.healthmod.compatibility.datagen.State
import io.github.blueminecraftteam.healthmod.registries.ItemRegistries
import io.github.blueminecraftteam.healthmod.util.isServer
import net.minecraft.block.*
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.screen.ScreenHandler
import net.minecraft.state.StateManager
import net.minecraft.util.*
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.BlockView
import net.minecraft.world.World

@CustomEnglishTranslation("Box of Bandages")
@Model(Model.Type.CUBE)
@State(State.Type.HORIZONTALLY_ROTATING)
@LootTable(LootTable.Type.NONE)
class BandageBoxBlock(settings: Settings) : BlockWithEntity(settings) {
    init {
        this.defaultState = this.stateManager.defaultState.with(HorizontalFacingBlock.FACING, Direction.NORTH)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(HorizontalFacingBlock.FACING)
    }

    override fun createBlockEntity(world: BlockView) = BandageBoxBlockEntity()

    override fun getRenderType(state: BlockState) = BlockRenderType.MODEL

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState =
        defaultState.with(HorizontalFacingBlock.FACING, ctx.playerFacing.opposite)


    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState = state.with(
        HorizontalFacingBlock.FACING,
        rotation.rotate(state.get(HorizontalFacingBlock.FACING))
    )

    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState =
        state.rotate(mirror.getRotation(state.get(HorizontalFacingBlock.FACING)))

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand,
        hit: BlockHitResult
    ): ActionResult {
        if (world.isServer) {
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

        if (blockEntity is BandageBoxBlockEntity) {
            for (i in 0 until 6) {
                blockEntity.setStack(
                    i,
                    // random amount of bandages from 1 to 8
                    ItemStack(ItemRegistries.BANDAGE, (1..8).random())
                )
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

            if (blockEntity is BandageBoxBlockEntity) {
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
}