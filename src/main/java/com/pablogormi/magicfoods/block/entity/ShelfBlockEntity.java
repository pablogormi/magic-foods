package com.pablogormi.magicfoods.block.entity;

import com.pablogormi.magicfoods.networking.ModMessages;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ShelfBlockEntity extends BlockEntity implements Inventory {
    private final DefaultedList<ItemStack> displayItems;
    public ShelfBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SHELF_BLOCK, pos, state);
        displayItems = DefaultedList.ofSize(4, ItemStack.EMPTY);
    }

    public int getOccupiedSlots() {
        return (int) displayItems.stream().filter((itemStack -> !itemStack.isEmpty())).count();
    }

    @Override
    public int size() {
        return 4;
    }

    @Override
    public boolean isEmpty() {
        return displayItems.isEmpty();
    }

    @Override
    public ItemStack getStack(int slot) {
        markDirty();
        return displayItems.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        displayItems.get(slot).decrement(amount);
        markDirty();
        return displayItems.get(slot);

    }

    @Override
    public ItemStack removeStack(int slot) {
        markDirty();
        return displayItems.remove(slot);

    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        displayItems.set(slot, stack);
        markDirty();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        displayItems.clear();
        markDirty();
    }

    public void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (hand==Hand.MAIN_HAND && !world.isClient) {
            ItemStack holding = player.getMainHandStack();
            if (!holding.isEmpty()) {
                for(int i = 0; i < this.displayItems.size(); i++) {
                    if (this.displayItems.get(i).isEmpty()) {
                        this.displayItems.set(i, holding.split(1));
                        break;
                    }
                }

            } else {

                for (int i = 0; i < this.displayItems.size(); i++) {
                    if (!this.displayItems.get(i).isEmpty()) {
                        player.setStackInHand(Hand.MAIN_HAND, this.displayItems.get(i));
                        this.displayItems.set(i, ItemStack.EMPTY);
                        break;
                    }
                }

            }
            markDirty();
            //((ServerPlayerEntity) player).networkHandler.sendPacket(BlockEntityUpdateS2CPacket.create(this));
            world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS);
        }

    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, displayItems);
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, displayItems);
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Override
    public void markDirty() {
        if(!world.isClient()) {
            PacketByteBuf data = PacketByteBufs.create();
            data.writeInt(this.displayItems.size());
            for(int i = 0; i < this.displayItems.size(); i++) {
                data.writeItemStack(this.displayItems.get(i));
            }
            data.writeBlockPos(getPos());

            for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, getPos())) {
                ServerPlayNetworking.send(player, ModMessages.ITEM_SYNC, data);
            }
        }

        super.markDirty();
    }
}
