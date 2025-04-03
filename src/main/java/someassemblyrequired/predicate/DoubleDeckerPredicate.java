package someassemblyrequired.predicate;

import com.mojang.serialization.Codec;
import net.minecraft.advancements.critereon.SingleComponentItemPredicate;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import someassemblyrequired.item.sandwich.SandwichContents;
import someassemblyrequired.registry.ModDataComponents;

public class DoubleDeckerPredicate implements SingleComponentItemPredicate<SandwichContents> {

    public static final DoubleDeckerPredicate INSTANCE = new DoubleDeckerPredicate();
    public static final Codec<DoubleDeckerPredicate> CODEC = Codec.unit(INSTANCE);

    @Override
    public DataComponentType<SandwichContents> componentType() {
        return ModDataComponents.SANDWICH_CONTENTS.get();
    }

    @Override
    public boolean matches(ItemStack stack, SandwichContents value) {
        return value.isDoubleDecker();
    }
}
