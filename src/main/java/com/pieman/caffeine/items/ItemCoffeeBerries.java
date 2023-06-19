package com.pieman.caffeine.items;

import net.dries007.tfc.common.capabilities.food.FoodHandler;
import net.dries007.tfc.common.items.DecayingItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

public class ItemCoffeeBerries extends DecayingItem {
	public ItemCoffeeBerries(Item.Properties properties) {
		super(properties);
	}

	public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
		return new FoodHandler.Dynamic();
	}
}
