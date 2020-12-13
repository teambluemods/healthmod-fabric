package io.github.blueminecraftteam.healthmod.components

import dev.onyxstudios.cca.api.v3.component.ComponentV3

interface BooleanComponent : ComponentV3 {
    var value: Boolean
}