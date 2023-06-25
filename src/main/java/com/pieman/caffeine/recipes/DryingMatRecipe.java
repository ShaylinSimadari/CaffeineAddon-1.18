package com.pieman.caffeine.recipes;

import com.google.gson.JsonObject;
import net.dries007.tfc.common.recipes.*;
import net.dries007.tfc.common.recipes.inventory.ItemStackInventory;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.JsonHelpers;
import net.dries007.tfc.util.collections.IndirectHashCollection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

public class DryingMatRecipe extends SimpleItemRecipe {
    public static final IndirectHashCollection<Item, DryingMatRecipe> CACHE;
    private final int duration;

    public static @Nullable DryingMatRecipe getRecipe(Level level, ItemStack stack) {
        return getRecipe(level, new ItemStackInventory(stack));
    }

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

    public DryingMatRecipe(ResourceLocation id, Ingredient ingredient, ItemStackProvider result, int duration) {
        super(id, ingredient, result);
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public RecipeSerializer<?> getSerializer() {
        return (RecipeSerializer) RecipeSerializers.DRYING_MAT.get();
    }

    public RecipeType<?> getType() {
        return (RecipeType) RecipeTypes.DRYING_MAT.get();
    }

    public static class Serializer extends RecipeSerializerImpl<DryingMatRecipe> {
        public Serializer() {
        }

        public DryingMatRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            Ingredient ingredient = Ingredient.fromJson(JsonHelpers.get(json, "ingredient"));
            ItemStackProvider result = ItemStackProvider.fromJson(JsonHelpers.getAsJsonObject(json, "result"));
            int duration = JsonHelpers.getAsInt(json, "duration");
            return new DryingMatRecipe(recipeId, ingredient, result, duration);
        }

        public @Nullable DryingMatRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            ItemStackProvider result = ItemStackProvider.fromNetwork(buffer);
            int duration = buffer.readVarInt();
            return new DryingMatRecipe(recipeId, ingredient, result, duration);
        }

        public void toNetwork(FriendlyByteBuf buffer, DryingMatRecipe recipe) {
            recipe.getIngredient().toNetwork(buffer);
            recipe.result.toNetwork(buffer);
            buffer.writeVarInt(recipe.duration);
        }
    }

    static {
        CACHE = IndirectHashCollection.createForRecipe(SimpleItemRecipe::getValidItems, RecipeTypes.DRYING_MAT);
    }
}
