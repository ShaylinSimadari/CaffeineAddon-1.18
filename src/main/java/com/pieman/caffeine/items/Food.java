package com.pieman.caffeine.items;

import net.dries007.tfc.common.TFCItemGroup;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

public enum Food {
    COFFEE_CHERRIES(false, true);
    private final boolean meat;
    private final boolean fast;

    Food() {
        this(false, false);
    }

    Food(boolean meat, boolean fast) {
        this.meat = meat;
        this.fast = fast;
    }

    public FoodProperties getFoodProperties() {
        FoodProperties.Builder builder = new FoodProperties.Builder();
        if (this.meat) {
            builder.meat();
        }

        if (this.fast) {
            builder.fast();
        }

        return builder.nutrition(4).saturationMod(0.3F).build();
    }

    public Item.Properties createProperties() {
        Item.Properties props = (new Item.Properties()).food(this.getFoodProperties());
        props.tab(TFCItemGroup.FOOD);
        return props;
    }
}
