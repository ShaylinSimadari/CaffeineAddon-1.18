package com.pieman.caffeine.recipes;

import net.dries007.tfc.common.recipes.SimpleItemRecipe;
import net.dries007.tfc.common.recipes.inventory.ItemStackInventory;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.dries007.tfc.util.collections.IndirectHashCollection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

public class DryingMatRecipe extends SimpleItemRecipe {
    public static final IndirectHashCollection<Item, DryingMatRecipe> CACHE;

    public static @Nullable DryingMatRecipe getRecipe(Level world, ItemStackInventory wrapper) {
        Iterator iter = CACHE.getAll(wrapper.getStack().getItem()).iterator();

        DryingMatRecipe recipe;
        do {
            if (!iter.hasNext()) {
                return null;
            }

            recipe = (DryingMatRecipe)iter.next();
        } while(!recipe.matches(wrapper, world));

        return recipe;
    }

    public DryingMatRecipe(ResourceLocation id, Ingredient ingredient, ItemStackProvider result) {
        super(id, ingredient, result);
    }

    public RecipeSerializer<?> getSerializer() {
        return (RecipeSerializer) RecipeSerializers.DRYING_MAT.get();
    }

    public RecipeType<?> getType() {
        return (RecipeType) RecipeTypes.DRYING_MAT.get();
    }

    static {
        CACHE = IndirectHashCollection.createForRecipe(SimpleItemRecipe::getValidItems, RecipeTypes.DRYING_MAT);
    }
}
