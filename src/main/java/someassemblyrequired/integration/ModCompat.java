package someassemblyrequired.integration;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import someassemblyrequired.integration.create.CreateCompat;
import someassemblyrequired.integration.farmersdelight.FarmersDelightCompat;
import someassemblyrequired.item.sandwich.SandwichItem;

import java.util.function.Consumer;
import java.util.stream.Stream;

public class ModCompat {

    public static final String FARMERSDELIGHT = "farmersdelight";
    public static final String CREATE = "create";
    public static final String JEI = "jei";
    public static final String MINERSDELIGHT = "minersdelight";
    public static final String SLICE_AND_DICE = "sliceanddice";

    public static void setup(IEventBus modEventBus) {
        if (isFarmersDelightLoaded()) FarmersDelightCompat.setup(modEventBus);
        if (isCreateLoaded()) CreateCompat.setup();
    }

    public static boolean isFarmersDelightLoaded() {
        return isLoaded(FARMERSDELIGHT);
    }

    public static boolean isCreateLoaded() {
        return isLoaded(CREATE);
    }

    public static boolean isJEILoaded() {
        return isLoaded(JEI);
    }

    private static boolean isLoaded(String modid) {
        return ModList.get().isLoaded(modid);
    }

    public static void gatherCreativeTabSandwiches(Consumer<ItemStack> consumer) {
        if (ModCompat.isFarmersDelightLoaded()) {
            FarmersDelightCompat.populateCreativeTab(consumer);
        }
        if (ModCompat.isCreateLoaded()) {
            CreateCompat.populateCreativeTab(consumer);
        }
    }

    public static void gatherJEISandwiches(Consumer<ItemStack> consumer) {
        if (ModCompat.isFarmersDelightLoaded()) {
            FarmersDelightCompat.populateJEI(consumer);
        }
        if (ModCompat.isCreateLoaded()) {
            CreateCompat.populateJEI(consumer);
        }
        Stream.of(
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
                Potions.SLOW_FALLING,
                Potions.WIND_CHARGED,
                Potions.WEAVING,
                Potions.OOZING,
                Potions.INFESTED
        ).map(SandwichItem::makeSandwich).forEach(consumer);
    }
}
