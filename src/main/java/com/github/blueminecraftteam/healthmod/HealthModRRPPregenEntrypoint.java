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

package com.github.blueminecraftteam.healthmod;

import com.github.blueminecraftteam.healthmod.registries.ItemRegistries;
import com.github.blueminecraftteam.healthmod.registries.StatusEffectRegistries;
import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RRPPreGenEntrypoint;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.lang.JLang;
import net.devtech.arrp.json.models.JModel;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;

import static com.github.blueminecraftteam.healthmod.HealthMod.MOD_ID;
import static net.devtech.arrp.api.RuntimeResourcePack.id;

public class HealthModRRPPregenEntrypoint implements RRPPreGenEntrypoint {
    public static final RuntimeResourcePack RESOURCE_PACK = RuntimeResourcePack.create(MOD_ID + ":resources");
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void pregen() {
        JLang lang = JLang.lang();
        lang.itemGroup(id(MOD_ID, "all"), WordUtils.capitalizeFully(MOD_ID));
        for (Field field : ItemRegistries.class.getFields()) {
            try {
                Item item = (Item) field.get(null);
                Identifier identifier = Registry.ITEM.getId(item);
                lang.item(identifier, getFormattedName(item.getTranslationKey(), "item"));
                RESOURCE_PACK.addModel(JModel.model("item/generated")
                                .textures(JModel.textures()
                                        .layer0(MOD_ID + ":/item/" + identifier.getPath())),
                        id(MOD_ID, "item/" + identifier.getPath()));
            } catch (IllegalAccessException exception) {
                LOGGER.error("[" + WordUtils.capitalizeFully(MOD_ID) + "] An exception happened while generating the lang/model file!", exception);
            }
        }
        for (Field field : StatusEffectRegistries.class.getFields()) {
            try {
                StatusEffect statusEffect = (StatusEffect) field.get(null);
                String translationKey = statusEffect.getTranslationKey();
                //lang.status is broken so /shrug
                lang.translate(translationKey, this.getFormattedName(translationKey, "effect"));
            } catch (IllegalAccessException exception) {
                LOGGER.error("[" + WordUtils.capitalizeFully(MOD_ID) + "] An exception happened while generating the lang file!", exception);
            }
        }
        lang.translate("text." + MOD_ID + ".band_aid.1", "Gives you regeneration for 15 seconds.");
        lang.translate("text." + MOD_ID + ".band_aid.2", "Only has 2 uses.");
        lang.translate("text." + MOD_ID + ".band_aid.3", "After the first use, you have a 25% chance of getting an infection.");
        lang.translate("death.attack.wound_infection", "%1$s died from a wound infection");
        RESOURCE_PACK.addLang(id(MOD_ID, "en_us"), lang);
        RRPCallback.EVENT.register(resourcePacks -> resourcePacks.add(RESOURCE_PACK));
    }

    private String getFormattedName(String translationKey, String type) {
        return WordUtils.capitalizeFully(translationKey.replace(type + "." + MOD_ID + ".", "")
                .replace("_", " "));
    }
}
