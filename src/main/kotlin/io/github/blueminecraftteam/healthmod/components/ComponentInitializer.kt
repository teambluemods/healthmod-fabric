package io.github.blueminecraftteam.healthmod.components

import dev.onyxstudios.cca.api.v3.component.ComponentKey
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry
import net.minecraft.util.Identifier
import nerdhub.cardinal.components.api.util.RespawnCopyStrategy


class ComponentInitializer : EntityComponentInitializer {
    val SANITIZED_WOUND: ComponentKey<BooleanComponent> = ComponentRegistryV3.INSTANCE.getOrCreate(
        Identifier("healthmod:sanitized_wound"),
        BooleanComponent::class.java
    )

    val BLOOD_LEVEL: ComponentKey<IntLevelComponent> = ComponentRegistryV3.INSTANCE.getOrCreate(
        Identifier("healthmod:blood_level"),
        IntLevelComponent::class.java
    )

    override fun registerEntityComponentFactories(registry: EntityComponentFactoryRegistry) {
        // add component to every PlayerEntity
        registry.registerForPlayers(SANITIZED_WOUND, { HasSanitizedWoundComponent() }, RespawnCopyStrategy.INVENTORY)
        registry.registerForPlayers(BLOOD_LEVEL, { BloodLevelComponent() }, RespawnCopyStrategy.LOSSLESS_ONLY)
    }

}