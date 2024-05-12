package com.bibireden.wizardex;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.Consumer;

import com.github.clevernucleus.dataattributes_dc.api.DataAttributesAPI;
import com.github.clevernucleus.dataattributes_dc.api.attribute.IEntityAttribute;
import com.github.clevernucleus.playerex.PlayerEx;
import com.github.clevernucleus.playerex.api.EntityAttributeSupplier;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.PacketType;
import com.github.clevernucleus.playerex.api.PlayerData;
import com.github.clevernucleus.playerex.api.client.ClientUtil;
import com.github.clevernucleus.playerex.api.client.PageLayer;
import com.github.clevernucleus.playerex.api.client.RenderComponent;
import com.github.clevernucleus.playerex.client.gui.widget.ScreenButtonWidget;
import com.google.common.collect.ImmutableList;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.spell_power.api.SpellPower;
import net.spell_power.api.SpellSchools;

@Environment(EnvType.CLIENT)
public class MagicPageLayer extends PageLayer {
    private static Supplier<Float> scaleX = () -> ExAPI.getConfig().textScaleX();
    private static Supplier<Float> scaleY = () -> ExAPI.getConfig().textScaleY();
    private static float scaleZ = 0.75F;
    private static int X_BASE_OFFSET = 10;

    private PlayerData playerData;

    private static final List<RenderComponent> COMPONENTS = new ArrayList<>();
    public static final Identifier schools = new Identifier(WizardEX.ID, "textures/gui/schools.png");
    public static final Identifier orb = new Identifier(WizardEX.ID, "textures/gui/orb.png");

    private static final List<Identifier> BUTTON_KEYS = ImmutableList.of(
            WizardEX.FIRE_SCHOOL,
            WizardEX.FROST_SCHOOL,
            WizardEX.ARCANE_SCHOOL,
            WizardEX.LIGHTNING_SCHOOL,
            WizardEX.SOUL_SCHOOL,
            WizardEX.HEALING_SCHOOL
    );

    public MagicPageLayer(HandledScreen<?> parent, ScreenHandler handler, PlayerInventory inventory, Text title) {
        super(parent, handler, inventory, title);
    }

    private boolean canRefund() {
        return this.playerData.refundPoints() > 0;
    }

    private void forEachScreenButton(Consumer<ScreenButtonWidget> consumer) {
        this.children().stream().filter(e -> e instanceof ScreenButtonWidget)
                .forEach(e -> consumer.accept((ScreenButtonWidget) e));
    }

