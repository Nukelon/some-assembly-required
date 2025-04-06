package someassemblyrequired.ingredient;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.ingredient.behavior.*;
import someassemblyrequired.registry.ModIngredients;

import java.util.HashMap;

public class Ingredients {

    private static final HashMap<Item, IngredientBehavior> INGREDIENT_BEHAVIORS = new HashMap<>();

    public static void addBehavior(Item item, IngredientBehavior properties) {
        if (INGREDIENT_BEHAVIORS.get(item) != null) {
            SomeAssemblyRequired.LOGGER.error("Multiple ingredient behaviors for item {}", BuiltInRegistries.ITEM.getKey(item));
        } else {
            INGREDIENT_BEHAVIORS.put(item, properties);
        }
    }

    public static void addBehaviors() {
        addBehavior(Items.CHORUS_FRUIT, new ChorusFruitBehavior());
        addBehavior(Items.SUSPICIOUS_STEW, new SuspiciousStewBehavior());
        addBehavior(Items.MILK_BUCKET, new MilkBucketBehavior());
        addBehavior(Items.HONEY_BOTTLE, new HoneyBottleBehavior());
    }

    @SuppressWarnings("ConstantConditions")
    public static boolean canAddToSandwich(ItemStack item) {
        return !item.isEmpty() && (item.getFoodProperties(null) != null || ModIngredients.get(item) != null && !ModIngredients.get(item).hidden());
    }

    public static @NotNull FoodProperties getFood(ItemStack item, @Nullable LivingEntity entity) {
        return ModIngredients.getOrDefault(item).getFood(item, entity);
    }

    public static void applyIngredientBehaviours(ItemStack item, LivingEntity entity) {
        if (INGREDIENT_BEHAVIORS.containsKey(item.getItem())) {
            INGREDIENT_BEHAVIORS.get(item.getItem()).onEaten(item, entity);
        }
    }

    public static Component getDisplayName(ItemStack item) {
        return ModIngredients.getOrDefault(item).getDisplayName(item);
    }

    public static Component getFullName(ItemStack item) {
        return ModIngredients.getOrDefault(item).getFullName(item);
    }

    public static ItemStack getDisplayItem(ItemStack item) {
        return ModIngredients.getOrDefault(item).getDisplayItem(item);
    }

    public static int getHeight(ItemStack item) {
        return ModIngredients.getOrDefault(item).height();
    }

    public static boolean shouldRenderAsItem(ItemStack item) {
        return ModIngredients.getOrDefault(item).renderAsItem();
    }

    public static void playApplySound(ItemStack item, Level level, @Nullable Player player, BlockPos pos) {
        ModIngredients.getOrDefault(item).playSound(level, player, pos, 1);
    }

    public static void playRemoveSound(ItemStack item, Level level, @Nullable Player player, BlockPos pos) {
        ModIngredients.getOrDefault(item).playSound(level, player, pos, 1.2F);
    }
}
