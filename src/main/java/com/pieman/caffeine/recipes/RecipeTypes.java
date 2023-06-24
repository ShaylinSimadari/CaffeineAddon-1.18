package com.pieman.caffeine.recipes;

import net.dries007.tfc.common.recipes.CollapseRecipe;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class RecipeTypes {//TODO add "type": "tfc:not_rotten", in all jsons
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES;
    public static final RegistryObject<RecipeType<DryingMatRecipe>> DRYING_MAT;

    private static <R extends Recipe<?>> RegistryObject<RecipeType<R>> register(String name) {
        return RECIPE_TYPES.register(name, () -> {
            return new RecipeType<R>() {
                public String toString() {
                    return name;
                }
            };
        });
    }

    static {
        RECIPE_TYPES = DeferredRegister.create(Registry.RECIPE_TYPE_REGISTRY, "caffeine");
        DRYING_MAT = register("drying_mat");
    }
}
