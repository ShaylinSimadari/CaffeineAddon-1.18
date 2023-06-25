package com.pieman.caffeine.compat.jei.category;

import com.pieman.caffeine.init.Blocks;
import com.pieman.caffeine.recipes.DryingMatRecipe;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.dries007.tfc.compat.jei.category.SimpleItemRecipeCategory;
import net.dries007.tfc.util.Helpers;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

public class DryingMatRecipeCategory  extends SimpleItemRecipeCategory<DryingMatRecipe> {
    //Overloading the title param so we can use our mod id
    private Component title;

    public DryingMatRecipeCategory(RecipeType<DryingMatRecipe> type, IGuiHelper helper) {
        super(type, helper, new ItemStack((ItemLike) Blocks.DRYING_MAT.get()));
        this.title = Helpers.translatable("caffeine.jei." + type.getUid().getPath());
    }

    @Override
    protected @Nullable TagKey<Item> getToolTag() {
        return null;
    }
    public Component getTitle() {
        return this.title;
    }
}
