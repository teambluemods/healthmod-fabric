/*
 * Copyright (c) 2020, 2021 Team Blue.
 *
 * This file is part of HealthMod Fabric.
 *
 * HealthMod Fabric is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * HealthMod Fabric is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with HealthMod Fabric.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.teambluemods.healthmod.blocks

import io.github.teambluemods.healthmod.HealthMod
import io.github.teambluemods.healthmod.registries.BlockRegistries
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type
import org.objectweb.asm.tree.ClassNode
import sun.misc.Unsafe
import kotlin.reflect.KClass

// cursed and funny
object HealthModBlockAsmGenerator {
    private val KClass<*>.internalName get() = Type.getInternalName(this.java)
    private val KClass<*>.descriptor get() = Type.getDescriptor(this.java)
    private val isDevelopmentEnvironment get() = FabricLoader.getInstance().isDevelopmentEnvironment

    fun init() {
        val classNode = ClassNode()

        classNode.visit(
            V1_8,
            ACC_PUBLIC or ACC_SUPER,
            "io/github/teambluemods/healthmod/blocks/HealthModBlock",
            null,
            Block::class.internalName,
            arrayOf(ItemConvertible::class.internalName)
        )

        classNode.visitMethod(
            ACC_PUBLIC,
            "<init>",
            "(L${AbstractBlock.Settings::class.internalName};)V",
            null,
            null
        ).apply {
            visitVarInsn(ALOAD, 0)
            visitVarInsn(ALOAD, 1)
            visitMethodInsn(
                INVOKESPECIAL,
                Block::class.internalName,
                "<init>",
                "(L${AbstractBlock.Settings::class.internalName};)V",
                false
            )
            visitInsn(RETURN)
        }

        classNode.visitMethod(
            ACC_PUBLIC,
            if (isDevelopmentEnvironment) "onUse" else "method_9534",
            "(L${BlockState::class.internalName};L${World::class.internalName};L${BlockPos::class.internalName};L${PlayerEntity::class.internalName};L${Hand::class.internalName};L${BlockHitResult::class.internalName};)L${ActionResult::class.internalName};",
            null,
            null
        ).apply {
            visitVarInsn(ALOAD, 4)
            visitMethodInsn(
                INVOKEVIRTUAL,
                Entity::class.internalName,
                if (isDevelopmentEnvironment) "kill" else "method_5768",
                "()V",
                false
            )
            visitFieldInsn(
                GETSTATIC,
                ActionResult::class.internalName,
                if (isDevelopmentEnvironment) "PASS" else "field_5811",
                ActionResult::class.descriptor
            )
            visitInsn(ARETURN)
        }

        val classWriter = ClassWriter(ClassWriter.COMPUTE_FRAMES)

        classNode.accept(classWriter)

        val bytes = classWriter.toByteArray()

        @Suppress("UNCHECKED_CAST")
        val klass = try {
            Class.forName("java.lang.invoke.MethodHandles")
                .getMethod("lookup")
                .invoke(null)
                .run {
                    this.javaClass
                        .getMethod("defineClass", ByteArray::class.java)
                        .invoke(this, bytes) as Class<out Block>
                }
        } catch (exception: NoSuchMethodException) {
            Unsafe::class.java.run {
                val unsafe = getDeclaredField("theUnsafe").get(null)

                getMethod("defineClass").invoke(
                    unsafe,
                    "io/github/teambluemods/healthmod/blocks/HealthModBlock",
                    bytes,
                    0,
                    bytes.size,
                    Thread.currentThread().contextClassLoader,
                    null
                )
            } as Class<out Block>
        }

        BlockRegistries.register(
            "healthmod_block",
            klass.getConstructor(AbstractBlock.Settings::class.java)
                .newInstance(AbstractBlock.Settings.copy(Blocks.BEDROCK)),
            Item.Settings().group(HealthMod.ITEM_GROUP).maxCount(1)
        )
    }
}
