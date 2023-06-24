package com.pieman.caffeine.init;

import net.dries007.tfc.common.TFCEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class Effects {
    public static final DeferredRegister<MobEffect> EFFECTS;
    public static final RegistryObject<MobEffect> CAFFEINE;

    public static <T extends MobEffect> RegistryObject<T> register(String name, Supplier<T> supplier) {
        return EFFECTS.register(name,supplier);
    }

    static {
        EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, "caffeine");
        CAFFEINE = register("caffeine", () -> {
            return (new TFCEffects.TFCMobEffect(MobEffectCategory.BENEFICIAL, 2504281))
                    .addAttributeModifier(Attributes.MOVEMENT_SPEED, "81b630a5-3b60-438f-9c73-728a3427205b", 0.15, AttributeModifier.Operation.MULTIPLY_TOTAL)
                    .addAttributeModifier((Attribute) ForgeMod.SWIM_SPEED.get(), "459dacad-9943-4583-9bba-9206886e3974", 0.15, AttributeModifier.Operation.MULTIPLY_TOTAL);
        });
    }
}
