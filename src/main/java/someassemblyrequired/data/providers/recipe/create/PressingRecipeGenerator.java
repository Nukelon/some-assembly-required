package someassemblyrequired.data.providers.recipe.create;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.integration.ModCompat;
import someassemblyrequired.integration.farmersdelight.FarmersDelightCompat;
import someassemblyrequired.item.sandwich.SandwichContents;
import someassemblyrequired.item.sandwich.SandwichItem;
import someassemblyrequired.registry.ModDataComponents;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.concurrent.CompletableFuture;

public class PressingRecipeGenerator extends ProcessingRecipeGenerator {

    public PressingRecipeGenerator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, registries);

        stompSandwich(ModItems.HAMBURGER.get(), FarmersDelightCompat.createBurger());
        stompSandwich(ModItems.BACON_SANDWICH.get(), FarmersDelightCompat.createBLT());
        stompSandwich(ModItems.EGG_SANDWICH.get(), SandwichItem.makeSandwich(ModItems.FRIED_EGG.get(), ModItems.FRIED_EGG.get()));
        stompSandwich(ModItems.CHICKEN_SANDWICH.get(), SandwichItem.makeSandwich(someassemblyrequired.registry.ModItems.CHOPPED_CARROT.get(), ModItems.COOKED_CHICKEN_CUTS.get(), ModItems.CABBAGE_LEAF.get()));
    }

    private void stompSandwich(Item result, ItemStack sandwich) {
        ResourceLocation id = SomeAssemblyRequired.id(BuiltInRegistries.ITEM.getKey(result).getPath());
        create(id, builder -> {
                    builder.output(result);
                    return builder.whenModLoaded(ModCompat.FARMERSDELIGHT)
                            .require(DataComponentIngredient.of(false,
                                    ModDataComponents.SANDWICH_CONTENTS,
                                    SandwichContents.get(sandwich),
                                    sandwich.getItem()
                            ));
                }
        );
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.PRESSING;
    }
}
