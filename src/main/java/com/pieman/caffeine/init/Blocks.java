package com.pieman.caffeine.init;

import com.pieman.caffeine.blocks.DryingMatBlock;
import com.pieman.caffeine.fluids.Coffee;
import net.dries007.tfc.common.TFCItemGroup;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.devices.PlacedItemBlock;
import net.dries007.tfc.common.fluids.Alcohol;
import net.dries007.tfc.common.fluids.FlowingFluidRegistryObject;
import net.dries007.tfc.common.fluids.TFCFluids;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.registry.RegistrationHelpers;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.pieman.caffeine.init.Items.ITEMS;

public class Blocks {
    public static final DeferredRegister<Block> BLOCKS;
    public static final Map<Coffee, RegistryObject<LiquidBlock>> COFFEE;
    public static final RegistryObject<Block> DRYING_MAT;

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier) {
        return register(name, blockSupplier, (Function)null);
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier, CreativeModeTab group) {
        return register(name, blockSupplier, (block) -> {
            return new BlockItem(block, (new Item.Properties()).tab(group));
        });
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier, Item.Properties blockItemProperties) {
        return register(name, blockSupplier, (block) -> {
            return new BlockItem(block, blockItemProperties);
        });
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier, @Nullable Function<T, ? extends BlockItem> blockItemFactory) {
        return RegistrationHelpers.registerBlock(BLOCKS, ITEMS, name, blockSupplier, blockItemFactory);
    }

    static {
        BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "caffeine");
        COFFEE = Helpers.mapOfKeys(Coffee.class, (fluid) -> {
            return register("fluid/" + fluid.getId(), () -> {
                return new LiquidBlock(((FlowingFluidRegistryObject) Fluids.COFFEE.get(fluid)).source(), BlockBehaviour.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops());
            });
        });
        DRYING_MAT = register("drying_mat", () -> {
            return new DryingMatBlock(ExtendedProperties.of(Material.DECORATION).instabreak().sound(SoundType.STEM).noOcclusion().blockEntity(BlockEntities.DRYING_MAT));
        }, TFCItemGroup.MISC);
    }
}
