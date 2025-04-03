package someassemblyrequired.integration.jei;

import com.simibubi.create.compat.jei.CreateJEI;
import com.simibubi.create.content.fluids.potion.PotionFluidHandler;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeBuilder;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.integration.create.recipe.SandwichFluidSpoutingRecipe;
import someassemblyrequired.item.sandwich.SandwichContents;
import someassemblyrequired.item.sandwich.SandwichItem;
import someassemblyrequired.registry.ModItems;
import someassemblyrequired.registry.ModRecipeTypes;

import java.util.List;
import java.util.Optional;

// TODO support double deckers
public class SequencedAssemblyRecipeGenerator extends SandwichRecipeGenerator<SequencedAssemblyRecipe> {

    @Override
    protected int getMaxInputs() {
        return 32;
    }

    @Override
    protected SequencedAssemblyRecipe getRecipeForSandwich(SandwichContents contents) {
        List<ItemStack> items = contents.items();
        ItemStack startItem = items.getFirst();
        int size = items.size();
        if (size - 1 > 6) {
            startItem = SandwichItem.of(items.subList(0, size - 6));
            items = items.subList(size - 6, size);
        } else {
            items = items.subList(1, size);
        }

        SequencedAssemblyRecipeBuilder recipe = new SequencedAssemblyRecipeBuilder(SomeAssemblyRequired.id("dynamic/sequenced_assembly"))
                .transitionTo(ModItems.SANDWICH.get())
                .loops(1)
                .addOutput(contents.makeItem(), 1)
                .require(Ingredient.of(startItem));

        for (ItemStack input : items) {
            Optional<SandwichFluidSpoutingRecipe> spoutingRecipe = getSpoutingRecipeFor(input);

            if (input.is(Items.POTION)) {
                Holder<Potion> potion = input.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).potion().orElse(Potions.WATER);
                int requiredAmount = PotionFluidHandler.getRequiredAmountForFilledBottle(null, null);
                FluidIngredient potionIngredient = PotionFluidHandler.potionIngredient(potion, requiredAmount);
                recipe.addStep(FillingRecipe::new, builder -> builder.require(potionIngredient));
            } else if (spoutingRecipe.isPresent()) {
                recipe.addStep(FillingRecipe::new, builder -> builder.require(spoutingRecipe.get().ingredient()));
            } else {
                recipe.addStep(DeployerApplicationRecipe::new, builder -> builder.require(Ingredient.of(input)));
            }
        }

        return recipe.build().value();
    }

    private Optional<SandwichFluidSpoutingRecipe> getSpoutingRecipeFor(ItemStack sandwichIngredient) {
        return CreateJEI.getTypedRecipesExcluding(ModRecipeTypes.SANDWICH_SPOUTING.get(), recipe -> recipe.value().getSerializer() != ModRecipeTypes.SANDWICH_FLUID_SPOUTING_SERIALIZER.get())
                .stream()
                .map(RecipeHolder::value)
                .map(recipe -> (SandwichFluidSpoutingRecipe) recipe)
                .filter(recipe -> ItemStack.isSameItemSameComponents(recipe.getResultItem(RegistryAccess.EMPTY), sandwichIngredient))
                .findFirst();
    }
}
