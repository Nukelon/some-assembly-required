package someassemblyrequired.registry;

import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.loot.RollLootTableModifier;

public class ModLootModifiers {

    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, SomeAssemblyRequired.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<RollLootTableModifier>> ROLL_LOOT_TABLE = LOOT_MODIFIERS.register("roll_loot_table", RollLootTableModifier.CODEC);
}
