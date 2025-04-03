package someassemblyrequired.data.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import someassemblyrequired.registry.ModItems;

import java.util.concurrent.CompletableFuture;

public class DataMaps extends net.neoforged.neoforge.common.data.DataMapProvider {

    public DataMaps(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        builder(NeoForgeDataMaps.COMPOSTABLES)
                .add(ModItems.BREAD_SLICE, new Compostable(0.3F), false)
                .add(ModItems.TOASTED_BREAD_SLICE, new Compostable(0.3F), false)
                .add(ModItems.BURGER_BUN, new Compostable(0.5F), false)
                .add(ModItems.BURGER_BUN_BOTTOM, new Compostable(0.3F), false)
                .add(ModItems.BURGER_BUN_TOP, new Compostable(0.3F), false)
                .add(ModItems.APPLE_SLICES, new Compostable(0.3F), false)
                .add(ModItems.CHOPPED_CARROT, new Compostable(0.3F), false)
                .add(ModItems.CHOPPED_BEETROOT, new Compostable(0.3F), false)
                .add(ModItems.TOMATO_SLICES, new Compostable(0.3F), false)
                .add(ModItems.SLICED_ONION, new Compostable(0.3F), false);
    }
}
