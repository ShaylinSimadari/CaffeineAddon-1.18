package com.pieman.caffeine.init;

import com.pieman.caffeine.fluids.Coffee;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.fluids.Alcohol;
import net.dries007.tfc.common.fluids.FlowingFluidRegistryObject;
import net.dries007.tfc.common.fluids.FluidType;
import net.dries007.tfc.common.fluids.MixingFluid;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.registry.RegistrationHelpers;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static net.dries007.tfc.common.fluids.TFCFluids.*;

public class Fluids {
    public static final DeferredRegister<Fluid> FLUIDS;
    public static final Map<Coffee, FlowingFluidRegistryObject<ForgeFlowingFluid>> COFFEE;

    private static FlowingFluidRegistryObject<ForgeFlowingFluid> register(String sourceName, String flowingName, Consumer<ForgeFlowingFluid.Properties> builder, FluidAttributes.Builder attributes) {
        return RegistrationHelpers.registerFluid(FLUIDS, sourceName, flowingName, builder, attributes);
    }

    private static <F extends FlowingFluid> FlowingFluidRegistryObject<F> register(String sourceName, String flowingName, Consumer<ForgeFlowingFluid.Properties> builder, FluidAttributes.Builder attributes, Function<ForgeFlowingFluid.Properties, F> sourceFactory, Function<ForgeFlowingFluid.Properties, F> flowingFactory) {
        return RegistrationHelpers.registerFluid(FLUIDS, sourceName, flowingName, builder, attributes, sourceFactory, flowingFactory);
    }

    private static <F extends Fluid> RegistryObject<F> register(String name, Supplier<F> factory) {
        return FLUIDS.register(name, factory);
    }

    static {
        FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, "caffeine");
        COFFEE = Helpers.mapOfKeys(Coffee.class, (fluid) ->
            register(fluid.getId(), "flowing_" + fluid.getId(), (properties) ->
                properties.block((Supplier) Blocks.COFFEE.get(fluid)).bucket((Supplier) Items.FLUID_BUCKETS.get(FluidType.asType(fluid)))
            , FluidAttributes.builder(WATER_STILL, WATER_FLOW).translationKey("fluid.caffeine." + fluid.getId()).color(fluid.getColor()).overlay(WATER_OVERLAY).sound(SoundEvents.BUCKET_FILL, SoundEvents.BUCKET_EMPTY), MixingFluid.Source::new, MixingFluid.Flowing::new)
        );
    }
}
