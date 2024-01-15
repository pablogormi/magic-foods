package com.pablogormi.magicfoods.entity.effect;

import com.pablogormi.magicfoods.MagicFoods;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModStatusEffects {
    public static final StatusEffect FLYING_STATUS_EFFECT = new FlyingStatusEffect(StatusEffectCategory.BENEFICIAL, 0x209cc1);

    public static void registerEffects() {
        Registry.register(Registries.STATUS_EFFECT, Identifier.of(MagicFoods.NAMESPACE, "flying"), FLYING_STATUS_EFFECT);
    }


}
