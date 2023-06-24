package com.pieman.caffeine.init;

import com.pieman.caffeine.items.Food;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class Tabs {
    public static final CreativeModeTab CAFFEINE = new CreativeModeTab("caffeine"){
        public ItemStack makeIcon() {
            return new ItemStack(Items.FOOD.get(Food.COFFEE_CHERRIES).get());
        }
    };
}
