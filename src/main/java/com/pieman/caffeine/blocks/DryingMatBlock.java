package com.pieman.caffeine.blocks;

import com.pieman.caffeine.blockentities.DryingMatBlockEntity;
import com.pieman.caffeine.init.BlockEntities;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blockentities.PitKilnBlockEntity;
import net.dries007.tfc.common.blockentities.PlacedItemBlockEntity;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.devices.PlacedItemBlock;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class DryingMatBlock extends PlacedItemBlock {
    public DryingMatBlock(ExtendedProperties properties) {
        super(properties);
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        DryingMatBlockEntity dryingMatBlockEntity = (DryingMatBlockEntity)level.getBlockEntity(pos, (BlockEntityType) BlockEntities.DRYING_MAT.get()).orElse((Object)null);
        if (dryingMatBlockEntity != null) {
            ItemStack held = player.getItemInHand(hand);
            return dryingMatBlockEntity.onRightClick(player, held, hit) ? InteractionResult.SUCCESS : InteractionResult.PASS;
        } else {
            return InteractionResult.PASS;
        }
    }
}
