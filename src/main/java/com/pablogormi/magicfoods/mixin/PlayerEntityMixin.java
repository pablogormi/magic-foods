package com.pablogormi.magicfoods.mixin;

import com.pablogormi.magicfoods.entity.effect.ModStatusEffects;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("RETURN"), method = "eatFood")
    protected void onEatFood(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        this.addStatusEffect(new StatusEffectInstance(ModStatusEffects.FLYING_STATUS_EFFECT, 5*20));
    }



}
