package someassemblyrequired.registry;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import someassemblyrequired.SomeAssemblyRequired;

public class ModAdvancementTriggers {

    public static final DeferredRegister<CriterionTrigger<?>> CRITERIA_TRIGGERS = DeferredRegister.create(Registries.TRIGGER_TYPE, SomeAssemblyRequired.MOD_ID);

    public static final DeferredHolder<CriterionTrigger<?>, PlayerTrigger> CONSUME_1000_SANDWICHES = CRITERIA_TRIGGERS.register("consume_1000_sandwiches", PlayerTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, PlayerTrigger> CONSUME_1000_BURGERS = CRITERIA_TRIGGERS.register("consume_1000_burgers", PlayerTrigger::new);
}
