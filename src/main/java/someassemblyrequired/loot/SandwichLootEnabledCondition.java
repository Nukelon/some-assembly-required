package someassemblyrequired.loot;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import someassemblyrequired.config.ModConfig;
import someassemblyrequired.registry.ModLootConditions;

public class SandwichLootEnabledCondition implements LootItemCondition {

    private static final SandwichLootEnabledCondition INSTANCE = new SandwichLootEnabledCondition();

    public static final MapCodec<SandwichLootEnabledCondition> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public LootItemConditionType getType() {
        return ModLootConditions.SANDWICH_LOOT_ENABLED.get();
    }

    @Override
    public boolean test(LootContext lootContext) {
        return ModConfig.server.generateChestLoot.get();
    }

    public static SandwichLootEnabledCondition sandwichLootEnabled() {
        return INSTANCE;
    }
}
