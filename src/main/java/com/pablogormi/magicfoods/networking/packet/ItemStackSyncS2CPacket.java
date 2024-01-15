package com.pablogormi.magicfoods.networking.packet;

import com.pablogormi.magicfoods.block.entity.ShelfBlockEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public class ItemStackSyncS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        int size = buf.readInt();
        DefaultedList<ItemStack> list = DefaultedList.ofSize(size, ItemStack.EMPTY);
        for(int i = 0; i < size; i++) {
            list.set(i, buf.readItemStack());
        }
        BlockPos position = buf.readBlockPos();

        if(client.world.getBlockEntity(position) instanceof ShelfBlockEntity blockEntity) {
            for (int i = 0; i < size; i++) {
                blockEntity.setStack(i, list.get(i));
            }
        }
    }
}