    private void buttonPressed(ButtonWidget buttonIn) {
        ScreenButtonWidget button = (ScreenButtonWidget) buttonIn;
        EntityAttributeSupplier attribute = EntityAttributeSupplier.of(button.key());
        DataAttributesAPI.ifPresent(this.client.player, attribute, (Object) null, amount -> {
            double value = this.canRefund() ? -1.0D : 1.0D;
            ClientUtil.modifyAttributes(this.canRefund() ? PacketType.REFUND : PacketType.SKILL,
                    c -> c.accept(attribute, value));
            this.client.player.playSound(PlayerEx.SP_SPEND_SOUND, SoundCategory.NEUTRAL,
                    ExAPI.getConfig().skillUpVolume(), 1.5F);
            return (Object) null;
        });
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        MatrixStack matrices = context.getMatrices();

        matrices.push();
        matrices.scale(scaleX.get(), scaleY.get(), scaleZ);

        COMPONENTS.forEach(component -> component.renderText(this.client.player, context, this.textRenderer, this.x,
                this.y, scaleX.get(), scaleY.get()));

        // this.textRenderer.draw(matrices, Text.of("Put character level points"),
        // (this.x + 15) /
        // scaleX.get(), (this.y + 20) / scaleY.get(), 4210752);
        // this.textRenderer.draw(matrices, Text.of("into specific schools of magic"),
        // (this.x + 15) /
        // scaleX.get(), (this.y + 27) / scaleY.get(), 4210752);

        context.drawText(this.textRenderer, Text.translatable("wizardex.gui.page.tooltip.fire_header"),
                (int) ((this.x + X_BASE_OFFSET + 22) / scaleX.get()), (int) ((this.y + 40) / scaleY.get()),
                4210752, false);
        context.drawText(this.textRenderer, Text.translatable("wizardex.gui.page.tooltip.frost_header"),
                (int) ((this.x + X_BASE_OFFSET + 22) / scaleX.get()), (int) ((this.y + 60) / scaleY.get()),
                4210752, false);
        context.drawText(this.textRenderer, Text.translatable("wizardex.gui.page.tooltip.arcane_header"),
                (int) ((this.x + X_BASE_OFFSET + 22) / scaleX.get()),
                (int) ((this.y + 80) / scaleY.get()), 4210752, false);

        // ->
        // SECOND COLUMN
        // ->

        context.drawText(this.textRenderer,  Text.translatable("wizardex.gui.page.tooltip.lightning_header"),
                (int) ((this.x + X_BASE_OFFSET + 100) / scaleX.get()),
                (int) ((this.y + 40) / scaleY.get()), 4210752, false);

        context.drawText(this.textRenderer,  Text.translatable("wizardex.gui.page.tooltip.soul_header"),
                (int) ((this.x + X_BASE_OFFSET + 100) / scaleX.get()),
                (int) ((this.y + 60) / scaleY.get()), 4210752, false);

        context.drawText(this.textRenderer, Text.translatable("wizardex.gui.page.tooltip.healing_header"),
                (int) ((this.x + X_BASE_OFFSET + 100) / scaleX.get()),
                (int) ((this.y + 80) / scaleY.get()), 4210752, false);


        // BOTTOM

        context.drawText(this.textRenderer, Text.translatable("wizardex.gui.page.tooltip.crit_chance_header"),
                (int) ((this.x + X_BASE_OFFSET) / scaleX.get()),
                (int) ((this.y + 130) / scaleY.get()), 4210752, false);

        context.drawText(this.textRenderer, Text.translatable("wizardex.gui.page.tooltip.crit_damage_header"),
                (int) ((this.x + X_BASE_OFFSET) / scaleX.get()),
                (int) ((this.y + 145) / scaleY.get()), 4210752, false);

        matrices.pop();

        COMPONENTS.forEach(component -> component.drawTooltip(this.client.player, context::drawTooltip, context,
                this.textRenderer, this.x, this.y, mouseX, mouseY, scaleX.get(), scaleY.get()));
    }

    @Override
    public void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        // Set texture to get icons from
        // MUST BE 256x256 !!!

        // Orb Icon
        context.drawTexture(orb, this.x + X_BASE_OFFSET, this.y + 24, 6, 6, 0, 0, 16, 16, 16, 16);

        // Fire Icon
        context.drawTexture(schools, this.x + X_BASE_OFFSET, this.y + 38, 0, 0, 16, 16);
        // Frost Icon
        context.drawTexture(schools, this.x + X_BASE_OFFSET, this.y + 58, 16, 0, 16, 16);
        // Arcane Icon
        context.drawTexture(schools, this.x + X_BASE_OFFSET, this.y + 78, 48, 0, 16, 16);

        // COLUMN 2

        // Lightning Icon
        context.drawTexture(schools, this.x + X_BASE_OFFSET + 78, this.y + 38, 32, 0, 16, 16);
        // Soul Icon
        context.drawTexture(schools, this.x + X_BASE_OFFSET + 78, this.y + 58, 80, 0, 16, 16);
        // Healing Icon
        context.drawTexture(schools, this.x + X_BASE_OFFSET + 78, this.y + 78, 64, 0, 16, 16);


