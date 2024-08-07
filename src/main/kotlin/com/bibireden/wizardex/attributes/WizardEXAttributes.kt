package com.bibireden.wizardex.attributes

import com.bibireden.wizardex.WizardEX
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.ai.attributes.RangedAttribute

object WizardEXAttributes {
    @JvmField
    val CRITICAL_CHANCE = register("critical_chance")
    @JvmField
    val CRITICAL_DAMAGE = register("critical_damage")

    object Schools {
        @JvmField
        val FIRE = register("schools.fire")
        @JvmField
        val FROST = register("schools.frost")
        @JvmField
        val SOUL = register("schools.soul")
        @JvmField
        val ARCANE = register("schools.arcane")
        @JvmField
        val HEALING = register("schools.healing")
        @JvmField
        val LIGHTNING = register("schools.lightning")
    }

    private fun register(id: String, default: Double = 0.0, min: Double = 0.0, max: Double = 100.0): RangedAttribute {
        return Registry.register(BuiltInRegistries.ATTRIBUTE, WizardEX.id(id), RangedAttribute("attribute.name.wizardex.$id", 0.0, 0.0, 100.0))
    }
}