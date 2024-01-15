package com.pablogormi.magicfoods.client;

import com.pablogormi.magicfoods.block.ShelfBlock;
import com.pablogormi.magicfoods.block.entity.ShelfBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;

public class ShelfBlockEntityRenderer implements BlockEntityRenderer<ShelfBlockEntity> {


    public ShelfBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {

    }

    @Override
    public void render(ShelfBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {

        Direction facing = entity.getCachedState().get(ShelfBlock.FACING);



        for (int i = 0; i < entity.size(); i++) {
            ItemStack stack = entity.getStack(i);
            matrices.push();

            switch(facing) { //TODO esto va mal mal
                case NORTH: {
                    matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(0));
                }
                case EAST: {
                    matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90));
                }
                case SOUTH: {
                    //matrices.translate(1.0/3.0, 0, 2.0/3.0);
                    matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(0));
                }
                case WEST: {
                    matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90));
                }
            }
            MinecraftClient.getInstance()
                    .getItemRenderer()
                    .renderItem(stack, ModelTransformation.Mode.GROUND, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, 0);
            if (i == 1) {
                //matrices.translate(1.0/3.0, 0, 0);
                //matrices.translate(0, 0.5, 0);
            } else {
                //matrices.translate(-1.0/3.0, 0, 0);
            }
            matrices.pop();
        }




    }
}
