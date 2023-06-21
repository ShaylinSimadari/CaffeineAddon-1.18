package com.pieman.caffeine.items;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.dries007.tfc.common.TFCItemGroup;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.function.Supplier;

public enum Food {
    COFFEE_CHERRIES(false, true, () -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 0), true),
    COFFEE_CHERRIES_DRIED(false, false, () -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 0), true),
    COFFEE_BEANS_GREEN(false, false, () -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 0), true),
    COFFEE_BEANS(false, false, () -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 0), false),
    COFFEE_GROUND(false, false, () -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 0), true);

    private final boolean meat;
    private final boolean fast;
    private final List<Pair<Supplier<MobEffectInstance>, Float>> effects;
    public final boolean decays;

//    private final static MobEffectInstance SHORT_SPEED_EFFECT = new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 0);

    Food(boolean meat, boolean fast, Supplier<MobEffectInstance> effect, boolean decays) {
        this.meat = meat;
        this.fast = fast;
        this.effects = Lists.newArrayList(new Pair<Supplier<MobEffectInstance>, Float>(effect, 1f));
        this.decays = decays;
    }

    public FoodProperties getFoodProperties() {
        FoodProperties.Builder builder = new FoodProperties.Builder();
        if (this.meat) {
            builder.meat();
        }

        if (this.fast) {
            builder.fast();
        }

        if(effects != null) {
            for (Pair<Supplier<MobEffectInstance>, Float> effect : effects) {
                builder.effect(effect.getFirst(), effect.getSecond());
            }
        }

        return builder.nutrition(4).saturationMod(0.3F).build();
    }

    public Item.Properties createProperties() {
        Item.Properties props = (new Item.Properties()).food(this.getFoodProperties());
        props.tab(TFCItemGroup.FOOD);
        return props;
    }
}
