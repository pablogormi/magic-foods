package com.pablogormi.magicfoods.block;

import com.mojang.serialization.MapCodec;
import com.pablogormi.magicfoods.entity.BridgeBallEntity;
import com.pablogormi.magicfoods.util.BridgeUtil;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BridgeExtenderBlock extends HorizontalFacingBlock {
    public static final MapCodec<BridgeExtenderBlock> CODEC = BridgeExtenderBlock.createCodec(BridgeExtenderBlock::new);

    public BridgeExtenderBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return CODEC;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (entity instanceof BoatEntity be && be.hasPlayerRider()) {
            //Lengthen bridge
            Direction dir = state.get(FACING);
            BridgeUtil.lengthenBridge(world, dir, pos, BridgeBallEntity.DISTANCE);
            //Replace current extender with Packed Ice
            world.setBlockState(pos, Blocks.PACKED_ICE.getDefaultState());
        }
    }
}
