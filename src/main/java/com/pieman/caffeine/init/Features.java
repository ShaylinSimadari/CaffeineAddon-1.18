package com.pieman.caffeine.init;

import com.mojang.serialization.Codec;
import net.dries007.tfc.world.Codecs;
import net.dries007.tfc.world.feature.plant.FruitTreeFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;

public class Features {
    public static final DeferredRegister<Feature<?>> FEATURES;
    public static final RegistryObject<Feature> FRUIT_TREES;

    public Features() {
    }

    private static <C extends FeatureConfiguration, F extends Feature<C>> RegistryObject<Feature> register(String name, Function<Codec<C>, F> factory, Codec<C> codec) {
        return FEATURES.register(name, () -> {
            return (Feature)factory.apply(codec);
        });
    }

    static {
        FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, "caffeine");
        FRUIT_TREES = register("fruit_trees", FruitTreeFeature::new, Codecs.BLOCK_STATE_CONFIG);
    }
}
