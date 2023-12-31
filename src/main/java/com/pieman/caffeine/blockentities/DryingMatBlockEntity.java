package com.pieman.caffeine.blockentities;

import com.pieman.caffeine.init.BlockEntities;
import com.pieman.caffeine.recipes.DryingMatRecipe;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blockentities.*;
import net.dries007.tfc.common.capabilities.InventoryItemHandler;
import net.dries007.tfc.common.capabilities.size.ItemSizeManager;
import net.dries007.tfc.common.capabilities.size.Size;
import net.dries007.tfc.common.recipes.LoomRecipe;
import net.dries007.tfc.common.recipes.inventory.ItemStackInventory;
import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

public class DryingMatBlockEntity extends TickableInventoryBlockEntity<ItemStackHandler> {
    public static final int SLOT_LARGE_ITEM = 0;
    private static final Component NAME = Helpers.translatable("caffeine.block_entity.drying_mat");
    public boolean isHoldingLargeItem;
    //TODO Replace this shit with a placeable coffee cherries

    //TODO might not be a well optimized solution
    // empty mats should be just block, no TE
    private int[] timers = new int[]{0,0,0,0};
    private DryingMatRecipe[] recipes = new DryingMatRecipe[]{null, null, null, null};

    public static void serverTick(Level level, BlockPos pos, BlockState state, DryingMatBlockEntity drying_mat) {
        if(level.isRaining()) {
            for(int i = 0; i < 4; i++) {
                if (drying_mat.recipes[i] != null) {
                    drying_mat.timers[i] = drying_mat.recipes[i].getDuration();
                }
            }
            return;
        }
        if(level.canSeeSky(pos) || level.getRawBrightness(pos, 0) > 12) {
            for (int i = 0; i < 4; i++) {
                if (drying_mat.timers[i] > 0) {
                    --drying_mat.timers[i];
                    if (drying_mat.timers[i] == 0) {
                        drying_mat.finishDrying(i);
                        drying_mat.setAndUpdateSlots(i);
                    }
                }
            }
        }
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, DryingMatBlockEntity drying_mat) {
    }

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
                boolean r =  this.insertItem(player, stack, slot);
                startDrying(slot);
                return r;
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
                    this.updateCachedRecipe(slot);
                    this.updateBlock();
                    return true;
                }
            } else if (!size.isEqualOrSmallerThan((Size)TFCConfig.SERVER.maxPlacedItemSize.get()) && size.isEqualOrSmallerThan((Size)TFCConfig.SERVER.maxPlacedLargeItemSize.get()) && this.isEmpty()) {
                if (player.isCreative()) {
                    input = stack.copy();
                    input.setCount(1);
                } else {
                    input = stack.split(0);
                }

                ((ItemStackHandler)this.inventory).setStackInSlot(0, input);
                this.updateCachedRecipe(0);
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
        this.timers = nbt.getIntArray("timers");
        super.loadAdditional(nbt);
        this.updateCachedRecipes();
    }

    public void saveAdditional(CompoundTag nbt) {
        nbt.putBoolean("isHoldingLargeItem", this.isHoldingLargeItem);
        nbt.putIntArray("timers", this.timers);
        super.saveAdditional(nbt);
    }

    protected void updateBlock() {
        this.markForBlockUpdate();
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

    public boolean startDrying(int slot) {
        assert this.level != null;

        ItemStack inputStack = ((ItemStackHandler)this.inventory).getStackInSlot(slot);
        if (!inputStack.isEmpty()) {
            ItemStackInventory wrapper = new ItemStackInventory(inputStack);
            DryingMatRecipe recipe = DryingMatRecipe.getRecipe(this.level, wrapper);
            if (recipe != null && recipe.matches(wrapper, this.level)) {
                //TODO this time value should be gotten from the recipe data
                this.timers[slot] = recipe.getDuration();
                this.recipes[slot] = recipe;
                this.markForSync();
                return true;
            }
        }
        return false;
    }

    private void finishDrying(int slot) {
        assert this.level != null;

        ItemStack inputStack = ((ItemStackHandler)this.inventory).getStackInSlot(slot);
        if (!inputStack.isEmpty()) {
            ItemStackInventory wrapper = new ItemStackInventory(inputStack);
            DryingMatRecipe recipe = DryingMatRecipe.getRecipe(this.level, wrapper);
            if (recipe != null && recipe.matches(wrapper, this.level)) {
                ItemStack outputStack = recipe.assemble(wrapper);

                inputStack.shrink(1);

                outputStack = Helpers.mergeInsertStack(this.inventory, slot, outputStack);
                if (!outputStack.isEmpty() && !this.level.isClientSide) {
                    Helpers.spawnItem(this.level, this.worldPosition, outputStack);
                }
                this.recipes[slot] = null;
                this.timers[slot] = 0;
                this.updateBlock();
                this.markForSync();
            }
        }
    }

    private void updateCachedRecipe(int slot) {
        assert this.level != null;
        this.recipes[slot] = DryingMatRecipe.getRecipe(this.level, ((ItemStackHandler)this.inventory).getStackInSlot(slot));
    }

    private void updateCachedRecipes() {
        for (int i = 0; i < 4; i++){
            updateCachedRecipe(i);
        }
    }
}
