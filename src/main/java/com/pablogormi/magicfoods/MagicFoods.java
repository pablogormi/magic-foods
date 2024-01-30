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
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.*;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class MagicFoods implements ModInitializer {
    public static final String NAMESPACE = "magicfoods";


    public static final Item BRIDGE_BALL_ITEM = new BridgeBallItem(new FabricItemSettings().maxCount(16));
    public static final Item MAGIC_BEEF_ITEM = new Item(new FabricItemSettings()
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



    public static final Block BRIDGE_EXTENDER_BLOCK = new BridgeExtenderBlock(FabricBlockSettings.create().slipperiness(0.98F).strength(0.5F).sounds(BlockSoundGroup.GLASS));
    public static final BlockItem BRIDGE_EXTENDER_BLOCKITEM = new BlockItem(BRIDGE_EXTENDER_BLOCK, new FabricItemSettings());

    public static final Block SHELF_BLOCK = new ShelfBlock(FabricBlockSettings.create().nonOpaque());
    public static final BlockItem SHELF_BLOCKITEM = new BlockItem(SHELF_BLOCK, new FabricItemSettings());

    public static final Block END_RELAY = new EndRelayBlock(FabricBlockSettings.create().requiresTool().strength(2f));
    public static final BlockItem END_RELAY_BLOCKITEM = new EndRelayBlockItem(END_RELAY, new FabricItemSettings());

    public static final EntityType<BridgeBallEntity> BRIDGE_BALL_ENTITY_TYPE = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(NAMESPACE, "bridge_ball"),
            FabricEntityTypeBuilder.<BridgeBallEntity>create(SpawnGroup.MISC, BridgeBallEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25F, 0.25F))
                    .trackRangeBlocks(4).trackedUpdateRate(10)
                    .build()
    );

    public static final SpecialRecipeSerializer<EndRelayRecipe> END_RELAY_RECIPE = new SpecialRecipeSerializer<EndRelayRecipe>(EndRelayRecipe::new);

    private static final ItemGroup ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(MAGIC_BEEF_ITEM, 1))
            .displayName(Text.translatable("magicfoods.gui.item_group"))
            .entries(((displayContext, entries) -> {
                entries.add(BRIDGE_BALL_ITEM);
                entries.add(MAGIC_BEEF_ITEM);
                entries.add(BRIDGE_EXTENDER_BLOCKITEM);
                entries.add(SHELF_BLOCKITEM);
                entries.add(END_RELAY_BLOCKITEM);
            }))
            .build();

    @Override
    public void onInitialize() {
        Registry.register(Registries.ITEM, Identifier.of(NAMESPACE, "bridge_ball"), BRIDGE_BALL_ITEM);
        Registry.register(Registries.ITEM, Identifier.of(NAMESPACE, "bridge_extender"), BRIDGE_EXTENDER_BLOCKITEM);
        Registry.register(Registries.ITEM, Identifier.of(NAMESPACE, "magic_beef"), MAGIC_BEEF_ITEM);
        Registry.register(Registries.ITEM, Identifier.of(NAMESPACE, "shelf"), SHELF_BLOCKITEM);
        Registry.register(Registries.ITEM, Identifier.of(NAMESPACE, "end_relay"), END_RELAY_BLOCKITEM);

        Registry.register(Registries.BLOCK, Identifier.of(NAMESPACE, "shelf"), SHELF_BLOCK);
        Registry.register(Registries.BLOCK, Identifier.of(NAMESPACE, "bridge_extender"), BRIDGE_EXTENDER_BLOCK);
        Registry.register(Registries.BLOCK, Identifier.of(NAMESPACE, "end_relay"), END_RELAY);

        Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(NAMESPACE, "end_relay_recipe"), END_RELAY_RECIPE);

        Registry.register(Registries.ITEM_GROUP, new Identifier(NAMESPACE, "group"), ITEM_GROUP);
        ModStatusEffects.registerEffects();

        ModBlockEntities.register();

        ModMessages.registerS2CPackets();

    }

}
