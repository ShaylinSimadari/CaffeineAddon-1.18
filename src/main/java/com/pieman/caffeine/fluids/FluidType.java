package com.pieman.caffeine.fluids;

import com.pieman.caffeine.init.Fluids;
import net.dries007.tfc.common.fluids.FlowingFluidRegistryObject;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.material.Fluid;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record FluidType(String name, OptionalInt color, Supplier<? extends Fluid> fluid) {
    private static final Map<Enum<?>, FluidType> IDENTITY;
    private static final List<FluidType> VALUES;

    public FluidType(String name, OptionalInt color, Supplier<? extends Fluid> fluid) {
        this.name = name;
        this.color = color;
        this.fluid = fluid;
    }

    public static <R> Map<FluidType, R> mapOf(Function<? super FluidType, ? extends R> map) {
        return (Map)VALUES.stream().collect(Collectors.toMap(Function.identity(), map));
    }

    public static FluidType asType(Enum<?> identity) {
        return (FluidType)IDENTITY.get(identity);
    }

    private static FluidType fromEnum(Enum<?> identity, int color, String name, Supplier<? extends Fluid> fluid) {
        FluidType type = new FluidType(name, OptionalInt.of(-16777216 | color), fluid);
        IDENTITY.put(identity, type);
        return type;
    }

    public String name() {
        return this.name;
    }

    public OptionalInt color() {
        return this.color;
    }

    public Supplier<? extends Fluid> fluid() {
        return this.fluid;
    }

    static {
        IDENTITY = new HashMap();
        VALUES = Stream.of(
            Arrays.stream(Coffee.values()).map((fluid) ->
                fromEnum(fluid, fluid.getColor(), fluid.getId(), ((FlowingFluidRegistryObject) Fluids.COFFEE.get(fluid)).source())
            )).flatMap(Function.identity()).toList();
    }
}
