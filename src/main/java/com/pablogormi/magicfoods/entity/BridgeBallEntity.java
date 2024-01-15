package com.pablogormi.magicfoods.entity;

import com.pablogormi.magicfoods.MagicFoods;
import com.pablogormi.magicfoods.util.BridgeUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class BridgeBallEntity extends ThrownItemEntity {

    public static final int DISTANCE = 50;

    public BridgeBallEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public BridgeBallEntity(EntityType<? extends ThrownItemEntity> entityType, double d, double e, double f, World world) {
        super(entityType, d, e, f, world);
    }

    public BridgeBallEntity(EntityType<? extends ThrownItemEntity> entityType, LivingEntity livingEntity, World world) {
        super(entityType, livingEntity, world);
    }

    public BridgeBallEntity(World world, LivingEntity owner) {
        super(MagicFoods.BRIDGE_BALL_ENTITY_TYPE, owner, world);
    }


    @Override
    protected void onCollision(HitResult hitResult) {
        Direction dir = this.getMovementDirection();

        if (dir.getAxis().isVertical()) { //Do not create upwards bridge
            dir = Direction.getEntityFacingOrder(this)[1];
        }
        if (dir.getAxis()== Direction.Axis.X) { //lazy fix
            dir = dir.getOpposite();
        }

        World world = this.getEntityWorld();
        if (this.getOwner() instanceof PlayerEntity user) {
            if (!world.isClient) {
                BridgeUtil.lengthenBridge(world, dir, this.getBlockPos(), user, DISTANCE, true);
            }
        }
        this.discard();
    }

    @Override
    protected Item getDefaultItem() {
        return MagicFoods.BRIDGE_BALL_ITEM;
    }


}
