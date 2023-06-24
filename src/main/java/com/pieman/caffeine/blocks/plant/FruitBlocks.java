package com.pieman.caffeine.blocks.plant;

import com.pieman.caffeine.init.Blocks;
import com.pieman.caffeine.init.Items;
import com.pieman.caffeine.items.Food;
import com.pieman.caffeine.util.climate.ClimateRanges;
import net.dries007.tfc.common.blockentities.BerryBushBlockEntity;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.plant.fruit.*;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.config.TFCConfig;
//import net.dries007.tfc.util.climate.ClimateRanges;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Locale;
import java.util.function.Supplier;

public final class FruitBlocks {
    public static enum Tree implements StringRepresentable {
        COFFEE(Food.COFFEE_CHERRIES, 8, new Lifecycle[]{Lifecycle.HEALTHY, Lifecycle.HEALTHY, Lifecycle.HEALTHY, Lifecycle.FLOWERING, Lifecycle.FLOWERING, Lifecycle.FRUITING, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.HEALTHY});

        private final Food product;
        private final Lifecycle[] stages;
        private final String serializedName;
        private final int treeGrowthDays;

        private Tree(Food product, int treeGrowthDays, Lifecycle[] stages) {
            this.product = product;
            this.stages = stages;
            this.serializedName = this.name().toLowerCase(Locale.ROOT);
            this.treeGrowthDays = treeGrowthDays;
        }

        public Block createSapling() {
            return new FruitTreeSaplingBlock(ExtendedProperties.of(Material.PLANT).noCollission().randomTicks().strength(0.0F).sound(SoundType.GRASS).blockEntity(TFCBlockEntities.TICK_COUNTER).flammableLikeLeaves(), (Supplier) Blocks.FRUIT_TREE_GROWING_BRANCHES.get(this), this::daysToGrow, (Supplier) ClimateRanges.FRUIT_TREES.get(this), this.stages);
        }

        public Block createPottedSapling() {
            return new FlowerPotBlock(() -> {
                return (FlowerPotBlock) net.minecraft.world.level.block.Blocks.FLOWER_POT;
            }, (Supplier) TFCBlocks.FRUIT_TREE_SAPLINGS.get(this), BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noOcclusion());
        }

        public Block createLeaves() {
            return new FruitTreeLeavesBlock(ExtendedProperties.of(Material.LEAVES).strength(0.5F).sound(SoundType.GRASS).randomTicks().noOcclusion().blockEntity(TFCBlockEntities.BERRY_BUSH).serverTicks(BerryBushBlockEntity::serverTick).flammableLikeLeaves(), (Supplier) Items.FOOD.get(this.product), this.stages, (Supplier) ClimateRanges.FRUIT_TREES.get(this));
        }

        public Block createBranch() {
            return new FruitTreeBranchBlock(ExtendedProperties.of(Material.WOOD).sound(SoundType.SCAFFOLDING).randomTicks().strength(1.0F).flammableLikeLogs(), (Supplier) ClimateRanges.FRUIT_TREES.get(this));
        }

        public Block createGrowingBranch() {
            return new com.pieman.caffeine.blocks.plant.fruit.GrowingFruitTreeBranchBlock(ExtendedProperties.of(Material.WOOD).sound(SoundType.SCAFFOLDING).randomTicks().strength(1.0F).blockEntity(TFCBlockEntities.TICK_COUNTER).flammableLikeLogs(), (Supplier) Blocks.FRUIT_TREE_BRANCHES.get(this), (Supplier) Blocks.FRUIT_TREE_LEAVES.get(this), (Supplier) ClimateRanges.FRUIT_TREES.get(this));
        }

        public int daysToGrow() {
            return defaultDaysToGrow();
//            return (Integer) ((ForgeConfigSpec.IntValue) TFCConfig.SERVER.fruitSaplingGrowthDays.get(this)).get();
        }

        public int defaultDaysToGrow() {
            return this.treeGrowthDays;
        }

        public String getSerializedName() {
            return this.serializedName;
        }
    }
}