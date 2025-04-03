package someassemblyrequired.registry;

import com.mojang.datafixers.util.Unit;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.integration.ModCompat;
import someassemblyrequired.integration.farmersdelight.FarmersDelightCompat;
import someassemblyrequired.item.sandwich.SandwichItem;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, SomeAssemblyRequired.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SomeAssemblyRequired.MOD_ID);

    public static DeferredHolder<CreativeModeTab, CreativeModeTab> CREATIVE_TAB = CREATIVE_MODE_TABS.register("main", () -> CreativeModeTab.builder()
            .icon(ModItems::makeIcon)
            .title(Component.translatable("%s.creative_tab".formatted(SomeAssemblyRequired.MOD_ID)))
            .displayItems(ModItems::fillCreativeTab)
            .build()
    );

    private static ItemStack makeIcon() {
        return new ItemStack(BREAD_SLICE.get());
    }

    private static void fillCreativeTab(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        BuiltInRegistries.ITEM
                .stream()
                .filter(item -> {
                    String namespace = BuiltInRegistries.ITEM.getKey(item).getNamespace();
                    return namespace.equals(SomeAssemblyRequired.MOD_ID);
                })
                .filter(item -> item != SPREAD.get())
                .filter(item -> item != SANDWICH.get())
                .forEach(output::accept);

        addSandwiches(output::accept);
    }

    public static void addSandwiches(Consumer<ItemStack> creativeTab) {
        ItemStack sandwich;
        ItemStack burger;
        if (ModCompat.isFarmersDelightLoaded()) {
            sandwich = FarmersDelightCompat.createBLT();
            burger = FarmersDelightCompat.createBurger();
            sandwich.set(ModDataComponents.JEI_EXAMPLE, Unit.INSTANCE);
            burger.set(ModDataComponents.JEI_EXAMPLE, Unit.INSTANCE);
            creativeTab.accept(sandwich);
            creativeTab.accept(burger);
        }
    }

    // sandwich assembly tables
    public static final DeferredHolder<Item, Item> SANDWICHING_STATION = ITEMS.register("sandwiching_station", () -> createBlockItem(ModBlocks.SANDWICHING_STATION.get()));
    // foods
    public static final DeferredHolder<Item, Item> BREAD_SLICE = ITEMS.register("bread_slice", () -> createFoodItem(ModFoods.BREAD_SLICE));
    public static final DeferredHolder<Item, Item> TOASTED_BREAD_SLICE = ITEMS.register("toasted_bread_slice", () -> createFoodItem(ModFoods.TOASTED_BREAD_SLICE));
    public static final DeferredHolder<Item, Item> BURGER_BUN = ITEMS.register("burger_bun", () -> createFoodItem(ModFoods.BURGER_BUN));
    public static final DeferredHolder<Item, Item> BURGER_BUN_BOTTOM = ITEMS.register("burger_bun_bottom", () -> createFoodItem(ModFoods.BURGER_BUN_BOTTOM));
    public static final DeferredHolder<Item, Item> BURGER_BUN_TOP = ITEMS.register("burger_bun_top", () -> createFoodItem(ModFoods.BURGER_BUN_TOP));
    public static final DeferredHolder<Item, Item> APPLE_SLICES = ITEMS.register("apple_slices", () -> createFoodItem(ModFoods.APPLE_SLICES));
    public static final DeferredHolder<Item, Item> GOLDEN_APPLE_SLICES = ITEMS.register("golden_apple_slices", () -> new Item(new Item.Properties().food(ModFoods.GOLDEN_APPLE_SLICES).rarity(Rarity.RARE)));
    public static final DeferredHolder<Item, Item> ENCHANTED_GOLDEN_APPLE_SLICES = ITEMS.register("enchanted_golden_apple_slices", () -> new Item(new Item.Properties().food(ModFoods.ENCHANTED_GOLDEN_APPLE_SLICES).component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true).rarity(Rarity.EPIC)));
    public static final DeferredHolder<Item, Item> CHOPPED_CARROT = ITEMS.register("chopped_carrot", () -> createFoodItem(ModFoods.CHOPPED_CARROT));
    public static final DeferredHolder<Item, Item> CHOPPED_GOLDEN_CARROT = ITEMS.register("chopped_golden_carrot", () -> createFoodItem(ModFoods.CHOPPED_GOLDEN_CARROT));
    public static final DeferredHolder<Item, Item> CHOPPED_BEETROOT = ITEMS.register("chopped_beetroot", () -> createFoodItem(ModFoods.CHOPPED_BEETROOT));
    public static final DeferredHolder<Item, Item> TOMATO_SLICES = ITEMS.register("tomato_slices", () -> createFoodItem(ModFoods.TOMATO_SLICES));
    public static final DeferredHolder<Item, Item> SLICED_ONION = ITEMS.register("sliced_onion", () -> createFoodItem(ModFoods.SLICED_ONION));

    // misc items
    public static final DeferredHolder<Item, SandwichItem> SANDWICH = ITEMS.register("sandwich", () -> new SandwichItem(ModBlocks.SANDWICH.get(), new Item.Properties().stacksTo(16).food(new FoodProperties.Builder().build())));
    public static final DeferredHolder<Item, Item> SPREAD = ITEMS.register("spread", () -> new Item(new Item.Properties().component(ModDataComponents.SPREAD_COLOR.get(), 0xFFFFFFFF)));

    private static Item createBlockItem(Block block) {
        return new BlockItem(block, new Item.Properties());
    }

    private static Item createFoodItem(FoodProperties food) {
        return new Item(new Item.Properties().food(food));
    }
}
