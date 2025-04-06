package someassemblyrequired.data.providers.ingredient;

import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import someassemblyrequired.data.providers.Ingredients;
import someassemblyrequired.integration.ModCompat;

import java.util.List;

public class MinersDelightIngredients {

    public static final List<Holder<Item>> MODEL_OVERRIDES = List.of(
            Ingredients.reference(ModCompat.MINERSDELIGHT, "vegan_patty")
    );

    public static void addIngredients(Ingredients ingredients) {
        ingredients.builder(Ingredients.reference(ModCompat.MINERSDELIGHT, "vegan_patty")).customModel().setHeight(2);
    }
}
