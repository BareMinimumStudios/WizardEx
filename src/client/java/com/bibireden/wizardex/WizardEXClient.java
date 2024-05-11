package com.bibireden.wizardex;

import com.github.clevernucleus.playerex.api.client.PageRegistry;
import com.github.clevernucleus.playerex.client.factory.NetworkFactoryClient;
import com.github.clevernucleus.playerex.factory.NetworkFactory;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class WizardEXClient implements ClientModInitializer {
	private static final Identifier magicIcon = new Identifier(WizardEX.ID, "textures/gui/staff.png");

	@Override
	public void onInitializeClient() {
		WizardEX.LOGGER.debug("Initializing client!");
		ClientLoginNetworking.registerGlobalReceiver(NetworkFactory.CONFIG, NetworkFactoryClient::loginQueryReceived);
		ClientPlayNetworking.registerGlobalReceiver(NetworkFactory.NOTIFY, NetworkFactoryClient::notifiedLevelUp);

		PageRegistry.registerPage(new Identifier(WizardEX.ID, "magic"), magicIcon, Text.translatable("wizardex.gui.page.title"));
		PageRegistry.registerLayer(new Identifier(WizardEX.ID, "magic"), MagicPageLayer::new);
	}
}
