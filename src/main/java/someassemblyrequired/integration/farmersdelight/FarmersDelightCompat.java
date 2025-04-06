package someassemblyrequired.integration.farmersdelight;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import someassemblyrequired.ingredient.Ingredients;
import someassemblyrequired.integration.farmersdelight.ingredient.ConsumableItemBehavior;
import someassemblyrequired.item.sandwich.SandwichItem;
import vectorwing.farmersdelight.common.item.ConsumableItem;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.function.Consumer;

public class FarmersDelightCompat {

    public static void setup(IEventBus modEventBus) {
        modEventBus.addListener(FarmersDelightCompat::onCommonSetup);
    }

    public static ItemStack createBLT() {
        return SandwichItem.makeSandwich(
                someassemblyrequired.registry.ModItems.TOMATO_SLICES.get(),
                ModItems.CABBAGE_LEAF.get(),
                ModItems.COOKED_BACON.get()
        );
    }

    public static ItemStack createBurger() {
        return SandwichItem.makeBurger(
                ModItems.BEEF_PATTY.get(),
                ModItems.CABBAGE_LEAF.get(),
                someassemblyrequired.registry.ModItems.TOMATO_SLICES.get(),
                someassemblyrequired.registry.ModItems.SLICED_ONION.get()
        );
    }

    public static void populateCreativeTab(Consumer<ItemStack> items) {
        items.accept(createBLT());
        items.accept(createBurger());
    }

    public static void populateJEI(Consumer<ItemStack> items) {
        items.accept(SandwichItem.makeToastSandwich(ModItems.FRIED_EGG.get()));
        items.accept(SandwichItem.makeToastSandwich(ModItems.COOKED_BACON.get()));
        items.accept(SandwichItem.makeSandwich(ModItems.COOKED_CHICKEN_CUTS.get()));
        items.accept(SandwichItem.makeBurger(ModItems.HAMBURGER.get()));
    }

    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(
                () -> BuiltInRegistries.ITEM
                        .stream()
                        .filter(item -> item instanceof ConsumableItem)
                        .map(item -> (ConsumableItem) item)
                        .map(ConsumableItemBehavior::new)
                        .forEach(behavior -> Ingredients.addBehavior(behavior.item(), behavior))
        );
    }
}
