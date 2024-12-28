package com.bibireden.wizardex.ui.menu

import com.bibireden.data_attributes.api.attribute.EntityAttributeSupplier
import com.bibireden.playerex.ext.id
import com.bibireden.playerex.ui.components.*
import com.bibireden.playerex.ui.components.labels.AttributeLabelComponent
import com.bibireden.playerex.ui.helper.InputHelper
import com.bibireden.wizardex.WizardEX
import com.bibireden.wizardex.attributes.WizardEXAttributes
import io.wispforest.owo.ui.component.Components

import io.wispforest.owo.ui.container.Containers
import io.wispforest.owo.ui.container.FlowLayout
import io.wispforest.owo.ui.core.*
import net.minecraft.network.chat.Component

class WizardEXMenu : MenuComponent(algorithm = Algorithm.VERTICAL) {
    private val CRITICALS: List<EntityAttributeSupplier> = listOf(
        EntityAttributeSupplier(WizardEXAttributes.CRITICAL_DAMAGE.id),
        EntityAttributeSupplier(WizardEXAttributes.CRITICAL_CHANCE.id)
    )

    override fun build(screenRoot: FlowLayout) {
        val player = this.client?.player ?: return

        child(
            Containers.verticalScroll(
                Sizing.fill(45),
                Sizing.fill(70),
                Containers.verticalFlow(Sizing.fill(100), Sizing.content(6)).apply {
                    verticalAlignment(VerticalAlignment.CENTER)
                    gap(10)
                    padding(Insets.right(5))

                    child(Containers.horizontalFlow(Sizing.fill(100), Sizing.content(4)).apply {
                        verticalAlignment(VerticalAlignment.CENTER)

                        child(Components.label(Component.translatable("wizardex.ui.category.spell_schools")).color(Color.ofArgb(0x32FFFFFF)))
                        child(
                            Components.textBox(Sizing.fixed(27))
                                .text("1")
                                .also {
                                    it.setMaxLength(3)
                                    it.setFilter(InputHelper::isUIntInput)
                                }
                                .verticalSizing(Sizing.fixed(15))
                                .positioning(Positioning.relative(100, 50))
                                .id("input")
                        )
                    })

                    child(
                        Components.box(Sizing.fill(100), Sizing.fixed(2))
                            .color(Color.ofArgb(0x36FFFFFF))
                    )

                    verticalAlignment(VerticalAlignment.CENTER)
                    gap(5)
                    padding(Insets.right(5))

                    for (school in WizardEX.SCHOOLS) {
                        child(AttributeComponent(school, player, playerComponent!!))
                        child(Components.box(Sizing.fill(100), Sizing.fixed(2)).fill(true).color(Color.ofArgb(0x10FFFFFF)))
                    }
                }.id("wizardry")
            )
                .positioning(Positioning.relative(0, 50))
        )

        child(Containers.verticalScroll(
            Sizing.fill(28),
            Sizing.fill(50),
            Containers.verticalFlow(Sizing.fill(100), Sizing.content()).apply {
                verticalAlignment(VerticalAlignment.CENTER)
                child(
                    AttributeListComponent("wizardex.ui.main.categories.criticals", player, CRITICALS)
                        .horizontalSizing(Sizing.fill(100))
                )
                padding(Insets.right(5))
                gap(8)
            }
        )
            .positioning(Positioning.relative(100, 50))
        )

        child(Containers.verticalScroll(
            Sizing.fill(26),
            Sizing.fill(50),
            Containers.verticalFlow(Sizing.fill(100), Sizing.content()).apply {
                verticalAlignment(VerticalAlignment.CENTER)
                child(
                    AttributeListComponent("wizardex.ui.main.categories.schools", player, WizardEX.SPELL_POWER_SCHOOLS.map { EntityAttributeSupplier(it.id) })
                        .horizontalSizing(Sizing.fill(100))
                )
                padding(Insets.right(5))
                gap(8)
            }
            ).positioning(Positioning.relative(62, 50))
        )

        onAttributeUpdated.subscribe { _, _ ->
            forEachDescendant { descendant ->
                when (descendant) {
                    is AttributeComponent -> descendant.refresh()
                    is AttributeLabelComponent -> descendant.refresh()
                    is AttributeListEntryComponent -> descendant.refresh()
                }
            }
        }
    }
}