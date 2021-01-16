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

package io.github.teambluemods.healthmod.professions.trades

import io.github.teambluemods.healthmod.registries.BlockRegistries
import io.github.teambluemods.healthmod.registries.ItemRegistries
import io.github.teambluemods.healthmod.util.immutableMapOf
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import net.minecraft.entity.Entity
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.village.TradeOffer
import net.minecraft.village.TradeOffers.Factory
import java.util.*

object ProfessionTrades {
    internal val DOCTOR = immutableMapOf(
        1 to arrayOf(
            SellFactory(sell = ItemRegistries.BANDAGE, price = 1, count = 2, experience = 1),
            SellFactory(sell = BlockRegistries.BANDAGE_BOX, price = 4, count = 1, experience = 4),
            SellFactory(sell = ItemRegistries.SOAP, price = 2, count = 1, experience = 1),
            BuyFactory(buy = ItemRegistries.BROCCOLI, price = 1, count = 4, experience = 2),
        ),
        2 to arrayOf(
            SellFactory(sell = ItemRegistries.ANTIBIOTICS, price = 1, count = 2, experience = 1),
            SellFactory(sell = BlockRegistries.FIRST_AID_KIT, price = 3, count = 1, experience = 4),
            BuyFactory(buy = ItemRegistries.ISOPROPYL_ALCOHOL, price = 2, count = 1, experience = 2),
        ),
        3 to arrayOf(
            BuyFactory(buy = BlockRegistries.BLOOD_TEST_MACHINE, price = 8, count = 1, experience = 8),
        ),
        // TODO more trades
    ).run(::Int2ObjectOpenHashMap)

    private class SellFactory(
        /** Item stack to sell */
        private val sell: ItemStack,
        /** Amount of emeralds to sell for */
        private val price: Int,
        /** Maximum amount of times this trade can be used */
        private val maxUses: Int,
        /** Amount of experience given on trade */
        private val experience: Int,
        private val priceMultiplier: Float = 0.05F
    ) : Factory {
        constructor(
            sell: ItemConvertible,
            price: Int,
            count: Int = 1,
            maxUses: Int = 12,
            experience: Int,
            priceMultiplier: Float = 0.05F
        ) : this(
            ItemStack(sell, count),
            price,
            maxUses,
            experience,
            priceMultiplier
        )

        override fun create(entity: Entity, random: Random) = TradeOffer(
            ItemStack(Items.EMERALD, price),
            sell,
            maxUses,
            experience,
            priceMultiplier
        )
    }

    private class BuyFactory(
        /** Item stack to buy */
        private val buy: ItemStack,
        /** Amount of emeralds to buy for */
        private val price: Int,
        /** Maximum amount of times this trade can be used */
        private val maxUses: Int,
        /** Experience given on trade */
        private val experience: Int,
        private val priceMultiplier: Float = 0.05F
    ) : Factory {
        constructor(
            buy: ItemConvertible,
            price: Int,
            count: Int = 1,
            maxUses: Int = 12,
            experience: Int,
            priceMultiplier: Float = 0.05F
        ) : this(
            ItemStack(buy, count),
            price,
            maxUses,
            experience,
            priceMultiplier
        )

        override fun create(entity: Entity, random: Random) = TradeOffer(
            buy,
            ItemStack(Items.EMERALD, price),
            maxUses,
            experience,
            priceMultiplier
        )
    }
}