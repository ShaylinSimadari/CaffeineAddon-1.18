package com.pieman.caffeine;

import com.mojang.logging.LogUtils;
import com.pieman.caffeine.init.*;
import com.pieman.caffeine.fluids.client.ClientEventHandler;
import com.pieman.caffeine.recipes.RecipeSerializers;
import com.pieman.caffeine.recipes.RecipeTypes;
import com.pieman.caffeine.util.climate.ClimateRanges;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("caffeine")
public class CaffeineAddon
{
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
//TODO jei, jade, pathouli compat
    //TODO tea. needs to be functionally different
    //Bees
    //Butchery
    //pottery + glazing
    //light roast and dark roat where light roast more coffee but harder to brew somehow
    public CaffeineAddon()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(this::setup);
        bus.addListener(this::enqueueIMC);
        bus.addListener(this::processIMC);

        Items.ITEMS.register(bus);
        Blocks.BLOCKS.register(bus);
        Fluids.FLUIDS.register(bus);
        BlockEntities.BLOCK_ENTITIES.register(bus);
        RecipeTypes.RECIPE_TYPES.register(bus);
        RecipeSerializers.RECIPE_SERIALIZERS.register(bus);
        Features.FEATURES.register(bus);
        Effects.EFFECTS.register(bus);
        new ClimateRanges();

        if (FMLEnvironment.dist == Dist.CLIENT) {
            ClientEventHandler.init();
        }

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }
//TODO limoncello
    private void setup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {
            Blocks.registerFlowerPotFlowers();
        }).exceptionally((e) -> {
            LOGGER.error("An unhandled exception was thrown during synchronous mod loading:", e);
//            this.syncLoadError = e;
            return null;
        });
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // Some example code to dispatch IMC to another mod
        InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // Some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.messageSupplier().get()).
                collect(Collectors.toList()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents
    {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent)
        {
            // Register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }
}
