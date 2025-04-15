package someassemblyrequired.integration;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.fml.ModList;
import someassemblyrequired.integration.create.CreateCompat;
import someassemblyrequired.integration.diet.DietCompat;
import someassemblyrequired.integration.farmersdelight.FarmersDelightCompat;
import someassemblyrequired.item.sandwich.SandwichItem;
import someassemblyrequired.registry.ModItems;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ModCompat {

    public static final String FARMERSDELIGHT = "farmersdelight";
    public static final String CREATE = "create";
    public static final String DIET = "diet";
    public static final String JEI = "jei";
    public static final String MINERS_DELIGHT = "miners_delight";
    public static final String SLICE_AND_DICE = "sliceanddice";

    public static final List<Potion> EXAMPLE_POTIONS = List.of(
            Potions.MUNDANE,
            Potions.THICK,
            Potions.AWKWARD,
            Potions.NIGHT_VISION,
            Potions.INVISIBILITY,
            Potions.LEAPING,
            Potions.FIRE_RESISTANCE,
            Potions.SWIFTNESS,
            Potions.SLOWNESS,
            Potions.TURTLE_MASTER,
            Potions.WATER_BREATHING,
            Potions.HEALING,
            Potions.HARMING,
            Potions.POISON,
            Potions.REGENERATION,
            Potions.STRENGTH,
            Potions.WEAKNESS,
            Potions.SLOW_FALLING
    );

    public static void setup() {
        if (isFarmersDelightLoaded()) FarmersDelightCompat.setup();
        if (isCreateLoaded()) CreateCompat.setup();
        if (isDietLoaded()) DietCompat.setup();
    }

    public static boolean isFarmersDelightLoaded() {
        return isLoaded(FARMERSDELIGHT);
    }

    public static boolean isCreateLoaded() {
        return isLoaded(CREATE);
    }

    public static boolean isDietLoaded() {
        return isLoaded(DIET);
    }

    public static boolean isJEILoaded() {
        return isLoaded(JEI);
    }

    public static boolean isMinersDelightLoaded() {
        return isLoaded(MINERS_DELIGHT);
    }

    private static boolean isLoaded(String modid) {
        return ModList.get().isLoaded(modid);
    }

    public static void gatherCreativeTabSandwiches(Consumer<ItemStack> consumer) {
        if (ModCompat.isFarmersDelightLoaded()) {
            FarmersDelightCompat.populateCreativeTab(consumer);
        }
    }

    public static void gatherJEISandwiches(Consumer<ItemStack> consumer) {
        if (ModCompat.isFarmersDelightLoaded()) {
            FarmersDelightCompat.populateJEI(consumer);
        }
        if (ModCompat.isCreateLoaded()) {
            CreateCompat.populateJEI(consumer);
        }
        EXAMPLE_POTIONS.stream().map(SandwichItem::makeSandwich).forEach(consumer);
        Stream.of(
                ModItems.APPLE_SLICES.get(),
                ModItems.CHOPPED_CARROT.get(),
                ModItems.CHOPPED_BEETROOT.get(),
                ModItems.TOMATO_SLICES.get()
        ).map(SandwichItem::makeSandwich).forEach(consumer);
    }
}
