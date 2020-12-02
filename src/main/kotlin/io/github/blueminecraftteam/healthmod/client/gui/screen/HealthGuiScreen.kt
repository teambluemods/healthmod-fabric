package io.github.blueminecraftteam.healthmod.client.gui.screen

import io.github.blueminecraftteam.healthmod.HealthMod
import me.lambdaurora.spruceui.Tooltip
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.TranslatableText

class HealthGuiScreen : Screen(TranslatableText("text.${HealthMod.MOD_ID}.health_gui.title")) {
    // TODO
    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        this.renderBackground(matrices)
        super.render(matrices, mouseX, mouseY, delta)
        drawCenteredText(matrices, textRenderer, title, width / 2, 15, 16777215)
        Tooltip.renderAll(matrices)
    }
}