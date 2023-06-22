package com.pieman.caffeine.blockentities;

import com.pieman.caffeine.init.BlockEntities;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blockentities.InventoryBlockEntity;
import net.dries007.tfc.common.blockentities.PlacedItemBlockEntity;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.capabilities.InventoryItemHandler;
import net.dries007.tfc.common.capabilities.size.ItemSizeManager;
import net.dries007.tfc.common.capabilities.size.Size;
import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

public class DryingMatBlockEntity extends InventoryBlockEntity<ItemStackHandler> {
    public static final int SLOT_LARGE_ITEM = 0;
    private static final Component NAME = Helpers.translatable("tfc.block_entity.placed_item");
    public boolean isHoldingLargeItem;

    public static int getSlotSelected(BlockHitResult rayTrace) {
        Vec3 location = rayTrace.getLocation();
        boolean x = (double)Math.round(location.x) < location.x;
        boolean z = (double)Math.round(location.z) < location.z;
        return (x ? 1 : 0) + (z ? 2 : 0);
    }

    public DryingMatBlockEntity(BlockPos pos, BlockState state) {
        this((BlockEntityType) BlockEntities.DRYING_MAT.get(), pos, state);
    }

    protected DryingMatBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state, (self) -> {
            return new InventoryItemHandler(self, 4);
        }, NAME);
        this.isHoldingLargeItem = false;
    }

    public boolean onRightClick(Player player, ItemStack stack, BlockHitResult rayTrace) {
        Vec3 location = rayTrace.getLocation();
        return this.onRightClick(player, stack, (double)Math.round(location.x) < location.x, (double)Math.round(location.z) < location.z);
    }

    public boolean insertItem(Player player, ItemStack stack, BlockHitResult rayTrace) {
        return this.insertItem(player, stack, getSlotSelected(rayTrace));
    }

    public boolean onRightClick(Player player, ItemStack stack, boolean x, boolean z) {
        int slot = (x ? 1 : 0) + (z ? 2 : 0);
        if (!player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty() && !player.isShiftKeyDown()) {
            if (!stack.isEmpty()) {
                return this.insertItem(player, stack, slot);
            }
        } else {
            ItemStack current;
            if (this.isHoldingLargeItem) {
                current = ((ItemStackHandler)this.inventory).getStackInSlot(0);
            } else {
                current = ((ItemStackHandler)this.inventory).getStackInSlot(slot);
            }

            if (!current.isEmpty()) {
                ItemHandlerHelper.giveItemToPlayer(player, current.copy());
                ((ItemStackHandler)this.inventory).setStackInSlot(this.isHoldingLargeItem ? 0 : slot, ItemStack.EMPTY);
                this.isHoldingLargeItem = false;
                this.updateBlock();
                return true;
            }
        }

        return false;
    }

    public boolean insertItem(Player player, ItemStack stack, int slot) {
        Size size = ItemSizeManager.get(stack).getSize(stack);
        if (Helpers.isItem(stack, TFCTags.Items.PLACED_ITEM_BLACKLIST) || (Boolean) TFCConfig.SERVER.usePlacedItemWhitelist.get() && !Helpers.isItem(stack, TFCTags.Items.PLACED_ITEM_WHITELIST)) {
            return false;
        } else {
            ItemStack input;
            if (size.isEqualOrSmallerThan((Size)TFCConfig.SERVER.maxPlacedItemSize.get()) && !this.isHoldingLargeItem) {
                if (((ItemStackHandler)this.inventory).getStackInSlot(slot).isEmpty()) {
                    if (player.isCreative()) {
                        input = stack.copy();
                        input.setCount(1);
                    } else {
                        input = stack.split(1);
                    }

                    ((ItemStackHandler)this.inventory).setStackInSlot(slot, input);
                    this.updateBlock();
                    return true;
                }
            } else if (!size.isEqualOrSmallerThan((Size)TFCConfig.SERVER.maxPlacedItemSize.get()) && size.isEqualOrSmallerThan((Size)TFCConfig.SERVER.maxPlacedLargeItemSize.get()) && this.isEmpty()) {
                if (player.isCreative()) {
                    input = stack.copy();
                    input.setCount(1);
                } else {
                    input = stack.split(1);
                }

                ((ItemStackHandler)this.inventory).setStackInSlot(0, input);
                this.isHoldingLargeItem = true;
                this.updateBlock();
                return true;
            }

            return false;
        }
    }

    public ItemStack getCloneItemStack(BlockState state, BlockHitResult hit) {
        return ((ItemStackHandler)this.inventory).getStackInSlot(getSlotSelected(hit)).copy();
    }

    public boolean holdingLargeItem() {
        return this.isHoldingLargeItem;
    }

    public void loadAdditional(CompoundTag nbt) {
        this.isHoldingLargeItem = nbt.getBoolean("isHoldingLargeItem");
        super.loadAdditional(nbt);
    }

    public void saveAdditional(CompoundTag nbt) {
        nbt.putBoolean("isHoldingLargeItem", this.isHoldingLargeItem);
        super.saveAdditional(nbt);
    }

    protected void updateBlock() {
        if (this.isEmpty() && this.level != null) {
            this.level.setBlockAndUpdate(this.worldPosition, Blocks.AIR.defaultBlockState());
        } else {
            this.markForBlockUpdate();
        }

    }

    protected boolean isEmpty() {
        if (this.isHoldingLargeItem && ((ItemStackHandler)this.inventory).getStackInSlot(0).isEmpty()) {
            return true;
        } else {
            for(int i = 0; i < 4; ++i) {
                if (!((ItemStackHandler)this.inventory).getStackInSlot(i).isEmpty()) {
                    return false;
                }
            }

            return true;
        }
    }
}
