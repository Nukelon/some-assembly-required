package someassemblyrequired.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import someassemblyrequired.SomeAssemblyRequired;

public class ModSoundEvents {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, SomeAssemblyRequired.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> ADD_ITEM = register("block.sandwich.add_item.generic");
    public static final DeferredHolder<SoundEvent, SoundEvent> ADD_ITEM_WET = register("block.sandwich.add_item.wet");
    public static final DeferredHolder<SoundEvent, SoundEvent> ADD_ITEM_MOIST = register("block.sandwich.add_item.moist");
    public static final DeferredHolder<SoundEvent, SoundEvent> ADD_ITEM_SLIMY = register("block.sandwich.add_item.slimy");
    public static final DeferredHolder<SoundEvent, SoundEvent> ADD_ITEM_LEAFY = register("block.sandwich.add_item.leafy");

    private static DeferredHolder<SoundEvent, SoundEvent> register(String id) {
        return SOUND_EVENTS.register(id, () -> SoundEvent.createVariableRangeEvent(SomeAssemblyRequired.id(id)));
    }
}
