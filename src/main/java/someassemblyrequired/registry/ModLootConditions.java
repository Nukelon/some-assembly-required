package someassemblyrequired.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.loot.SandwichLootEnabledCondition;

public class ModLootConditions {

    public static final DeferredRegister<LootItemConditionType> LOOT_CONDITION_TYPES = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, SomeAssemblyRequired.MOD_ID);

    public static final DeferredHolder<LootItemConditionType, LootItemConditionType> SANDWICH_LOOT_ENABLED = LOOT_CONDITION_TYPES.register("sandwich_loot_enabled", () -> new LootItemConditionType(SandwichLootEnabledCondition.CODEC));

}
