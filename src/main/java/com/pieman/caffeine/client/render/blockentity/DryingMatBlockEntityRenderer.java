package com.pieman.caffeine.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.pieman.caffeine.blockentities.DryingMatBlockEntity;
import net.dries007.tfc.client.RenderHelpers;
import net.dries007.tfc.common.capabilities.Capabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;

public class DryingMatBlockEntityRenderer <T extends DryingMatBlockEntity> implements BlockEntityRenderer<T> {
    public DryingMatBlockEntityRenderer() {
        System.out.println("Built fuck");
    }

    @Override
    public void render(T blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        blockEntity.getCapability(Capabilities.ITEM).ifPresent((cap) -> {

            float timeD = RenderHelpers.itemTimeRotation();

            poseStack.translate(0.25, 0.25, 0.25);
            poseStack.scale(0.5F, 0.5F, 0.5F);

            for(int i = 0; i < cap.getSlots(); ++i) {
                ItemStack stackx = cap.getStackInSlot(i);
                if (!stackx.isEmpty()) {
                    poseStack.pushPose();
                    poseStack.translate((double)(i % 2 == 0 ? 1 : 0), 3/16f-0.5f, (double)(i < 2 ? 1 : 0));
                    poseStack.mulPose(RenderHelpers.rotateDegreesX(90));
                    itemRenderer.renderStatic(stackx, ItemTransforms.TransformType.FIXED, combinedLight, combinedOverlay, poseStack, buffer, 0);
                    poseStack.popPose();
                }
            }

        });
    }
}
