package someassemblyrequired.data.providers.ingredient;

import com.simibubi.create.AllFluids;
import com.simibubi.create.AllItems;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Items;
import someassemblyrequired.data.providers.Ingredients;

public class CreateIngredients {

    public static void addIngredients(Ingredients ingredients) {
        ingredients.builder(AllItems.BUILDERS_TEA.get())
                .setSpread(0xdf8367, 0.8).setMoistSound();
        ingredients.builder(AllFluids.CHOCOLATE.get().getBucket())
                .setCustomFullName()
                .setSpread(0xad513c)
                .setMoistSound()
                .setFoodProperties(new FoodProperties.Builder().nutrition(6)
                        .saturationModifier(0.3F)
                        .usingConvertsTo(Items.BUCKET)
                        .build());
    }
}
