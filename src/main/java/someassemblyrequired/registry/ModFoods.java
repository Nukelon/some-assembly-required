package someassemblyrequired.registry;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoods {

    public static final FoodProperties EMPTY = new FoodProperties.Builder().build();

    public static final FoodProperties BREAD_SLICE = new FoodProperties.Builder()
            .nutrition(1).saturationModifier(0.6F).fast().build();

    public static final FoodProperties TOASTED_BREAD_SLICE = new FoodProperties.Builder()
            .nutrition(1).saturationModifier(1F).fast().build();

    public static final FoodProperties BURGER_BUN = new FoodProperties.Builder()
            .nutrition(3).saturationModifier(0.5F).build();

    public static final FoodProperties BURGER_BUN_BOTTOM = new FoodProperties.Builder()
            .nutrition(1).saturationModifier(0.5F).fast().build();

    public static final FoodProperties BURGER_BUN_TOP = new FoodProperties.Builder().nutrition(2)
            .saturationModifier(0.5F).fast().build();

    public static final FoodProperties APPLE_SLICES = new FoodProperties.Builder()
            .nutrition(2).saturationModifier(0.3F).fast().build();

    public static final FoodProperties GOLDEN_APPLE_SLICES = new FoodProperties.Builder()
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 50, 1), 1)
            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 1200, 0), 1)
            .nutrition(2).saturationModifier(1.2F).fast().alwaysEdible().build();

    public static final FoodProperties ENCHANTED_GOLDEN_APPLE_SLICES = new FoodProperties.Builder()
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 200, 1), 1)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 3000, 0), 1)
            .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 3000, 0), 1)
            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 1200, 3), 1)
            .nutrition(2).saturationModifier(1.2F).fast().alwaysEdible().build();

    public static final FoodProperties CHOPPED_CARROT = new FoodProperties.Builder()
            .nutrition(2).saturationModifier(0.6F).fast().build();

    public static final FoodProperties CHOPPED_GOLDEN_CARROT = new FoodProperties.Builder()
            .nutrition(3).saturationModifier(1.2F).fast().build();

    public static final FoodProperties CHOPPED_BEETROOT = new FoodProperties.Builder()
            .nutrition(1).saturationModifier(0.8F).fast().build();

    public static final FoodProperties TOMATO_SLICES = new FoodProperties.Builder()
            .nutrition(1).saturationModifier(1F).fast().build();

    public static final FoodProperties RAW_BURGER_BUN = new FoodProperties.Builder()
            .effect(() -> new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.3F)
            .nutrition(2).saturationModifier(0.3F).build();

    public static final FoodProperties SLICED_ONION = new FoodProperties.Builder()
            .nutrition(1).saturationModifier(1F).fast().build();
}
