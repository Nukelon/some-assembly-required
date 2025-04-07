package someassemblyrequired.data.providers.recipe.create;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.integration.ModCompat;
import someassemblyrequired.integration.farmersdelight.FarmersDelightCompat;
import someassemblyrequired.item.sandwich.SandwichContents;
import someassemblyrequired.registry.ModDataComponents;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.concurrent.CompletableFuture;

public class PressingRecipeGenerator extends ProcessingRecipeGenerator {

    public PressingRecipeGenerator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, registries);

        ResourceLocation id = SomeAssemblyRequired.id(BuiltInRegistries.ITEM.getKey(ModItems.HAMBURGER.get()).getPath());
        create(id, builder -> {
                    builder.output(ModItems.HAMBURGER.get());
                    return builder.whenModLoaded(ModCompat.FARMERSDELIGHT)
                            .require(DataComponentIngredient.of(false,
                                    ModDataComponents.SANDWICH_CONTENTS,
                                    SandwichContents.get(FarmersDelightCompat.createBurger()),
                                    FarmersDelightCompat.createBurger().getItem()
                            ));
                }
        );
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.PRESSING;
    }
}
