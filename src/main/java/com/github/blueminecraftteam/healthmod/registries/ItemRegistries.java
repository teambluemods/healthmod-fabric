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

package com.github.blueminecraftteam.healthmod.registries;

import com.github.blueminecraftteam.healthmod.HealthMod;
import com.github.blueminecraftteam.healthmod.items.BandAidItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public class ItemRegistries {
    public static final Item BAND_AID = register("band_aid", new BandAidItem(new Item.Settings()
            .group(HealthMod.ITEM_GROUP)
            .maxCount(16)
            .maxDamage(2)
            .rarity(Rarity.UNCOMMON)));

    private static Item register(String id, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(HealthMod.MOD_ID, id), item);
    }
}
