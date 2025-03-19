package someassemblyrequired.data.providers.ingredient;

import com.simibubi.create.AllFluids;
import com.simibubi.create.AllItems;
import someassemblyrequired.data.providers.Ingredients;

public class CreateIngredients {

    @SuppressWarnings("deprecation")
    public static void addIngredients(Ingredients ingredients) {
        ingredients.builder(AllItems.BUILDERS_TEA.get()).setBottled().setSpread(0xdf8367).setMoistSound();
        ingredients.builder(AllFluids.CHOCOLATE.get().getBucket()).setCustomFullName().setBucketed().setSpread(0xad513c).setMoistSound().setFoodProperties(AllItems.BAR_OF_CHOCOLATE.get().getFoodProperties());
    }
}
