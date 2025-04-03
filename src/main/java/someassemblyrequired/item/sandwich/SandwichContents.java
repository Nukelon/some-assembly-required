package someassemblyrequired.item.sandwich;

import com.mojang.serialization.Codec;
import net.minecraft.ResourceLocationException;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import someassemblyrequired.config.ModConfig;
import someassemblyrequired.ingredient.Ingredients;
import someassemblyrequired.registry.ModDataComponents;
import someassemblyrequired.registry.ModItems;
import someassemblyrequired.registry.ModTags;

import java.util.*;

public record SandwichContents(List<ItemStack> items) {

    public static final SandwichContents EMPTY = new SandwichContents(List.of());

    public static final Codec<SandwichContents> CODEC = ItemStack.CODEC.listOf(0, 32).xmap(SandwichContents::new, SandwichContents::items);
    public static final StreamCodec<RegistryFriendlyByteBuf, SandwichContents> STREAM_CODEC = ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()).map(SandwichContents::new, SandwichContents::items);

    public SandwichContents(List<ItemStack> items) {
        this.items = items.stream().map(ItemStack::copy).toList();
    }

    public static SandwichContents get(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.SANDWICH_CONTENTS, EMPTY);
    }

    public static Optional<SandwichContents> maybeGet(ItemStack stack) {
        return Optional.ofNullable(stack.get(ModDataComponents.SANDWICH_CONTENTS));
    }

    public SandwichContents dropLast() {
        List<ItemStack> items = new ArrayList<>(this.items);
        items.removeLast();
        return new SandwichContents(items);
    }

    public SandwichContents concat(List<ItemStack> toAdd) {
        if (toAdd.stream().anyMatch(ItemStack::isEmpty)) {
            throw new IllegalArgumentException("Cannot add empty item to sandwich");
        }
        List<ItemStack> items = new ArrayList<>(this.items);
        toAdd.stream()
                .map(ItemStack::copy)
                .peek(stack -> stack.setCount(1))
                .forEach(items::add);
        return new SandwichContents(items);
    }

    public int getTotalHeight() {
        int size = 0;
        for (ItemStack item : items) {
            size += Ingredients.getHeight(item);
        }
        return size;
    }

    public int nutrition(@Nullable LivingEntity entity) {
        int result = 0;
        for (ItemStack stack : items) {
            result += Ingredients.getFood(stack, entity).nutrition();
        }
        return result;
    }

    public float saturation(@Nullable LivingEntity entity) {
        float result = 0;
        for (ItemStack stack : items) {
            result += Ingredients.getFood(stack, entity).saturation();
        }
        return result;
    }

    public boolean isBurger() {
        return !items.isEmpty()
                && items.getFirst().is(ModTags.BURGER_BUNS)
                && items.getLast().is(ModTags.BURGER_BUNS);
    }

    public boolean hasTopAndBottomBread() {
        return !items.isEmpty()
                && items.getFirst().is(ModTags.SANDWICH_BREAD)
                && items.getLast().is(ModTags.SANDWICH_BREAD);
    }

    public boolean isDoubleDecker() {
        if (items.size() < 5 || !hasTopAndBottomBread()
                || items.get(1).is(ModTags.SANDWICH_BREAD)
                || items.get(items.size() - 2).is(ModTags.SANDWICH_BREAD)
        ) {
            return false;
        }

        int breadCount = 0;
        for (ItemStack item : items) {
            if (item.is(ModTags.SANDWICH_BREAD)) {
                breadCount++;
            }
        }
        return breadCount == 3;
    }

    public FoodProperties createFoodProperties(@Nullable LivingEntity entity) {
        int nutrition = nutrition(entity);
        float saturationModifier = 0.5F * saturation(entity) / nutrition;
        FoodProperties.Builder builder = new FoodProperties.Builder()
                .nutrition(nutrition)
                .saturationModifier(saturationModifier);

        Set<Item> uniqueIngredients = new HashSet<>();
        for (ItemStack stack : items) {
            FoodProperties food = Ingredients.getFood(stack, entity);
            if (food.nutrition() > 0 && food.effects().isEmpty() && !stack.is(ModTags.SANDWICH_BREAD)) {
                uniqueIngredients.add(stack.getItem());
            } else {
                for (FoodProperties.PossibleEffect effect : food.effects()) {
                    builder.effect(effect::effect, effect.probability());
                }
            }
        }

        if (ModConfig.serverSpec.isLoaded()) {
            addBonusEffect(builder, uniqueIngredients.size());
        }
        return builder.build();
    }

    private void addBonusEffect(FoodProperties.Builder builder, int uniqueIngredientCount) {
        String effectName = isBurger() ? ModConfig.server.burgerBonusEffect.get() : ModConfig.server.sandwichBonusEffect.get();
        List<Integer> durations = isBurger() ? ModConfig.server.burgerEffectDurations.get() : ModConfig.server.sandwichEffectDurations.get();
        uniqueIngredientCount = Math.min(durations.size() - 1, uniqueIngredientCount);
        ResourceLocation effectId;
        try {
            effectId = ResourceLocation.parse(effectName);
        } catch (ResourceLocationException e) {
            return;
        }
        Optional<? extends Holder<MobEffect>> effect = BuiltInRegistries.MOB_EFFECT.getHolder(effectId);
        if (effect.isPresent() && !durations.isEmpty()) {
            int duration = durations.get(uniqueIngredientCount);
            if (duration > 0) {
                builder.effect(() -> new MobEffectInstance(effect.get(), duration * 20, 0), 1F);
            }
        }
    }

    public ItemStack makeItem() {
        if (items.isEmpty()) {
            return ItemStack.EMPTY;
        } else if (items.size() == 1) {
            return items.getFirst().copy();
        } else {
            ItemStack result = new ItemStack(ModItems.SANDWICH.get());
            result.set(ModDataComponents.SANDWICH_CONTENTS.get(), this);
            return result;
        }
    }
}
