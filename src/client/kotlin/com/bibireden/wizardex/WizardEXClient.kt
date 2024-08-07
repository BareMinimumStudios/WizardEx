package com.bibireden.wizardex

import com.bibireden.playerex.PlayerEX
import com.bibireden.playerex.registry.PlayerEXMenuRegistry
import com.bibireden.wizardex.ui.menu.WizardEXMenu

import net.fabricmc.api.ClientModInitializer
import net.minecraft.resources.ResourceLocation

object WizardEXClient : ClientModInitializer {
    private val MAGIC_ICON: ResourceLocation = WizardEX.id("textures/gui/staff.png")

    override fun onInitializeClient() {
        WizardEX.LOGGER.debug("Initializing client!")

        PlayerEXMenuRegistry.register(PlayerEX.id("main"), WizardEXMenu::class.java)
    }
}