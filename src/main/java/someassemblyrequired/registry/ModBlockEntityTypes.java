package someassemblyrequired.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.block.SandwichBlockEntity;

@SuppressWarnings("ConstantConditions")
public class ModBlockEntityTypes {

    public static final DeferredRegister<BlockEntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, SomeAssemblyRequired.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SandwichBlockEntity>> SANDWICH = ENTITY_TYPES.register(
            "sandwich",
            () -> BlockEntityType.Builder.of(SandwichBlockEntity::new,
                    ModBlocks.SANDWICH.get()
            ).build(null)
    );
}
