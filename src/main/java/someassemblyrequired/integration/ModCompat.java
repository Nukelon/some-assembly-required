package someassemblyrequired.integration;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import someassemblyrequired.integration.create.CreateCompat;
import someassemblyrequired.integration.farmersdelight.FarmersDelightCompat;

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
}
