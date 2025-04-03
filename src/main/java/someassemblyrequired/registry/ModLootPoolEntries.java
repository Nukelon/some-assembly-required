package someassemblyrequired.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.loot.OptionalLootItem;

public class ModLootPoolEntries {

    public static final DeferredRegister<LootPoolEntryType> LOOT_POOL_ENTRY_TYPES = DeferredRegister.create(Registries.LOOT_POOL_ENTRY_TYPE, SomeAssemblyRequired.MOD_ID);

    public static final DeferredHolder<LootPoolEntryType, LootPoolEntryType> OPTIONAL_ITEM = LOOT_POOL_ENTRY_TYPES.register("optional_item", () -> new LootPoolEntryType(OptionalLootItem.CODEC));
}
