package someassemblyrequired.registry;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.PlayerTrigger;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.advancement.ItemTrigger;

public class ModAdvancementTriggers {

    public static final ItemTrigger CONSUME_POTION_SANDWICH = new ItemTrigger(SomeAssemblyRequired.id("consume_potion_sandwich"));
    public static final ItemTrigger CONSUME_DOUBLE_DECKER_SANDWICH = new ItemTrigger(SomeAssemblyRequired.id("consume_double_decker_sandwich"));
    public static final PlayerTrigger CONSUME_1000_SANDWICHES = new PlayerTrigger(SomeAssemblyRequired.id("consume_1000_sandwiches"));
    public static final PlayerTrigger CONSUME_1000_BURGERS = new PlayerTrigger(SomeAssemblyRequired.id("consume_1000_burgers"));

    public static void register() {
        CriteriaTriggers.register(CONSUME_POTION_SANDWICH);
        CriteriaTriggers.register(CONSUME_DOUBLE_DECKER_SANDWICH);
        CriteriaTriggers.register(CONSUME_1000_SANDWICHES);
        CriteriaTriggers.register(CONSUME_1000_BURGERS);
    }
}
