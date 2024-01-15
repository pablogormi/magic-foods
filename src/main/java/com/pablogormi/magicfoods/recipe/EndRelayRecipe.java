package com.pablogormi.magicfoods.recipe;

import com.pablogormi.magicfoods.MagicFoods;
import com.pablogormi.magicfoods.block.entity.EndRelayBlockEntity;
import com.pablogormi.magicfoods.block.entity.ModBlockEntities;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EndRelayRecipe extends SpecialCraftingRecipe {
    public EndRelayRecipe(Identifier id) {
        super(id);
    }

    /*
       0 1 2
       3 4 5
       6 7 8
     */
    @Override
    public boolean matches(CraftingInventory inventory, World world) {
        if (inventory.getWidth() == 3 && inventory.getHeight()==3) { //watafak
            return inventory.getStack(0).isOf(Items.OBSIDIAN) &&
                    inventory.getStack(2).isOf(Items.OBSIDIAN) &&
                    inventory.getStack(6).isOf(Items.OBSIDIAN) &&
                    inventory.getStack(8).isOf(Items.OBSIDIAN) &&
                    inventory.getStack(1).isOf(Items.POPPED_CHORUS_FRUIT) &&
                    inventory.getStack(5).isOf(Items.POPPED_CHORUS_FRUIT) &&
                    inventory.getStack(3).isOf(Items.POPPED_CHORUS_FRUIT) &&
                    inventory.getStack(7).isOf(Items.POPPED_CHORUS_FRUIT) &&
                    inventory.getStack(4).isOf(Items.COMPASS); //&& TODO: ACABAR ESTO (debería funcionar)
                    //inventory.getStack(4).getNbt().contains("LodestoneTracked") &&
                    //inventory.getStack(4).getNbt().getBoolean("LodestoneTracked");
        }
        return false;
    }

    @Override
    public ItemStack craft(CraftingInventory inventory) {
        if (!matches(inventory, null)) return ItemStack.EMPTY;
        ItemStack compass_stack = inventory.getStack(4);
        BlockPos pos = NbtHelper.toBlockPos(compass_stack.getNbt().getCompound("LodestonePos"));
        ItemStack i = new ItemStack(MagicFoods.END_RELAY_BLOCKITEM, 1);

        EndRelayBlockEntity be = new EndRelayBlockEntity(BlockPos.ORIGIN, MagicFoods.END_RELAY.getDefaultState());
        NbtCompound compound = be.createNbtWithIdentifyingData();
        compound.remove("position");
        compound.put("position", NbtHelper.fromBlockPos(pos));
        BlockItem.setBlockEntityNbt(i, ModBlockEntities.END_RELAY, compound);
        return i;
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return MagicFoods.END_RELAY_RECIPE;
    }
}
