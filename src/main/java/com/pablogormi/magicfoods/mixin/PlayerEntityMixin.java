package com.pablogormi.magicfoods.mixin;

import com.pablogormi.magicfoods.entity.effect.ModStatusEffects;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    @Final
    @Shadow
    private PlayerAbilities abilities;
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("RETURN"), method = "getAbilities")
    protected void onGetAbilities(CallbackInfoReturnable<PlayerAbilities> cir) {
        if (this.getWorld().isClient()) {
            this.abilities.allowFlying = this.hasStatusEffect(ModStatusEffects.FLYING_STATUS_EFFECT) || this.abilities.creativeMode;
            if (!this.abilities.creativeMode && !this.hasStatusEffect(ModStatusEffects.FLYING_STATUS_EFFECT))
                this.abilities.flying = false;
        }
    }



}
