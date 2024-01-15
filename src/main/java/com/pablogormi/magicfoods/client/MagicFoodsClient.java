package com.pablogormi.magicfoods.client;

import com.pablogormi.magicfoods.MagicFoods;
import com.pablogormi.magicfoods.block.entity.ModBlockEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class MagicFoodsClient implements ClientModInitializer {


    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(MagicFoods.BRIDGE_BALL_ENTITY_TYPE, FlyingItemEntityRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntities.SHELF_BLOCK, ShelfBlockEntityRenderer::new);
    }



}
