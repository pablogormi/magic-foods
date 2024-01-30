package com.pablogormi.magicfoods.entity.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;

public class FlyingStatusEffect extends StatusEffect {
    protected FlyingStatusEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public void onApplied(LivingEntity entity, int amplifier) {
        if (entity instanceof PlayerEntity pe) {
            pe.getAbilities().allowFlying = true;
            pe.getAbilities().flying=true;

            pe.sendAbilitiesUpdate();
        }
        super.onApplied(entity, amplifier);
    }
}
