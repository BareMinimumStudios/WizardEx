package com.bibireden.wizardex

import com.bibireden.playerex.api.PlayerEXAPI
import com.bibireden.wizardex.attributes.WizardEXAttributes
import net.fabricmc.api.ModInitializer
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.ai.attributes.RangedAttribute
import net.spell_power.api.SpellSchool
import net.spell_power.api.SpellSchools
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object WizardEX : ModInitializer {
    const val MOD_ID: String = "wizardex"

    @JvmField
    val SPELL_POWER_SCHOOLS: List<SpellSchool> = listOf(
        SpellSchools.FIRE,
        SpellSchools.FROST,
        SpellSchools.SOUL,
        SpellSchools.ARCANE,
        SpellSchools.HEALING,
        SpellSchools.LIGHTNING
    )
    @JvmField
    val SCHOOLS: List<RangedAttribute> = listOf(
        WizardEXAttributes.Schools.FIRE,
        WizardEXAttributes.Schools.FROST,
        WizardEXAttributes.Schools.SOUL,
        WizardEXAttributes.Schools.ARCANE,
        WizardEXAttributes.Schools.HEALING,
        WizardEXAttributes.Schools.LIGHTNING,
    )

    @JvmField
    val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)

    @JvmStatic
    fun id(str: String): ResourceLocation = ResourceLocation.tryBuild(MOD_ID, str)!!

    private fun registerRefundConditions() {
        SPELL_POWER_SCHOOLS.forEach { school -> PlayerEXAPI.registerRefundCondition { data, _ -> data.get(school.attribute) } }
    }

    override fun onInitialize() {
        LOGGER.debug("WizardEX is preparing magic!")
        registerRefundConditions()
    }
}