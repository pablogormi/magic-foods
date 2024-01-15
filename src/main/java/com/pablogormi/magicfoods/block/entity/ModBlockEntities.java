package com.pablogormi.magicfoods.block.entity;

import com.pablogormi.magicfoods.MagicFoods;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static BlockEntityType<ShelfBlockEntity> SHELF_BLOCK;
    public static BlockEntityType<EndRelayBlockEntity> END_RELAY;

    public static void register() {
        SHELF_BLOCK = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier(MagicFoods.NAMESPACE, "shelf_block"),
                FabricBlockEntityTypeBuilder.create(
                        ShelfBlockEntity::new,
                        MagicFoods.SHELF_BLOCK).build(null));
        END_RELAY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier(MagicFoods.NAMESPACE, "end_relay"),
                FabricBlockEntityTypeBuilder.create(
                        EndRelayBlockEntity::new,
                        MagicFoods.END_RELAY
                ).build()
                );
    }
}
