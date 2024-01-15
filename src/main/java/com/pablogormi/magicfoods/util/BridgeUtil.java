package com.pablogormi.magicfoods.util;

import com.pablogormi.magicfoods.MagicFoods;
import com.pablogormi.magicfoods.block.BridgeExtenderBlock;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class BridgeUtil {

    public static void lengthenBridge(World world, Direction dir, BlockPos bp, PlayerEntity user, int length, boolean placeBoat) {
        Vec3i dirVector = dir.getVector();

        for (int i = 1; i <= length; i++) {
            if (world.getBlockState(bp.add(dirVector.multiply(i))).getBlock().getHardness() == -1.0F) //Unbreakable
                continue;
            if (i > length-2 && i != length)
                world.setBlockState(bp.add(dirVector.multiply(i)), Blocks.SNOW_BLOCK.getDefaultState());
            else if (i == length)
                world.setBlockState(bp.add(dirVector.multiply(i)), MagicFoods.BRIDGE_EXTENDER_BLOCK.getDefaultState().with(Properties.HORIZONTAL_FACING, dir));
            else
                world.setBlockState(bp.add(dirVector.multiply(i)), Blocks.PACKED_ICE.getDefaultState());
        }

        if (placeBoat) {
            BlockPos boatSpawnPos = bp.add(dirVector.multiply(2).add(0, 2, 0)); //1 above
            BoatEntity boat = new BoatEntity(world, boatSpawnPos.getX() + 0.5F, boatSpawnPos.getY(), boatSpawnPos.getZ() + 0.5F);
            boat.setYaw(dir.asRotation());
            world.spawnEntity(boat);
            world.emitGameEvent(user, GameEvent.ENTITY_PLACE, boatSpawnPos);
        }
    }
    public static void lengthenBridge(World world, Direction dir, BlockPos bp, int length) {
        lengthenBridge(world, dir, bp, null, length, false);
    }
}
