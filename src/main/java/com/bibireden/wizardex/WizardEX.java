package com.bibireden.wizardex;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.clevernucleus.dataattributes_dc.api.DataAttributesAPI;
import com.github.clevernucleus.playerex.api.EntityAttributeSupplier;
import com.github.clevernucleus.playerex.api.ExAPI;


public class WizardEX implements ModInitializer {
	public static final String ID = "wizardex";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);


	// Overall spell stat gain from int levels
	public static final Identifier spell_power_all = new Identifier(ID, "spell_power_all");

	// Indvidual Spell Levels
	public static final Identifier spell_power_fire = new Identifier(ID, "spell_power_fire");
	public static final Identifier spell_power_frost = new Identifier(ID, "spell_power_frost");
	public static final Identifier spell_power_lightning = new Identifier(ID, "spell_power_lightning");
	public static final Identifier spell_power_soul = new Identifier(ID, "spell_power_soul");
	public static final Identifier spell_power_arcane = new Identifier(ID, "spell_power_arcane");
	public static Identifier spell_power_healing = new Identifier(ID, "spell_power_healing");

	public static final Identifier spell_power_crit_chance = new Identifier("spell_power:critical_chance");
	public static final Identifier spell_power_crit_damage = new Identifier("spell_power:critical_damage");

	@Override
	public void onInitialize() {
		LOGGER.debug("WizardEX is preparing magic!");
		registerRefundConditions();
	}

	private void registerRefundConditions() {
		ExAPI.registerRefundCondition((data, player) -> {
			var attribute = EntityAttributeSupplier.of(spell_power_fire);
			return DataAttributesAPI.ifPresent(player, attribute, 0.0D, value -> data.get(attribute));
		});
		ExAPI.registerRefundCondition((data, player) -> {
			var attribute = EntityAttributeSupplier.of(spell_power_frost);
			return DataAttributesAPI.ifPresent(player, attribute, 0.0D, value -> data.get(attribute));
		});
		ExAPI.registerRefundCondition((data, player) -> {
			var attribute = EntityAttributeSupplier.of(spell_power_arcane);
			return DataAttributesAPI.ifPresent(player, attribute, 0.0D, value -> data.get(attribute));
		});
	}
}
