package com.pablogormi.magicfoods.block;

import com.pablogormi.magicfoods.block.entity.EndRelayBlockEntity;
import com.pablogormi.magicfoods.block.entity.ModBlockEntities;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.IntProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.listener.GameEventListener;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.Nullable;

public class EndRelayBlock extends BlockWithEntity {



    public EndRelayBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new EndRelayBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return super.getTicker(world, state, type);
    }

    @Override
    public <T extends BlockEntity> GameEventListener getGameEventListener(ServerWorld world, T blockEntity) {
        return super.getGameEventListener(world, blockEntity);
    }


    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof EndRelayBlockEntity be) {
            BlockPos p1 = be.getSavedPos();
            if (p1 != null && !world.isClient())
            player.requestTeleport(p1.getX()+0.5, p1.getY()+1, p1.getZ()+0.5);
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    private void dropCompass(World world, BlockPos pos, EndRelayBlockEntity be, PlayerEntity p) {
        //CompassItem item = (CompassItem) Items.COMPASS;
        //TODO: Añadir nbt data y comprobar si lo de abajo funciona
        ItemStack stack = new ItemStack(Items.COMPASS);
        if (!world.isClient()) {
            ServerWorld server = (ServerWorld) world;
            server.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack));
        }

    }
}
