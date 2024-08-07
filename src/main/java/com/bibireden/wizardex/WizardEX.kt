package com.bibireden.wizardex

import com.bibireden.data_attributes.api.attribute.StackingBehavior
import com.bibireden.data_attributes.api.factory.DefaultAttributeFactory
import com.bibireden.data_attributes.config.functions.AttributeFunction
import com.bibireden.data_attributes.data.EntityTypeData
import com.bibireden.playerex.api.PlayerEXAPI
import com.bibireden.playerex.api.attribute.PlayerEXAttributes
import com.bibireden.playerex.ext.id
import com.bibireden.wizardex.attributes.WizardEXAttributes
import net.fabricmc.api.ModInitializer
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.ai.attributes.RangedAttribute
import net.spell_power.SpellPowerMod
import net.spell_power.api.SpellPower
import net.spell_power.api.SpellSchool
import net.spell_power.api.SpellSchools
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KCallable
import kotlin.reflect.full.memberProperties

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
        SPELL_POWER_SCHOOLS.forEach { school -> PlayerEXAPI.registerRefundCondition { data, player -> data.get(school.attribute) } }
    }

    override fun onInitialize() {
        LOGGER.debug("WizardEX is preparing magic!")

        registerRefundConditions()

        DefaultAttributeFactory.registerFunctions(mapOf(
            PlayerEXAttributes.DEXTERITY.id to listOf(
                AttributeFunction(WizardEXAttributes.CRITICAL_DAMAGE.id, StackingBehavior.Add, 0.5)
            ),
            PlayerEXAttributes.INTELLIGENCE.id to SCHOOLS.map { AttributeFunction(it.id, StackingBehavior.Add, 0.25) },
            PlayerEXAttributes.LUCKINESS.id to listOf(
                AttributeFunction(WizardEXAttributes.CRITICAL_CHANCE.id, StackingBehavior.Add, 0.25)
            ),
            WizardEXAttributes.CRITICAL_CHANCE.id to listOf(
                AttributeFunction(ResourceLocation.tryBuild("spell_power", "critical_chance")!!, StackingBehavior.Add, 1.0)
            ),
            WizardEXAttributes.CRITICAL_DAMAGE.id to listOf(
                AttributeFunction(ResourceLocation.tryBuild("spell_power", "critical_damage")!!, StackingBehavior.Add, 1.0)
            ),

            WizardEXAttributes.Schools.FIRE.id to listOf(
                AttributeFunction(SpellSchools.FIRE.id, StackingBehavior.Add, 0.5)
            ),
            WizardEXAttributes.Schools.FROST.id to listOf(
                AttributeFunction(SpellSchools.FROST.id, StackingBehavior.Add, 0.5)
            ),
            WizardEXAttributes.Schools.LIGHTNING.id to listOf(
                AttributeFunction(SpellSchools.LIGHTNING.id, StackingBehavior.Add, 0.5)
            ),
            WizardEXAttributes.Schools.HEALING.id to listOf(
                AttributeFunction(SpellSchools.HEALING.id, StackingBehavior.Add, 0.5)
            ),
            WizardEXAttributes.Schools.ARCANE.id to listOf(
                AttributeFunction(SpellSchools.ARCANE.id, StackingBehavior.Add, 0.5)
            ),
            WizardEXAttributes.Schools.SOUL.id to listOf(
                AttributeFunction(SpellSchools.SOUL.id, StackingBehavior.Add, 0.5)
            ),
        ))

        DefaultAttributeFactory.registerEntityTypes(mapOf(
            ResourceLocation.tryBuild("minecraft", "player")!! to EntityTypeData(
                mutableMapOf(WizardEXAttributes.CRITICAL_CHANCE.id to 0.0, WizardEXAttributes.CRITICAL_DAMAGE.id to 0.0)
                    .also { it.putAll(SCHOOLS.map { s -> s.id to 0.0 }) }
            )
        ))
    }
}