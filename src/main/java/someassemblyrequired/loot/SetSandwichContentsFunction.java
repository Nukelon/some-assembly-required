package someassemblyrequired.loot;

import com.google.common.collect.Lists;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntries;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.item.sandwich.SandwichContents;
import someassemblyrequired.registry.ModDataComponents;
import someassemblyrequired.registry.ModLootFunctions;

import java.util.List;
import java.util.function.Consumer;

public class SetSandwichContentsFunction extends LootItemConditionalFunction {

    private final List<LootPoolEntryContainer> entries;

    public static final MapCodec<SetSandwichContentsFunction> CODEC = RecordCodecBuilder.mapCodec(instance ->
            commonFields(instance).and(LootPoolEntries.CODEC.listOf().fieldOf("entries").forGetter(SetSandwichContentsFunction::entries))
                    .apply(instance, SetSandwichContentsFunction::new));

    protected SetSandwichContentsFunction(List<LootItemCondition> conditions, List<LootPoolEntryContainer> entries) {
        super(conditions);
        this.entries = entries;
    }

    private List<LootPoolEntryContainer> entries() {
        return entries;
    }

    @Override
    protected ItemStack run(ItemStack item, LootContext context) {
        NonNullList<ItemStack> ingredients = NonNullList.create();

        entries.forEach(
                entry -> entry.expand(context,
                        (lootPoolEntry) -> lootPoolEntry.createItemStack(splitStacks(ingredients::add), context)
                )
        );

        for (ItemStack ingredient : ingredients) {
            if (ingredient.isEmpty() || ingredient.getCount() != 1) {
                SomeAssemblyRequired.LOGGER.warn("Tried to generate sandwich with invalid ingredient '{}', " +
                        "ingredients must have a stack size of 1", ingredient.toString());
                return ItemStack.EMPTY;
            }
        }

        SandwichContents contents = new SandwichContents(ingredients);
        item.set(ModDataComponents.SANDWICH_CONTENTS, contents);

        return item;
    }

    private Consumer<ItemStack> splitStacks(Consumer<ItemStack> consumer) {
        return (stack) -> {
            if (stack.getCount() == 1) {
                consumer.accept(stack);
            } else if (stack.getCount() != 0) {
                int count = stack.getCount();
                stack.setCount(1);
                for (int i = 0; i < count; i++) {
                    consumer.accept(stack.copy());
                }
            }
        };
    }

    @Override
    public LootItemFunctionType<SetSandwichContentsFunction> getType() {
        return ModLootFunctions.SET_SANDWICH_CONTENTS.get();
    }

    public static SetSandwichContentsFunction.Builder setIngredients() {
        return new SetSandwichContentsFunction.Builder();
    }

    public static class Builder extends LootItemConditionalFunction.Builder<SetSandwichContentsFunction.Builder> {

        private final List<LootPoolEntryContainer> entries = Lists.newArrayList();

        protected SetSandwichContentsFunction.Builder getThis() {
            return this;
        }

        public SetSandwichContentsFunction.Builder withEntry(LootPoolEntryContainer.Builder<?> entry) {
            entries.add(entry.build());
            return this;
        }

        public LootItemFunction build() {
            return new SetSandwichContentsFunction(getConditions(), entries);
        }
    }
}
