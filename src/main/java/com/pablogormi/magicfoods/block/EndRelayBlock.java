package com.pablogormi.magicfoods.block;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;
import com.pablogormi.magicfoods.MagicFoods;
import com.pablogormi.magicfoods.block.entity.EndRelayBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CompassItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.listener.GameEventListener;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public class EndRelayBlock extends BlockWithEntity {
    public static final MapCodec<EndRelayBlock> CODEC = EndRelayBlock.createCodec(EndRelayBlock::new);
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final IntProperty CHARGES = Properties.CHARGES;

    public EndRelayBlock(Settings settings) {
        super(settings);
        this.setDefaultState(((BlockState)this.stateManager.getDefaultState()).with(CHARGES, 0));
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
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
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(CHARGES);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            if (hand == Hand.MAIN_HAND && player.getStackInHand(hand).isOf(Items.END_CRYSTAL)) { //charging behaviour
                if (canCharge(state)) {
                    charge(player, world, pos, state);
                    if (!player.getAbilities().creativeMode) {
                        player.getStackInHand(hand).decrement(1);
                    }
                }
            } else if (state.get(CHARGES) > 0 && world.getBlockEntity(pos) instanceof EndRelayBlockEntity be) {
                //player.sendMessage(Text.literal("Interacted with block!"), false);
                BlockPos p1 = be.getSavedPos();
                world.setBlockState(pos, state.with(CHARGES, state.get(CHARGES) - 1)); //Update charges
                if (p1 != null) {
                    world.playSound(null, p1.getX() + 0.5, p1.getY() + 1, p1.getZ() + 0.5, SoundEvents.ENTITY_PLAYER_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f);
                    if (player.hasVehicle())
                        ((ServerPlayerEntity) player).requestTeleportAndDismount(p1.getX() + 0.5, p1.getY() + 1, p1.getZ() + 0.5);
                    else
                        ((ServerPlayerEntity) player).requestTeleport(p1.getX() + 0.5, p1.getY() + 1, p1.getZ() + 0.5);
                    player.onLanding();
                }
            } else {
                ((ServerPlayerEntity) player).sendMessage(Text.translatable("text."+MagicFoods.NAMESPACE + ".teleport.not_possible"), true);
            }
        }
        return ActionResult.SUCCESS;
    }

    public boolean canCharge(BlockState state) {
        return state.get(CHARGES) <= 0;
    }
    public static void charge(@Nullable Entity charger, World world, BlockPos pos, BlockState state) {
        BlockState blockState = (BlockState)state.with(CHARGES, state.get(CHARGES) + 4); // One End Crystal charges the block completely.
        world.setBlockState(pos, blockState, Block.NOTIFY_ALL);
        world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(charger, blockState));
        world.playSound(null, (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, SoundCategory.BLOCKS, 1.0f, 1.0f);
    }


    private void dropCompass(World world, BlockPos pos, EndRelayBlockEntity be) {
        ItemStack stack = new ItemStack(Items.COMPASS, 1);
        if (!world.isClient()) {
            NbtCompound compassNbt = new NbtCompound();
            NbtCompound lodPos = new NbtCompound();
            lodPos.putInt("X", be.getSavedPos().getX());
            lodPos.putInt("Y", be.getSavedPos().getY());
            lodPos.putInt("Z", be.getSavedPos().getZ());
            compassNbt.put(CompassItem.LODESTONE_POS_KEY, lodPos);
            compassNbt.putBoolean(CompassItem.LODESTONE_TRACKED_KEY, true);
            World.CODEC.encodeStart(NbtOps.INSTANCE, world.getRegistryKey()).resultOrPartial(LOGGER::error).ifPresent(nbtElement -> compassNbt.put(CompassItem.LODESTONE_DIMENSION_KEY, nbtElement));
            stack.setNbt(compassNbt);
            DefaultedList<ItemStack> list = DefaultedList.of();
            list.add(stack);
            LOGGER.info("Spawning compass");
            ItemScatterer.spawn(world, pos, list);
        }

    }


    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient() && world.getBlockEntity(pos) instanceof EndRelayBlockEntity be) {
            dropCompass(world, pos, be);
        }
        return super.onBreak(world, pos, state, player);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
