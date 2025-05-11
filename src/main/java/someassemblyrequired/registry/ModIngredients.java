package someassemblyrequired.registry;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.Nullable;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.ingredient.IngredientProperties;
import someassemblyrequired.ingredient.IngredientPropertiesBase;
import someassemblyrequired.ingredient.PotionProperties;
import someassemblyrequired.network.UpdateIngredientsPacket;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

// TODO simplify by using a data map
public class ModIngredients {

    public static final ResourceKey<Registry<IngredientProperties>> INGREDIENTS = ResourceKey.createRegistryKey(SomeAssemblyRequired.id("ingredients"));

    private static final IngredientPropertiesBase DEFAULT_PROPERTIES = new IngredientProperties(
            null, Optional.empty(), Optional.empty(), Optional.empty(),
            ItemStack.EMPTY, ModSoundEvents.ADD_ITEM, 1, true, false
    );

    private static final Map<Item, IngredientPropertiesBase> PROPERTIES = new HashMap<>();

    public static void onDataPackSync(OnDatapackSyncEvent event) {
        if (event.getPlayer() == null) {
            if (ServerLifecycleHooks.getCurrentServer() != null) {
                ServerLifecycleHooks.getCurrentServer()
                        .registryAccess()
                        .registry(ModIngredients.INGREDIENTS)
                        .map(Registry::asLookup)
                        .ifPresent(ModIngredients::refresh);
            }
        }
        event.getRelevantPlayers().forEach(player -> PacketDistributor.sendToPlayer(player, UpdateIngredientsPacket.INSTANCE));
    }

    public static void refresh(HolderLookup.RegistryLookup<IngredientProperties> registry) {
        PROPERTIES.clear();
        registry.listElements().forEachOrdered(ingredient -> {
            Item item = ingredient.value().item().value();
            if (PROPERTIES.containsKey(item)) {
                SomeAssemblyRequired.LOGGER.error("Multiple ingredients found for item {}", BuiltInRegistries.ITEM.getKey(item));
            }
            PROPERTIES.put(item, ingredient.value());
        });
        PROPERTIES.put(Items.POTION, new PotionProperties());
    }

    @Nullable
    public static IngredientPropertiesBase get(ItemStack item) {
        return PROPERTIES.get(item.getItem());
    }

    public static IngredientPropertiesBase getOrDefault(ItemStack item) {
        if (get(item) != null) {
            return get(item);
        }
        return DEFAULT_PROPERTIES;
    }
}
