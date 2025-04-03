package someassemblyrequired.registry;

import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.item.sandwich.SandwichContents;

public class ModDataComponents {

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, SomeAssemblyRequired.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SandwichContents>> SANDWICH_CONTENTS = DATA_COMPONENT_TYPES.register("sandwich_contents", () -> new DataComponentType.Builder<SandwichContents>()
            .persistent(SandwichContents.CODEC)
            .networkSynchronized(SandwichContents.STREAM_CODEC)
            .cacheEncoding()
            .build()
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> SPREAD_COLOR = DATA_COMPONENT_TYPES.register("spread_color", () -> new DataComponentType.Builder<Integer>()
            .persistent(Codec.INT)
            .networkSynchronized(ByteBufCodecs.VAR_INT)
            .build()
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> JEI_EXAMPLE = DATA_COMPONENT_TYPES.register("jei_example", () -> new DataComponentType.Builder<Unit>()
            .persistent(Codec.unit(Unit.INSTANCE))
            .networkSynchronized(StreamCodec.unit(Unit.INSTANCE))
            .build()
    );
}
