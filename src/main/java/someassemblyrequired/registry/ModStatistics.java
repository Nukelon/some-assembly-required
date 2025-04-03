package someassemblyrequired.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import someassemblyrequired.SomeAssemblyRequired;

public class ModStatistics {

    public static final DeferredRegister<ResourceLocation> CUSTOM_STATS = DeferredRegister.create(Registries.CUSTOM_STAT, SomeAssemblyRequired.MOD_ID);

    public static final DeferredHolder<ResourceLocation, ResourceLocation> SANDWICHES_EATEN = CUSTOM_STATS.register("sandwiches_eaten", () -> SomeAssemblyRequired.id("sandwiches_eaten"));
    public static final DeferredHolder<ResourceLocation, ResourceLocation> BURGERS_EATEN = CUSTOM_STATS.register("burgers_eaten", () -> SomeAssemblyRequired.id("burgers_eaten"));
}
