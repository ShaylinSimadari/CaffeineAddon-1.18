package com.pieman.caffeine.init;

import com.pieman.caffeine.blockentities.DryingMatBlockEntity;
import net.dries007.tfc.common.blockentities.PlacedItemBlockEntity;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.util.registry.RegistrationHelpers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class BlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES;

    public static final RegistryObject<BlockEntityType<DryingMatBlockEntity>> DRYING_MAT;

    private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> factory, Supplier<? extends Block> block) {
        return RegistrationHelpers.register(BLOCK_ENTITIES, name, factory, block);
    }

    private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> factory, Stream<? extends Supplier<? extends Block>> blocks) {
        return RegistrationHelpers.register(BLOCK_ENTITIES, name, factory, blocks);
    }

    static {
        BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, "caffeine");

        DRYING_MAT = register("drying_mat", DryingMatBlockEntity::new, (Supplier) Blocks.DRYING_MAT);
    }
}
