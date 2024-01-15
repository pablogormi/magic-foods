package com.pablogormi.magicfoods.block.entity;

import com.mojang.datafixers.types.Type;
import com.pablogormi.magicfoods.MagicFoods;
import com.pablogormi.magicfoods.block.EndRelayBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlockEntities {
    public static BlockEntityType<ShelfBlockEntity> SHELF_BLOCK;
    public static BlockEntityType<EndRelayBlockEntity> END_RELAY;

    public static void register() {
        SHELF_BLOCK = Registry.register(
                Registry.BLOCK_ENTITY_TYPE,
                new Identifier(MagicFoods.NAMESPACE, "shelf_block"),
                FabricBlockEntityTypeBuilder.create(
                        ShelfBlockEntity::new,
                        MagicFoods.SHELF_BLOCK).build(null));
        END_RELAY = Registry.register(
                Registry.BLOCK_ENTITY_TYPE,
                new Identifier(MagicFoods.NAMESPACE, "end_relay"),
                FabricBlockEntityTypeBuilder.create(
                        EndRelayBlockEntity::new,
                        MagicFoods.END_RELAY
                ).build()
                );
    }
}
