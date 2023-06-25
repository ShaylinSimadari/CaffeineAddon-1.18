package com.pieman.caffeine.recipes;

import net.dries007.tfc.common.recipes.*;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class RecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS;
    public static final RegistryObject<DryingMatRecipe.Serializer> DRYING_MAT;

    private static <S extends RecipeSerializer<?>> RegistryObject<S> register(String name, Supplier<S> factory) {
        return RECIPE_SERIALIZERS.register(name, factory);
    }

    static {
        RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, "caffeine");
        DRYING_MAT = register("drying_mat", DryingMatRecipe.Serializer::new);
    }
}
