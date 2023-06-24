package com.pieman.caffeine.init;

import com.pieman.caffeine.fluids.FluidType;
import com.pieman.caffeine.items.Food;
import net.dries007.tfc.common.TFCItemGroup;
import net.dries007.tfc.common.items.DecayingItem;
import net.dries007.tfc.util.Helpers;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.*;
import java.util.function.Supplier;

public class Items {
    public static final DeferredRegister<Item> ITEMS;
    public static final Map<Food, RegistryObject<Item>> FOOD;
    public static final Map<FluidType, RegistryObject<BucketItem>> FLUID_BUCKETS;
    public static final RegistryObject<Item> USED_COFFEE_GROUNDS;

    private static <T extends Item> RegistryObject<T> register(String name, Supplier<T> item) {
        return ITEMS.register(name.toLowerCase(Locale.ROOT), item);
    }

    private static RegistryObject<Item> register(String name, CreativeModeTab group) {
        return register(name, () -> {
            return new Item((new Item.Properties()).tab(group));
        });
    }

    static {
        ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "caffeine");

        FOOD = Helpers.mapOfKeys(Food.class, (food) ->
            register("food/" + food.name(), () ->
                    food.decays ?
                            new DecayingItem(food.createProperties()):
                            new Item(food.createProperties())
            )
        );

        FLUID_BUCKETS = FluidType.mapOf((fluid) ->
            register("bucket/" + fluid.name(), () ->
                new BucketItem(fluid.fluid(), (new Item.Properties()).craftRemainder(net.minecraft.world.item.Items.BUCKET).stacksTo(1).tab(Tabs.CAFFEINE))
            )
        );

        USED_COFFEE_GROUNDS = register("used_coffee_grounds", Tabs.CAFFEINE);
    }

}
