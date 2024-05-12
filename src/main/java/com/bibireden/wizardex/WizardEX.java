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
	public static final Identifier ALL_SCHOOLS = new Identifier(ID, "spell_power_all");

	// Indvidual Spell Levels
	public static final Identifier FIRE_SCHOOL = new Identifier(ID, "spell_power_fire");
	public static final Identifier FROST_SCHOOL = new Identifier(ID, "spell_power_frost");
	public static final Identifier LIGHTNING_SCHOOL = new Identifier(ID, "spell_power_lightning");
	public static final Identifier SOUL_SCHOOL = new Identifier(ID, "spell_power_soul");
	public static final Identifier ARCANE_SCHOOL = new Identifier(ID, "spell_power_arcane");
	public static Identifier HEALING_SCHOOL = new Identifier(ID, "spell_power_healing");

	public static final Identifier CRIT_CHANCE_SP = new Identifier("spell_power:critical_chance");
	public static final Identifier CRIT_DAMAGE_SP = new Identifier("spell_power:critical_damage");

	@Override
	public void onInitialize() {
		LOGGER.debug("WizardEX is preparing magic!");
		registerRefundConditions();
	}

	private void registerRefundConditions() {
		ExAPI.registerRefundCondition((data, player) -> {
			var attribute = EntityAttributeSupplier.of(FIRE_SCHOOL);
			return DataAttributesAPI.ifPresent(player, attribute, 0.0D, value -> data.get(attribute));
		});
		ExAPI.registerRefundCondition((data, player) -> {
			var attribute = EntityAttributeSupplier.of(FROST_SCHOOL);
			return DataAttributesAPI.ifPresent(player, attribute, 0.0D, value -> data.get(attribute));
		});
		ExAPI.registerRefundCondition((data, player) -> {
			var attribute = EntityAttributeSupplier.of(ARCANE_SCHOOL);
			return DataAttributesAPI.ifPresent(player, attribute, 0.0D, value -> data.get(attribute));
		});
	}
}
