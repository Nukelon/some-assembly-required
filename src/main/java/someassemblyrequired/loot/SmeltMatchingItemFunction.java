package someassemblyrequired.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.registry.ModLootFunctions;

import java.util.List;
import java.util.Optional;

public class SmeltMatchingItemFunction extends LootItemConditionalFunction {

    public static final MapCodec<SmeltMatchingItemFunction> CODEC = RecordCodecBuilder.mapCodec(instance ->
            commonFields(instance)
                    .and(BuiltInRegistries.ITEM.holderByNameCodec().fieldOf("item").forGetter(SmeltMatchingItemFunction::item))
                    .apply(instance, SmeltMatchingItemFunction::new));

    private final Holder<Item> item;

    public SmeltMatchingItemFunction(List<LootItemCondition> conditions, Holder<Item> item) {
        super(conditions);
        this.item = item;
    }

    private Holder<Item> item() {
        return item;
    }

    public LootItemFunctionType<SmeltMatchingItemFunction> getType() {
        return ModLootFunctions.SMELT_MATCHING_ITEM.get();
    }

    public ItemStack run(ItemStack stack, LootContext context) {
        if (!stack.isEmpty() && stack.getItem() == item.value()) {
            Optional<RecipeHolder<SmeltingRecipe>> optional = context.getLevel().getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(stack), context.getLevel());
            if (optional.isPresent()) {
                ItemStack smelted = optional.get().value().getResultItem(RegistryAccess.EMPTY);
                if (!smelted.isEmpty()) {
                    ItemStack result = smelted.copy();
                    result.setCount(stack.getCount() * smelted.getCount());
                    return result;
                }
            }

            SomeAssemblyRequired.LOGGER.warn("Couldn't smelt {} because there is no smelting recipe", stack);
        }
        return stack;
    }

    public static LootItemConditionalFunction.Builder<?> smeltMatching(Holder<Item> item) {
        return simpleBuilder(conditions -> new SmeltMatchingItemFunction(conditions, item));
    }
}
