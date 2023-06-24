package com.pieman.caffeine.compat;

import com.pieman.caffeine.compat.category.DryingMatRecipeCategory;
import com.pieman.caffeine.recipes.DryingMatRecipe;
import com.pieman.caffeine.recipes.RecipeTypes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.dries007.tfc.client.ClientHelpers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@JeiPlugin
public class JEIIntegration implements IModPlugin {

    public static final RecipeType<DryingMatRecipe> DRYING_MAT;

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation("caffeine", "jei");
    }

    private static <T> RecipeType<T> type(String name, Class<T> tClass) {
        return RecipeType.create("caffeine", name, tClass);
    }

    private static <C extends Container, T extends Recipe<C>> List<T> recipes(net.minecraft.world.item.crafting.RecipeType<T> type) {
        return ClientHelpers.getLevelOrThrow().getRecipeManager().getAllRecipesFor(type);
    }

    private static <C extends Container, T extends Recipe<C>> List<T> recipes(net.minecraft.world.item.crafting.RecipeType<T> type, Predicate<T> filter) {
        return (List)recipes(type).stream().filter(filter).collect(Collectors.toList());
    }

    public void registerCategories(IRecipeCategoryRegistration registry) {
        IGuiHelper gui = registry.getJeiHelpers().getGuiHelper();
        registry.addRecipeCategories(new IRecipeCategory[]{
                new DryingMatRecipeCategory(DRYING_MAT, gui)
        });
    }

    public void registerRecipes(IRecipeRegistration registry) {
        registry.addRecipes(DRYING_MAT, recipes((net.minecraft.world.item.crafting.RecipeType) RecipeTypes.DRYING_MAT.get()));
    }

    static {
        DRYING_MAT = type("drying_mat", DryingMatRecipe.class);
    }
}
