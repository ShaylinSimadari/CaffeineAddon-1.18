package com.pieman.caffeine.compat.jade.common;

import com.pieman.caffeine.blockentities.DryingMatBlockEntity;
import com.pieman.caffeine.blocks.DryingMatBlock;
import net.dries007.tfc.common.blockentities.BarrelBlockEntity;
import net.dries007.tfc.common.blocks.devices.BarrelBlock;
import net.dries007.tfc.common.recipes.BarrelRecipe;
import net.dries007.tfc.compat.jade.common.BlockEntityTooltip;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.calendar.Calendars;
import net.dries007.tfc.util.calendar.ICalendar;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;

import java.util.function.BiConsumer;

public final class BlockEntityTooltips {

//    public static final BlockEntityTooltip DRYING_MAT = (level, state, pos, entity, tooltip) -> {
//        if (state.getBlock() instanceof DryingMatBlock && entity instanceof DryingMatBlockEntity barrel) {
//            tooltip.accept(Component.nullToEmpty("dn"));
////            if ((Boolean)state.getValue(BarrelBlock.SEALED)) {
////                BarrelRecipe recipe = barrel.getRecipe();
////                if (recipe != null) {
////                    tooltip.accept(recipe.getTranslationComponent());
//////                    tooltip.accept(Helpers.translatable("tfc.jade.sealed_date", new Object[]{ICalendar.getTimeAndDate(Calendars.get(level).ticksToCalendarTicks(barrel.getSealedTick()), (long)Calendars.get(level).getCalendarDaysInMonth())}));
////                }
////            }
//        }
//
//    };
//
//    public static void register(BiConsumer<BlockEntityTooltip, Class<? extends Block>> registerBlock) {
//        registerBlock.accept(DRYING_MAT, DryingMatBlock.class);
//    }
}
