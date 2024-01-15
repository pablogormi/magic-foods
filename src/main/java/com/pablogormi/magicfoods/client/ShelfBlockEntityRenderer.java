package com.pablogormi.magicfoods.client;

import com.pablogormi.magicfoods.block.ShelfBlock;
import com.pablogormi.magicfoods.block.entity.ShelfBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AffineTransformations;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

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
                    matrices.multiplyPositionMatrix(AffineTransformations.DIRECTION_ROTATIONS.get(Direction.NORTH).getMatrix());
                }
                case EAST: {
                    matrices.multiplyPositionMatrix(AffineTransformations.DIRECTION_ROTATIONS.get(Direction.EAST).getMatrix());
                }
                case SOUTH: {
                    //matrices.translate(1.0/3.0, 0, 2.0/3.0);
                    matrices.multiplyPositionMatrix(AffineTransformations.DIRECTION_ROTATIONS.get(Direction.SOUTH).getMatrix());
                }
                case WEST: {
                    matrices.multiplyPositionMatrix(AffineTransformations.DIRECTION_ROTATIONS.get(Direction.WEST).getMatrix());
                }
            }
            int lightAbove = WorldRenderer.getLightmapCoordinates(entity.getWorld(), entity.getPos().up());
            MinecraftClient.getInstance()
                    .getItemRenderer()
                    .renderItem(stack, ModelTransformationMode.GROUND, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 0);
                    //.renderItem(stack, ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, 0);
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
