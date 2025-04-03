package someassemblyrequired.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.loot.SetSandwichContentsFunction;
import someassemblyrequired.loot.SmeltMatchingItemFunction;

public class ModLootFunctions {

    public static final DeferredRegister<LootItemFunctionType<?>> LOOT_FUNCTION_TYPES = DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, SomeAssemblyRequired.MOD_ID);

    public static final DeferredHolder<LootItemFunctionType<?>, LootItemFunctionType<SetSandwichContentsFunction>> SET_SANDWICH_CONTENTS = LOOT_FUNCTION_TYPES.register("set_sandwich_contents", () -> new LootItemFunctionType<>(SetSandwichContentsFunction.CODEC));
    public static final DeferredHolder<LootItemFunctionType<?>, LootItemFunctionType<SmeltMatchingItemFunction>> SMELT_MATCHING_ITEM = LOOT_FUNCTION_TYPES.register("smelt_matching_item", () -> new LootItemFunctionType<>(SmeltMatchingItemFunction.CODEC));

}
