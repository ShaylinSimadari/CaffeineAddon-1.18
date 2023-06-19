package com.pieman.caffeine;

import com.pieman.caffeine.init.Items;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class RegistryHandler {

    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
//        event.getRegistry().register("coffee_cherries", Items.CoffeeCherries);
//        event.getRegistry().registerAll(Items.ITEMS.toArray(new Item[0]));
//        event.getRegistry().registerAll(ModItems.ITEMBLOCKS.toArray(new Item[0]));
//        OreDictionaryCompat.register();
    }

}
