package io.github.blueminecraftteam.healthmod.registries

import io.github.blueminecraftteam.healthmod.HealthMod
import io.github.blueminecraftteam.healthmod.professions.trades.ProfessionTrades
import net.fabricmc.fabric.api.`object`.builder.v1.villager.VillagerProfessionBuilder
import net.fabricmc.fabric.api.`object`.builder.v1.world.poi.PointOfInterestHelper
import net.minecraft.sound.SoundEvents
import net.minecraft.util.registry.Registry
import net.minecraft.village.TradeOffers
import net.minecraft.village.VillagerProfession
import net.minecraft.world.poi.PointOfInterestType

object ProfessionRegistries : ModRegistry<VillagerProfession> {
    override val registry: Registry<VillagerProfession> get() = Registry.VILLAGER_PROFESSION

    val DOCTOR_POINT_OF_INTEREST: PointOfInterestType = PointOfInterestHelper.register(
        HealthMod.id("doctor_point_of_interest"),
        /* ticket count of */ 1,
        /* search distance of */ 1,
        BlockRegistries.FIRST_AID_KIT,
        BlockRegistries.BANDAGE_BOX
    )

    val DOCTOR: VillagerProfession = register(
        id = "doctor",
        toRegister = VillagerProfessionBuilder.create()
            .id(HealthMod.id("doctor"))
            .workstation(DOCTOR_POINT_OF_INTEREST)
            .workSound(SoundEvents.BLOCK_BREWING_STAND_BREW)
            .build()
    )

    override fun init() {
        super.init()

        TradeOffers.PROFESSION_TO_LEVELED_TRADE[DOCTOR] = ProfessionTrades.DOCTOR
    }
}