package com.pieman.caffeine.util.climate;

import com.pieman.caffeine.blocks.plant.FruitBlocks;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.RegisteredDataManager;
import net.dries007.tfc.util.climate.ClimateRange;

import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

public class ClimateRanges {
    //Registers a climate for plant with TFCs ClimateRange.MANAGER
    public static final Map<FruitBlocks.Tree, Supplier<ClimateRange>> FRUIT_TREES = Helpers.mapOfKeys(FruitBlocks.Tree.class, (tree) -> {
        return register("plant/" + tree.name() + "_tree");
    });
    public ClimateRanges() {
    }

    private static RegisteredDataManager.Entry<ClimateRange> register(String name) {
        return ClimateRange.MANAGER.register(Helpers.identifier(name.toLowerCase(Locale.ROOT)));
    }
}
