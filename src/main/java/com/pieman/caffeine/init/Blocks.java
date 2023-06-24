package com.pieman.caffeine.init;

import com.pieman.caffeine.blockentities.DryingMatBlockEntity;
import com.pieman.caffeine.blocks.DryingMatBlock;
import com.pieman.caffeine.blocks.plant.FruitBlocks;
import com.pieman.caffeine.fluids.Coffee;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.fluids.FlowingFluidRegistryObject;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.registry.RegistrationHelpers;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.pieman.caffeine.init.Items.ITEMS;

public class Blocks {
    public static final DeferredRegister<Block> BLOCKS;
    public static final Map<Coffee, RegistryObject<LiquidBlock>> COFFEE;
    public static final RegistryObject<Block> DRYING_MAT;
    public static final Map<FruitBlocks.Tree, RegistryObject<Block>> FRUIT_TREE_LEAVES;
    public static final Map<FruitBlocks.Tree, RegistryObject<Block>> FRUIT_TREE_BRANCHES;
    public static final Map<FruitBlocks.Tree, RegistryObject<Block>> FRUIT_TREE_GROWING_BRANCHES;
    public static final Map<FruitBlocks.Tree, RegistryObject<Block>> FRUIT_TREE_SAPLINGS;
    public static final Map<FruitBlocks.Tree, RegistryObject<Block>> FRUIT_TREE_POTTED_SAPLINGS;

    public static void registerFlowerPotFlowers() {
        FlowerPotBlock pot = (FlowerPotBlock) net.minecraft.world.level.block.Blocks.FLOWER_POT;
        FRUIT_TREE_POTTED_SAPLINGS.forEach((plant, reg) -> {
            pot.addPlant(((RegistryObject)FRUIT_TREE_SAPLINGS.get(plant)).getId(), reg);
        });
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier) {
        return register(name, blockSupplier, (Function)null);
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier, CreativeModeTab group) {
        return register(name, blockSupplier, (block) -> {
            return new BlockItem(block, (new Item.Properties()).tab(group));
        });
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier, Item.Properties blockItemProperties) {
        return register(name, blockSupplier, (block) -> {
            return new BlockItem(block, blockItemProperties);
        });
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier, @Nullable Function<T, ? extends BlockItem> blockItemFactory) {
        return RegistrationHelpers.registerBlock(BLOCKS, ITEMS, name, blockSupplier, blockItemFactory);
    }

    static {
        BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "caffeine");
        COFFEE = Helpers.mapOfKeys(Coffee.class, (fluid) -> {
            return register("fluid/" + fluid.getId(), () -> {
                return new LiquidBlock(((FlowingFluidRegistryObject) Fluids.COFFEE.get(fluid)).source(), BlockBehaviour.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops());
            });
        });
        DRYING_MAT = register("drying_mat", () -> {
            return new DryingMatBlock(ExtendedProperties
                    .of(Material.DECORATION)
                    .instabreak()
                    .sound(SoundType.STEM)
                    .noOcclusion()
                    .blockEntity(BlockEntities.DRYING_MAT)
                    .ticks(DryingMatBlockEntity::serverTick, DryingMatBlockEntity::clientTick));
        }, Tabs.CAFFEINE);
        FRUIT_TREE_LEAVES = Helpers.mapOfKeys(FruitBlocks.Tree.class, (tree) -> {
            String var10000 = "plant/" + tree.name() + "_leaves";
            Objects.requireNonNull(tree);
            return register(var10000, tree::createLeaves, Tabs.CAFFEINE);
        });
        FRUIT_TREE_BRANCHES = Helpers.mapOfKeys(FruitBlocks.Tree.class, (tree) -> {
            String var10000 = "plant/" + tree.name() + "_branch";
            Objects.requireNonNull(tree);
            return register(var10000, tree::createBranch);
        });
        FRUIT_TREE_GROWING_BRANCHES = Helpers.mapOfKeys(FruitBlocks.Tree.class, (tree) -> {
            String var10000 = "plant/" + tree.name() + "_growing_branch";
            Objects.requireNonNull(tree);
            return register(var10000, tree::createGrowingBranch);
        });
        FRUIT_TREE_SAPLINGS = Helpers.mapOfKeys(FruitBlocks.Tree.class, (tree) -> {
            String var10000 = "plant/" + tree.name() + "_sapling";
            Objects.requireNonNull(tree);
            return register(var10000, tree::createSapling, Tabs.CAFFEINE);
        });
        FRUIT_TREE_POTTED_SAPLINGS = Helpers.mapOfKeys(FruitBlocks.Tree.class, (tree) -> {
            String var10000 = "plant/potted/" + tree.name() + "_sapling";
            Objects.requireNonNull(tree);
            return register(var10000, tree::createPottedSapling);
        });
    }
}
