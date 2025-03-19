package someassemblyrequired.data.providers.ingredient;

import com.sammy.minersdelight.setup.MDItems;
import net.minecraft.world.item.Item;
import someassemblyrequired.data.providers.Ingredients;

import java.util.List;

public class MinersDelightIngredients {

    public static final List<Item> MODEL_OVERRIDES = List.of(
            MDItems.VEGAN_PATTY.get()
    );

    public static void addIngredients(Ingredients ingredients) {
        ingredients.builder(MDItems.VEGAN_PATTY.get()).setRenderAsItem(false).setHeight(2);
    }
}
