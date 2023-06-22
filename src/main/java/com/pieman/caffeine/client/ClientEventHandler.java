package com.pieman.caffeine.client;

import com.pieman.caffeine.client.render.blockentity.DryingMatBlockEntityRenderer;
import com.pieman.caffeine.init.BlockEntities;
import com.pieman.caffeine.init.Blocks;
import net.dries007.tfc.client.render.blockentity.PlacedItemBlockEntityRenderer;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;

public class ClientEventHandler {

    public static void init() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(ClientEventHandler::registerEntityRenderers);
        bus.addListener(ClientEventHandler::clientSetup);
    }

    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        System.out.println("REGISTER BLOCK ENTITY FUCK");
        event.registerBlockEntityRenderer((BlockEntityType) BlockEntities.DRYING_MAT.get(), (ctx) -> {
            return new DryingMatBlockEntityRenderer();
        });
    }

    public static void clientSetup(FMLClientSetupEvent event) {
        RenderType solid = RenderType.solid();
        RenderType cutout = RenderType.cutout();
        RenderType cutoutMipped = RenderType.cutoutMipped();
        RenderType translucent = RenderType.translucent();

        ItemBlockRenderTypes.setRenderLayer((Block)Blocks.DRYING_MAT.get(), cutoutMipped);
    }
}