        // add the button to level up each school of magic
        this.forEachScreenButton(button -> {
            Identifier key = button.key();
            Identifier lvl = new Identifier("playerex:level");
            EntityAttributeSupplier attribute = EntityAttributeSupplier.of(key);
            PlayerEntity player = this.client.player;

            DataAttributesAPI.ifPresent(player, attribute, (Object) null, value -> {
                if (BUTTON_KEYS.contains(key)) {
                    double max = ((IEntityAttribute) attribute.get()).maxValue();

                    if (key.equals(lvl)) {
                        button.active = value < max && player.experienceLevel >= ExAPI.getConfig().requiredXp(player);
                    } else {
                        double modifierValue = this.playerData.get(attribute);

                        if (this.canRefund()) {
                            button.active = modifierValue >= 1.0D;
                        } else {
                            button.active = modifierValue < max && this.playerData.skillPoints() >= 1;
                        }

                        button.alt = this.canRefund();
                    }
                }

                return (Object) null;
            });
        });
    }

    @Override
    protected void init() {
        super.init();
        this.playerData = ExAPI.PLAYER_DATA.get(this.client.player);

        var x = 69;
        var x2 = 154;

        // Fire level button
        this.addDrawableChild(createAttributeButton(x, 40, BUTTON_KEYS.get(0), this::buttonPressed));
        this.addDrawableChild(createAttributeButton(x, 60, BUTTON_KEYS.get(1), this::buttonPressed));
        this.addDrawableChild(createAttributeButton(x, 80, BUTTON_KEYS.get(2), this::buttonPressed));

        this.addDrawableChild(createAttributeButton(x2, 40, BUTTON_KEYS.get(3), this::buttonPressed));
        this.addDrawableChild(createAttributeButton(x2, 60, BUTTON_KEYS.get(4), this::buttonPressed));
        this.addDrawableChild(createAttributeButton(x2, 80, BUTTON_KEYS.get(5), this::buttonPressed));
    }

    private Tooltip createAttributeTooltip(Identifier key) {
        Identifier lvl = new Identifier("playerex:level");

        if (key.equals(lvl)) {
            int requiredXp = ExAPI.getConfig().requiredXp(this.client.player);
            int currentXp = this.client.player.experienceLevel;
            String progress = "(" + currentXp + "/" + requiredXp + ")";
            Text tooltip = (Text.translatable("playerex.gui.page.attributes.tooltip.button.level", progress))
                    .formatted(Formatting.GRAY);

            return Tooltip.of(tooltip);
        }

        Supplier<EntityAttribute> attribute = DataAttributesAPI.getAttribute(key);

        return DataAttributesAPI.ifPresent(this.client.player, attribute, null, value -> {
            Text text = Text.translatable(attribute.get().getTranslationKey());
            String type = "playerex.gui.page.attributes.tooltip.button." + (this.canRefund() ? "refund" : "skill");
            Text tooltip = (Text.translatable(type)).append(text).formatted(Formatting.GRAY);

            return Tooltip.of(tooltip);
        });
    }

    private MutableText narrationButtonTooltip(Supplier<MutableText> textSupplier) {
        return textSupplier.get();
    }

    private ScreenButtonWidget createAttributeButton(int x, int y, Identifier key,
            ButtonWidget.PressAction pressAction) {
        var button = new ScreenButtonWidget(this.parent, x, y, 204, 0, 11, 10, key, pressAction,
                this::narrationButtonTooltip);
        button.setTooltip(this.createAttributeTooltip(key));
        return button;
    }

    public static Supplier<EntityAttribute> GetSpellPowerAll() {
        Supplier<EntityAttribute> spellPowerSupplier = EntityAttributeSupplier.of(WizardEX.ALL_SCHOOLS);
        return spellPowerSupplier;
    }

    public static Supplier<EntityAttribute> GetSpellPowerFire() {
        Supplier<EntityAttribute> spellPowerSupplier = EntityAttributeSupplier.of(WizardEX.FIRE_SCHOOL);
        return spellPowerSupplier;
    }

    public static Supplier<EntityAttribute> GetSpellPowerFrost() {
        Supplier<EntityAttribute> spellPowerSupplier = EntityAttributeSupplier.of(WizardEX.FROST_SCHOOL);
        return spellPowerSupplier;
    }

    public static Supplier<EntityAttribute> GetSpellPowerLightning() {
        Supplier<EntityAttribute> spellPowerSupplier = EntityAttributeSupplier.of(WizardEX.LIGHTNING_SCHOOL);
        return spellPowerSupplier;
    }

    public static Supplier<EntityAttribute> GetSpellPowerHealing() {
        Supplier<EntityAttribute> spellPowerSupplier = EntityAttributeSupplier.of(WizardEX.HEALING_SCHOOL);
        return spellPowerSupplier;
    }

    public static Supplier<EntityAttribute> GetSpellPowerArcane() {
        Supplier<EntityAttribute> spellPowerSupplier = EntityAttributeSupplier.of(WizardEX.ARCANE_SCHOOL);
        return spellPowerSupplier;
    }

    public static Supplier<EntityAttribute> GetSpellPowerSoul() {
        Supplier<EntityAttribute> spellPowerSupplier = EntityAttributeSupplier.of(WizardEX.SOUL_SCHOOL);
        return spellPowerSupplier;
    }

    public static Supplier<EntityAttribute> GetSpellPowerCritChance() {
        Supplier<EntityAttribute> spellPowerSupplier = EntityAttributeSupplier.of(WizardEX.CRIT_CHANCE_SP);
        return spellPowerSupplier;
    }

    public static Supplier<EntityAttribute> GetSpellPowerCritDamage() {
        Supplier<EntityAttribute> spellPowerSupplier = EntityAttributeSupplier.of(WizardEX.CRIT_DAMAGE_SP);
        return spellPowerSupplier;
    }

    static {
        // SECTION: Show Skill Points/Level
        // COMPONENTS.add(RenderComponent.of(ExAPI.LEVEL, value -> {
        // return Text.translatable("playerex.gui.page.attributes.text.level",
        // Math.round(value)).formatted(Formatting.DARK_GRAY);
        // }, value -> {
        // List<Text> tooltip = new ArrayList<Text>();
        // tooltip.add((Text.translatable("playerex.gui.page.attributes.tooltip.level[0]")).formatted(Formatting.GRAY));
        // tooltip.add((Text.translatable("playerex.gui.page.attributes.tooltip.level[1]")).formatted(Formatting.GRAY));
        // tooltip.add(Text.empty());
        // tooltip.add((Text.translatable("playerex.gui.page.attributes.tooltip.level[2]",
        // ExAPI.getConfig().skillPointsPerLevelUp())).formatted(Formatting.GRAY));
        // tooltip.add(Text.translatable("playerex.gui.page.attributes.tooltip.level[0]").formatted(Formatting.GRAY));
        // tooltip.add(Text.translatable("playerex.gui.page.attributes.tooltip.level[1]").formatted(Formatting.GRAY));
        // tooltip.add(Text.empty());
        // tooltip.add(Text.translatable("playerex.gui.page.attributes.tooltip.level[2]",
        // ExAPI.getConfig().skillPointsPerLevelUp()).formatted(Formatting.GRAY));
        // return tooltip;
        // }, 80, 130));
        COMPONENTS.add(RenderComponent.of(entity -> {
            return Text.translatable("playerex.gui.page.attributes.text.skill_points",
                    ExAPI.PLAYER_DATA.get(entity).skillPoints()).formatted(Formatting.DARK_GRAY);
        }, entity -> {
            List<Text> tooltip = new ArrayList<Text>();
            tooltip.add((Text.translatable("playerex.gui.page.attributes.tooltip.skill_points[0]"))
                    .formatted(Formatting.GRAY));
            tooltip.add((Text.translatable("playerex.gui.page.attributes.tooltip.skill_points[1]"))
                    .formatted(Formatting.GRAY));
            // tooltip.add(Text.translatable("playerex.gui.page.attributes.tooltip.skill_points[0]").formatted(Formatting.GRAY));
            // tooltip.add(Text.translatable("playerex.gui.page.attributes.tooltip.skill_points[1]").formatted(Formatting.GRAY));
            return tooltip;
        }, X_BASE_OFFSET + 9, 25));

        // SECTION: WizardEX Stats
        // add Indiviual magic school stats
        // ----------------------------------
        COMPONENTS.add(RenderComponent.of(GetSpellPowerFire(), value -> {
            var intValue = value.intValue();
            return Text.of(String.valueOf(intValue));
        }, value -> {
            List<Text> tooltip = new ArrayList<Text>();
            tooltip.add((Text.translatable("wizardex.gui.page.tooltip.fire_specialization")));

            ClientUtil.appendChildrenToTooltip(tooltip, GetSpellPowerFire());
            return tooltip;
        }, 60, 40));

        COMPONENTS.add(RenderComponent.of(GetSpellPowerFrost(), value -> {
            var intValue = value.intValue();
            return Text.of(String.valueOf(intValue));
        }, value -> {
            List<Text> tooltip = new ArrayList<Text>();
            tooltip.add((Text.translatable("wizardex.gui.page.tooltip.frost_specialization")));

            ClientUtil.appendChildrenToTooltip(tooltip, GetSpellPowerFrost());
            return tooltip;
        }, 60, 60));

        COMPONENTS.add(RenderComponent.of(GetSpellPowerArcane(), value -> {
            var intValue = value.intValue();
            return Text.of(String.valueOf(intValue));
        }, value -> {
            List<Text> tooltip = new ArrayList<Text>();
            tooltip.add((Text.translatable("wizardex.gui.page.tooltip.arcane_specialization")));

            ClientUtil.appendChildrenToTooltip(tooltip, GetSpellPowerArcane());
            return tooltip;
        }, 60, 80));

        // SECOND COLUMN

        COMPONENTS.add(RenderComponent.of(GetSpellPowerLightning(), value -> {
            var intValue = value.intValue();
            return Text.of(String.valueOf(intValue));
        }, value -> {
            List<Text> tooltip = new ArrayList<Text>();
            tooltip.add((Text.translatable("wizardex.gui.page.tooltip.lightning_specialization")));

            ClientUtil.appendChildrenToTooltip(tooltip, GetSpellPowerLightning());
            return tooltip;
        }, 145, 40));

        COMPONENTS.add(RenderComponent.of(GetSpellPowerSoul(), value -> {
            var intValue = value.intValue();
            return Text.of(String.valueOf(intValue));
        }, value -> {
            List<Text> tooltip = new ArrayList<Text>();
            tooltip.add((Text.translatable("wizardex.gui.page.tooltip.soul_specialization")));

            ClientUtil.appendChildrenToTooltip(tooltip, GetSpellPowerSoul());
            return tooltip;
        }, 145, 60));

        COMPONENTS.add(RenderComponent.of(GetSpellPowerHealing(), value -> {
            var intValue = value.intValue();
            return Text.of(String.valueOf(intValue));
        }, value -> {
            List<Text> tooltip = new ArrayList<Text>();
            tooltip.add((Text.translatable("wizardex.gui.page.tooltip.healing_specialization")));

            ClientUtil.appendChildrenToTooltip(tooltip, GetSpellPowerHealing());
            return tooltip;
        }, 145, 80));

        COMPONENTS.add(RenderComponent.of(entity -> {
            Double critChance = entity.getAttributeValue(GetSpellPowerCritChance().get());
            var statFormatted = new DecimalFormat("###");
            return Text.of(statFormatted.format(critChance) + "%");
        }, entity -> {
            List<Text> tooltip = new ArrayList<Text>();
            tooltip.add((Text.translatable("wizardex.gui.page.tooltip.crit_chance")));
            tooltip.add((Text.translatable("wizardex.gui.page.tooltip.item_bonus")));
            return tooltip;
        }, X_BASE_OFFSET + 45, 130));
        COMPONENTS.add(RenderComponent.of(entity -> {
            Double critDmg = entity.getAttributeValue(GetSpellPowerCritDamage().get());
            var statFormatted = new DecimalFormat("###");
            return Text.of(statFormatted.format(critDmg) + "%");
        }, entity -> {
            List<Text> tooltip = new ArrayList<Text>();
            tooltip.add((Text.translatable("wizardex.gui.page.tooltip.crit_damage")));
            tooltip.add((Text.translatable("wizardex.gui.page.tooltip.item_bonus")));
            return tooltip;
        }, X_BASE_OFFSET + 45, 145));

        // SECTION: Spellpower Stats
        // show resulting stats from SpellPower API
        // -----------------------------------------
        COMPONENTS.add(RenderComponent.of(entity -> {
            var school = SpellPower.getSpellPower(SpellSchools.FIRE, entity);
            var value = school.baseValue();
            var statFormatted = new DecimalFormat("#.##");
            return Text.of(statFormatted.format(value));
        }, entity -> {
            List<Text> tooltip = new ArrayList<Text>();
            tooltip.add((Text.translatable("wizardex.gui.page.tooltip.fire_bonus")));
            tooltip.add((Text.translatable("wizardex.gui.page.tooltip.item_bonus")));
            return tooltip;
        }, 32, 47));
        COMPONENTS.add(RenderComponent.of(entity -> {
            var school = SpellPower.getSpellPower(SpellSchools.FROST, entity);
            var value = school.baseValue();
            var statFormatted = new DecimalFormat("###.##");
            return Text.of(statFormatted.format(value));
        }, entity -> {
            List<Text> tooltip = new ArrayList<Text>();
            tooltip.add((Text.translatable("wizardex.gui.page.tooltip.frost_bonus")));
            tooltip.add((Text.translatable("wizardex.gui.page.tooltip.item_bonus")));
            return tooltip;
        }, 32, 67));
        COMPONENTS.add(RenderComponent.of(entity -> {
            var school = SpellPower.getSpellPower(SpellSchools.ARCANE, entity);
            var value = school.baseValue();
            var statFormatted = new DecimalFormat("###.##");
            return Text.of(statFormatted.format(value));
        }, entity -> {
            List<Text> tooltip = new ArrayList<Text>();
            tooltip.add((Text.translatable("wizardex.gui.page.tooltip.arcane_bonus")));
            tooltip.add((Text.translatable("wizardex.gui.page.tooltip.item_bonus")));
            return tooltip;
        }, 32, 87));

        // 2nd Row

        COMPONENTS.add(RenderComponent.of(entity -> {
            var school = SpellPower.getSpellPower(SpellSchools.LIGHTNING, entity);
            var value = school.baseValue();
            var statFormatted = new DecimalFormat("###.##");
            return Text.of(statFormatted.format(value));
        }, entity -> {
            List<Text> tooltip = new ArrayList<Text>();
            tooltip.add((Text.translatable("wizardex.gui.page.tooltip.lightning_bonus")));
            tooltip.add((Text.translatable("wizardex.gui.page.tooltip.item_bonus")));
            return tooltip;
        }, 110, 47));
        COMPONENTS.add(RenderComponent.of(entity -> {
            var school = SpellPower.getSpellPower(SpellSchools.SOUL, entity);
            var value = school.baseValue();
            var statFormatted = new DecimalFormat("###.##");
            return Text.of(statFormatted.format(value));
        }, entity -> {
            List<Text> tooltip = new ArrayList<Text>();
            tooltip.add((Text.translatable("wizardex.gui.page.tooltip.soul_bonus")));
            tooltip.add((Text.translatable("wizardex.gui.page.tooltip.item_bonus")));
            return tooltip;
        }, 110, 67));
        COMPONENTS.add(RenderComponent.of(entity -> {
            var school = SpellPower.getSpellPower(SpellSchools.HEALING, entity);
            var value = school.baseValue();
            var statFormatted = new DecimalFormat("###.##");
            return Text.of(statFormatted.format(value));
        }, entity -> {
            List<Text> tooltip = new ArrayList<Text>();
            tooltip.add((Text.translatable("wizardex.gui.page.tooltip.healing_bonus")));
            tooltip.add((Text.translatable("wizardex.gui.page.tooltip.item_bonus")));
            return tooltip;
        }, 110, 87));
    }
}
