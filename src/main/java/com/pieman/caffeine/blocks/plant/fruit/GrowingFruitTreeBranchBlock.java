package com.pieman.caffeine.blocks.plant.fruit;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blockentities.TickCounterBlockEntity;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.TFCBlockStateProperties;
import net.dries007.tfc.common.blocks.plant.fruit.FruitTreeBranchBlock;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.climate.ClimateRange;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class GrowingFruitTreeBranchBlock extends net.dries007.tfc.common.blocks.plant.fruit.GrowingFruitTreeBranchBlock{
    private final Supplier<? extends Block> body;
    private final Supplier<? extends Block> leaves;
    private final Supplier<ClimateRange> climateRange;
    public static final IntegerProperty SAPLINGS;
    public static final BooleanProperty NATURAL;
    private static final Direction[] NOT_DOWN;

    private static boolean canGrowInto(LevelReader level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.isAir() || Helpers.isBlock(state, TFCTags.Blocks.FRUIT_TREE_LEAVES);
    }

    private static boolean allNeighborsEmpty(LevelReader level, BlockPos pos, @Nullable Direction excludingSide) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        Iterator var4 = Direction.Plane.HORIZONTAL.iterator();

        Direction direction;
        do {
            if (!var4.hasNext()) {
                return true;
            }

            direction = (Direction)var4.next();
            mutablePos.set(pos).move(direction);
        } while(direction == excludingSide || canGrowInto(level, mutablePos));

        return false;
    }

    public GrowingFruitTreeBranchBlock(ExtendedProperties properties, Supplier<? extends Block> body, Supplier<? extends Block> leaves, Supplier<ClimateRange> climateRange) {
        super(properties, body, leaves, climateRange);
        this.body = body;
        this.leaves = leaves;
        this.climateRange = climateRange;
    }

    public void grow(BlockState state, ServerLevel level, BlockPos pos, Random random, int cyclesLeft) {
        System.out.println("Growing at (" + pos.getX() + ", " + pos.getZ() + ")");
        FruitTreeBranchBlock body = (FruitTreeBranchBlock)this.body.get();
        BlockPos abovePos = pos.above();
        boolean natural = (Boolean)state.getValue(NATURAL);
        if (canGrowInto(level, abovePos) && abovePos.getY() < level.getMaxBuildHeight() - 1) {
            System.out.println("canGrowInto(level, abovePos) && abovePos.getY() < level.getMaxBuildHeight() - 1");
            int stage = (Integer)state.getValue(STAGE);
            if (stage < 3) {
                System.out.println("stage < 3");
                boolean willGrowUpward = false;
                BlockState belowState = level.getBlockState(pos.below());
                Block belowBlock = belowState.getBlock();
                if (Helpers.isBlock(belowBlock, TFCTags.Blocks.BUSH_PLANTABLE_ON)) {
                    System.out.println("Helpers.isBlock(belowBlock, TFCTags.Blocks.BUSH_PLANTABLE_ON");
                    willGrowUpward = true;
                } else if (belowBlock != body) {
                    System.out.println("belowBlock != body");
                    if (canGrowInto(level, pos.below())) {
                        System.out.println("canGrowInto(level, pos.below())");
                        willGrowUpward = true;
                    }
                } else {
                    BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
                    int j = 1;

                    for(int k = 0; k < 4; ++k) {
                        mutablePos.setWithOffset(pos, 0, -1 * (j + 1), 0);
                        if (level.getBlockState(mutablePos).getBlock() != body) {
                            break;
                        }

                        ++j;
                    }

                    if (j < 2) {
                        willGrowUpward = true;
                    }
                }

                if (willGrowUpward && allNeighborsEmpty(level, abovePos, (Direction)null) && canGrowInto(level, pos.above(2))) {
                    System.out.println("willGrowUpward && allNeighborsEmpty(level, abovePos, (Direction)null) && canGrowInto(level, pos.above(2))");
                    this.placeBody(level, pos, stage);
                    this.placeGrownFlower(level, abovePos, stage, (Integer)state.getValue(SAPLINGS), cyclesLeft - 1, natural);
                } else if (stage < 2) {
                    System.out.println("While looping");
                    System.out.println("saplings" + (Integer)state.getValue(SAPLINGS));
                    System.out.println("stage" + stage);
                    int branches = Math.max(0, (Integer)state.getValue(SAPLINGS) - stage);
                    System.out.println("branches" + branches);
                    BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
                    List<Direction> directions = (List) Direction.Plane.HORIZONTAL.stream().collect(Collectors.toList());

                    while(branches > 0) {
                        Direction test = Direction.Plane.HORIZONTAL.getRandomDirection(random);
                        if (directions.contains(test)) {
                            mutablePos.setWithOffset(pos, test);
                            if (canGrowInto(level, mutablePos)) {
                                mutablePos.move(0, -1, 0);
                                if (canGrowInto(level, mutablePos)) {
                                    mutablePos.move(0, 1, 0);
                                    if (allNeighborsEmpty(level, mutablePos, test.getOpposite())) {
                                        System.out.println("*while loop place flower");
                                        this.placeGrownFlower(level, mutablePos, stage + 1, (Integer)state.getValue(SAPLINGS), cyclesLeft - 1, natural);
                                    }
                                }
                            }

                            directions.remove(test);
                            --branches;
                        }
                    }
                    System.out.println("-placing body");
                    this.placeBody(level, pos, stage);
                } else {
                    System.out.println("-placeBody last");
                    this.placeBody(level, pos, stage);
                }
            }
        }

    }

    private void placeGrownFlower(ServerLevel level, BlockPos pos, int stage, int saplings, int cycles, boolean natural) {
        System.out.println("place grown flower"+ pos);
        System.out.println(pos.getX() + " " + pos.getY());
        //-5600431040290935706
        ///tp -5966 100 7582
        ///tp -6127 100 7075
        System.out.println((BlockState)((BlockState)((BlockState)this.getStateForPlacement(level, pos).setValue(STAGE, stage)).setValue(SAPLINGS, saplings)).setValue(NATURAL, natural));
        level.setBlock(pos, (BlockState)((BlockState)((BlockState)this.getStateForPlacement(level, pos).setValue(STAGE, stage)).setValue(SAPLINGS, saplings)).setValue(NATURAL, natural), 3);
        BlockEntity var8 = level.getBlockEntity(pos);
        System.out.println("level.getBlockEntity(pos)" + var8);
        if (var8 instanceof TickCounterBlockEntity counter) {
            System.out.println("var8 instanceof TickCounterBlockEntity counter");
            counter.resetCounter();
            counter.reduceCounter(-24000L * (long)cycles * 5L);
        }

        this.addLeaves(level, pos);
        level.getBlockState(pos).randomTick(level, pos, level.random);
    }

    private void placeBody(LevelAccessor level, BlockPos pos, int stage) {
        FruitTreeBranchBlock plant = (FruitTreeBranchBlock)this.body.get();
        level.setBlock(pos, (BlockState)plant.getStateForPlacement(level, pos).setValue(STAGE, stage), 3);
        this.addLeaves(level, pos);
    }

    private void addLeaves(LevelAccessor level, BlockPos pos) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        BlockState leaves = ((Block)this.leaves.get()).defaultBlockState();
        mutablePos.setWithOffset(pos, 0, -2, 0);
        BlockState downState = level.getBlockState(mutablePos);
        if (downState.isAir() || Helpers.isBlock(downState, TFCTags.Blocks.FRUIT_TREE_LEAVES) || Helpers.isBlock(downState, TFCTags.Blocks.FRUIT_TREE_BRANCH)) {
            Direction[] var6 = NOT_DOWN;
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                Direction d = var6[var8];
                mutablePos.setWithOffset(pos, d);
                if (level.isEmptyBlock(mutablePos)) {
                    level.setBlock(mutablePos, leaves, 2);
                }
            }

        }
    }

    static {
        SAPLINGS = TFCBlockStateProperties.SAPLINGS;
        NATURAL = TFCBlockStateProperties.NATURAL;
        NOT_DOWN = new Direction[]{Direction.WEST, Direction.EAST, Direction.SOUTH, Direction.NORTH, Direction.UP};
    }


}
