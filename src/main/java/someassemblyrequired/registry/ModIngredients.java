package someassemblyrequired.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.ingredient.IngredientProperties;
import someassemblyrequired.ingredient.IngredientPropertiesBase;
import someassemblyrequired.ingredient.PotionProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ModIngredients {

    public static final ResourceKey<Registry<IngredientProperties>> INGREDIENTS = ResourceKey.createRegistryKey(SomeAssemblyRequired.id("ingredients"));

    private static final IngredientPropertiesBase DEFAULT_PROPERTIES = new IngredientProperties(
            null, Optional.empty(), Optional.empty(), Optional.empty(),
            ItemStack.EMPTY, ModSoundEvents.ADD_ITEM, 1, true, false
    );

    private static final Map<Item, IngredientPropertiesBase> properties = new HashMap<>();

    public static void onBake(Registry<IngredientProperties> registry) {
        registry.stream().forEachOrdered(ingredient -> {
            Item item = ingredient.item().value();
            if (properties.containsKey(item)) {
                SomeAssemblyRequired.LOGGER.error("Multiple ingredients found for item {}", BuiltInRegistries.ITEM.getKey(item));
            }
            properties.put(item, ingredient);
        });
        properties.put(Items.POTION, new PotionProperties());
    }

    public static boolean hasIngredientFor(Item item) {
        return properties.containsKey(item);
    }

    @Nullable
    public static IngredientPropertiesBase get(ItemStack item) {
        return properties.get(item.getItem());
    }

    public static IngredientPropertiesBase getOrDefault(ItemStack item) {
        if (get(item) != null) {
            return get(item);
        }
        return DEFAULT_PROPERTIES;
    }
}
