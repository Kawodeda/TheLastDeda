package com.kawodeda.farmandcharmfix.mixin;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.satisfy.farm_and_charm.core.effect.FarmersBlessingEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sfiomn.legendarysurvivaloverhaul.common.effects.*;

import java.util.Objects;

@Mixin({FarmersBlessingEffect.class})
public abstract class FarmersBlessingEffectMixin {

    @Shadow
    public abstract boolean isDurationEffectTick(int duration, int amplifier);

    @Inject(method = "applyEffectTick", at = @At("HEAD"), cancellable = true)
    public void applyEffectTick(LivingEntity entity, int amplifier, CallbackInfo cir)
    {
        if (this.isDurationEffectTick(((MobEffectInstance) Objects.requireNonNull(entity.getEffect((FarmersBlessingEffect)(Object)this))).getDuration(), amplifier)) {
            entity.getActiveEffectsMap().forEach((effect, instance) -> {
                if (instance.isInfiniteDuration()
                        || effect instanceof FrostbiteEffect
                        || effect instanceof ColdHungerEffect
                        || effect instanceof HeatStrokeEffect
                        || effect instanceof HeatThirstEffect
                        || effect instanceof HardFallingEffect
                        || effect instanceof VulnerabilityEffect)
                {
//                    FarmAndCharmFix.LOGGER.info("skip effect: " + effect);
                    return;
                }
                if (effect.getCategory() == MobEffectCategory.HARMFUL) {
                    entity.removeEffect(effect);
                }

            });
        }

        cir.cancel();
    }
}
