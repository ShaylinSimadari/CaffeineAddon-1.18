package com.pieman.caffeine.compat.jade;

import mcp.mobius.waila.api.IWailaClientRegistration;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;
import net.dries007.tfc.compat.jade.common.BlockEntityTooltip;
import net.dries007.tfc.compat.jade.common.BlockEntityTooltips;
import net.dries007.tfc.compat.jade.common.EntityTooltips;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;

//@WailaPlugin
public class JadeIntegration implements IWailaPlugin {

//    public void registerClient(IWailaClientRegistration registry) {
//        BlockEntityTooltips.register((tooltip, aClass) -> {
//            this.register(registry, tooltip, aClass);
//        });
//    }
//
//    private void register(IWailaClientRegistration registry, BlockEntityTooltip blockEntityTooltip, Class<? extends Block> blockClass) {
//        registry.registerComponentProvider((tooltip, access, config) -> {
//            Level level = access.getLevel();
//            BlockState state = access.getBlockState();
//            BlockPos pos = access.getPosition();
//            BlockEntity entity = access.getBlockEntity();
//            Objects.requireNonNull(tooltip);
//            blockEntityTooltip.display(level, state, pos, entity, tooltip::add);
//        }, TooltipPosition.BODY, blockClass);
//    }
}
