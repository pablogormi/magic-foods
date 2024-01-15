package com.pablogormi.magicfoods.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.math.BlockPos;

public class EndRelayBlockEntity extends BlockEntity {

    private BlockPos pos;

    public EndRelayBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.END_RELAY, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        if (pos != null)
            nbt.put("position", NbtHelper.fromBlockPos(pos));
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.pos = NbtHelper.toBlockPos(nbt.getCompound("position"));
        super.readNbt(nbt);
    }

    public BlockPos getSavedPos() {
        return this.pos;
    }
}
