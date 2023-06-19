package com.pieman.caffeine.items;

import net.dries007.tfc.common.TFCItemGroup;
import net.minecraft.world.item.Item;

public enum Coffee {
    COFFEE_CHERRIES_DRIED,
    COFFEE_BEANS_GREEN,
    COFFEE_BEANS,
    COFFEE_GROUND;

    Coffee() {
    }

    public Item.Properties createProperties() {
        Item.Properties props = new Item.Properties();
        props.tab(TFCItemGroup.FOOD);
        return props;
    }
}
