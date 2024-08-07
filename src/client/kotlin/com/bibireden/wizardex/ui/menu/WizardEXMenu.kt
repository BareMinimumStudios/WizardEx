package com.bibireden.wizardex.ui.menu

import com.bibireden.data_attributes.api.attribute.EntityAttributeSupplier
import com.bibireden.playerex.ext.id
import com.bibireden.playerex.ui.components.*
import com.bibireden.playerex.ui.components.buttons.AttributeButtonComponent
import com.bibireden.playerex.ui.components.labels.AttributeLabelComponent
import com.bibireden.playerex.ui.util.FormattingPredicates
import com.bibireden.wizardex.WizardEX
import com.bibireden.wizardex.attributes.WizardEXAttributes
import io.wispforest.owo.ui.component.Components

import io.wispforest.owo.ui.container.Containers
import io.wispforest.owo.ui.container.FlowLayout
import io.wispforest.owo.ui.core.*
import net.minecraft.network.chat.Component

class WizardEXMenu : MenuComponent(algorithm = Algorithm.VERTICAL) {
    private val CRITICALS: List<Pair<EntityAttributeSupplier, FormattingPredicate>> = listOf(
        EntityAttributeSupplier(WizardEXAttributes.CRITICAL_DAMAGE.id) to { it.toInt().toString() },
        EntityAttributeSupplier(WizardEXAttributes.CRITICAL_CHANCE.id) to { "+${it.toInt()}%" }
    )

    override fun build(screenRoot: FlowLayout) {
        val player = this.client?.player ?: return

        child(
            Containers.verticalScroll(
                Sizing.fill(100),
                Sizing.fill(60),
                Containers.verticalFlow(Sizing.fill(100), Sizing.content()).apply {
                    child(Containers.horizontalFlow(Sizing.fill(100), Sizing.content(2)).apply {
                        child(Components.label(Component.translatable("wizardex.ui.category.spell_schools")))
                        child(
                            Components.textBox(Sizing.fixed(27))
                                .also {
                                    it.setMaxLength(4)
                                    it.setFilter { s -> s.toUIntOrNull() != null }
                                }
                                .text("1")
                                .verticalSizing(Sizing.fixed(10))
                                .positioning(Positioning.relative(100, 50))
                                .id("input")
                        )
                    })
                    child(Components.box(Sizing.fill(100), Sizing.fixed(2)))
                    verticalAlignment(VerticalAlignment.TOP)
                    gap(5)
                    padding(Insets.right(5))
                    children(WizardEX.SCHOOLS.map { AttributeComponent(it, player, playerComponent!!) })
                }.id("wizardry")
            )
                .positioning(Positioning.relative(50, 50))
        )
        child(Containers.verticalScroll(
            Sizing.fill(100),
            Sizing.fill(40),
            Containers.verticalFlow(Sizing.fill(100), Sizing.content()).apply {
                child(AttributeListComponent("wizardex.ui.main.categories.criticals", player, CRITICALS))
                padding(Insets.right(5))
                gap(8)
            }
        ))

        onAttributeUpdated.subscribe { _, _ ->
            forEachDescendant { descendant ->
                if (descendant is AttributeComponent) descendant.refresh()
                if (descendant is AttributeButtonComponent) descendant.refresh()
                if (descendant is AttributeLabelComponent) descendant.refresh()
            }
        }
    }
}