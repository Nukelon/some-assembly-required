package someassemblyrequired.integration.jei;

import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import someassemblyrequired.item.sandwich.SandwichContents;

public class SandwichSubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {

    @Override
    public @Nullable Object getSubtypeData(ItemStack stack, UidContext uidContext) {
        return SandwichContents.get(stack);
    }

    @Override
    public String getLegacyStringSubtypeInfo(ItemStack stack, UidContext uidContext) {
        return SandwichContents.get(stack).toString();
    }
}
