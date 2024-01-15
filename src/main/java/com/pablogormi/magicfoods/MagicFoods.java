package com.pablogormi.magicfoods;

import com.pablogormi.magicfoods.block.BridgeExtenderBlock;
import com.pablogormi.magicfoods.block.EndRelayBlock;
import com.pablogormi.magicfoods.block.ShelfBlock;
import com.pablogormi.magicfoods.block.entity.ModBlockEntities;
import com.pablogormi.magicfoods.entity.BridgeBallEntity;
import com.pablogormi.magicfoods.entity.effect.ModStatusEffects;
import com.pablogormi.magicfoods.item.BridgeBallItem;
import com.pablogormi.magicfoods.item.EndRelayBlockItem;
import com.pablogormi.magicfoods.networking.ModMessages;
import com.pablogormi.magicfoods.recipe.EndRelayRecipe;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.*;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.registry.Registry;

public class MagicFoods implements ModInitializer {
    public static final String NAMESPACE = "magicfoods";


    public static final Item BRIDGE_BALL_ITEM = new BridgeBallItem(new FabricItemSettings().group(ItemGroup.FOOD).maxCount(16));
    public static final Item MAGIC_BEEF_ITEM = new Item(new FabricItemSettings()
            .group(ItemGroup.FOOD)
            .rarity(Rarity.RARE)
            .food(new FoodComponent.Builder()
                    .hunger(8)
                    .saturationModifier(0.8F)
                    .alwaysEdible()
                    .meat()
                    .statusEffect(new StatusEffectInstance(ModStatusEffects.FLYING_STATUS_EFFECT, 20*30), 1.0F)
                    .build()
            )
    );

    public static final Block BRIDGE_EXTENDER_BLOCK = new BridgeExtenderBlock(FabricBlockSettings.of(Material.DENSE_ICE).slipperiness(0.98F).strength(0.5F).sounds(BlockSoundGroup.GLASS));
    public static final BlockItem BRIDGE_EXTENDER_BLOCKITEM = new BlockItem(BRIDGE_EXTENDER_BLOCK, new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS));

    public static final Block SHELF_BLOCK = new ShelfBlock(FabricBlockSettings.of(Material.WOOD).nonOpaque());
    public static final BlockItem SHELF_BLOCKITEM = new BlockItem(SHELF_BLOCK, new FabricItemSettings().group(ItemGroup.DECORATIONS));

    public static final Block END_RELAY = new EndRelayBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(3.5f));
    public static final BlockItem END_RELAY_BLOCKITEM = new EndRelayBlockItem(END_RELAY, new FabricItemSettings().group(ItemGroup.MISC));

    public static final EntityType<BridgeBallEntity> BRIDGE_BALL_ENTITY_TYPE = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(NAMESPACE, "bridge_ball"),
            FabricEntityTypeBuilder.<BridgeBallEntity>create(SpawnGroup.MISC, BridgeBallEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25F, 0.25F))
                    .trackRangeBlocks(4).trackedUpdateRate(10)
                    .build()
    );

    public static final SpecialRecipeSerializer<EndRelayRecipe> END_RELAY_RECIPE = new SpecialRecipeSerializer<>(EndRelayRecipe::new);

    @Override
    public void onInitialize() {
        Registry.register(Registry.ITEM, Identifier.of(NAMESPACE, "bridge_ball"), BRIDGE_BALL_ITEM);
        Registry.register(Registry.ITEM, Identifier.of(NAMESPACE, "bridge_extender"), BRIDGE_EXTENDER_BLOCKITEM);
        Registry.register(Registry.ITEM, Identifier.of(NAMESPACE, "magic_beef"), MAGIC_BEEF_ITEM);
        Registry.register(Registry.ITEM, Identifier.of(NAMESPACE, "shelf"), SHELF_BLOCKITEM);
        Registry.register(Registry.ITEM, Identifier.of(NAMESPACE, "end_relay"), END_RELAY_BLOCKITEM);

        Registry.register(Registry.BLOCK, Identifier.of(NAMESPACE, "shelf"), SHELF_BLOCK);
        Registry.register(Registry.BLOCK, Identifier.of(NAMESPACE, "bridge_extender"), BRIDGE_EXTENDER_BLOCK);
        Registry.register(Registry.BLOCK, Identifier.of(NAMESPACE, "end_relay"), END_RELAY);

        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(NAMESPACE, "end_relay_recipe"), END_RELAY_RECIPE);
        //RecipeSerializer.register("end_relay", END_RELAY_RECIPE);

        ModStatusEffects.registerEffects();

        ModBlockEntities.register();

        ModMessages.registerS2CPackets();

        /*UseItemCallback.EVENT.register((player, world, hand) -> {
            if (player.getStackInHand(hand).getItem().getFoodComponent() != null) {
                player.addStatusEffect(new StatusEffectInstance(ModStatusEffects.FLYING_STATUS_EFFECT, 20*5,1));
                player.getStackInHand(hand).decrement(1);
                return TypedActionResult.success(ItemStack.EMPTY);
            }
            return TypedActionResult.pass(player.getStackInHand(hand));
        });*/
    }

}
