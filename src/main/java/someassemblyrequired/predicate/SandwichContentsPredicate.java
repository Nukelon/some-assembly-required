package someassemblyrequired.predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.CollectionPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.SingleComponentItemPredicate;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import someassemblyrequired.item.sandwich.SandwichContents;
import someassemblyrequired.registry.ModDataComponents;

import java.util.Optional;

public record SandwichContentsPredicate(Optional<CollectionPredicate<ItemStack, ItemPredicate>> items) implements SingleComponentItemPredicate<SandwichContents> {

    public static final Codec<SandwichContentsPredicate> CODEC = RecordCodecBuilder.create(instance -> instance
                    .group(CollectionPredicate.codec(ItemPredicate.CODEC).optionalFieldOf("items").forGetter(SandwichContentsPredicate::items))
                    .apply(instance, SandwichContentsPredicate::new)
    );

    @Override
    public DataComponentType<SandwichContents> componentType() {
        return ModDataComponents.SANDWICH_CONTENTS.get();
    }

    public boolean matches(ItemStack stack, SandwichContents value) {
        return items.isEmpty() || items.get().test(value.items());
    }
}
