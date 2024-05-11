package com.bibireden.wizardex.mixin.client;

import com.bibireden.wizardex.WizardEX;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class EntryMixin {
	@Inject(at = @At("HEAD"), method = "run")
	private void init(CallbackInfo info) {
		WizardEX.LOGGER.info("Wizards do not need mixins!");
	}
}