package someassemblyrequired.integration.create;

import com.simibubi.create.AllFluids;
import com.simibubi.create.compat.jei.CreateJEI;
import com.simibubi.create.content.fluids.potion.PotionFluidHandler;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.kinetics.deployer.DeployerRecipeSearchEvent;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.fluids.FluidStack;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.integration.ModCompat;
import someassemblyrequired.integration.create.recipe.SandwichFluidSpoutingRecipe;
import someassemblyrequired.integration.create.recipe.deployer.SandwichDeployingRecipe;
import someassemblyrequired.item.sandwich.SandwichContents;
import someassemblyrequired.item.sandwich.SandwichItem;
import someassemblyrequired.registry.ModDataComponents;
import someassemblyrequired.registry.ModItems;
import someassemblyrequired.registry.ModRecipeTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CreateCompat {

    public static void setup() {
        NeoForge.EVENT_BUS.addListener(CreateCompat::onDeployerRecipeSearch);
    }

    public static void onDeployerRecipeSearch(DeployerRecipeSearchEvent event) {
        event.addRecipe(() -> SandwichDeployingRecipe.createRecipe(event.getInventory()), 150);
    }

    public static List<SequencedAssemblyRecipe> createSandwichAssemblingRecipes() {
        if (true) {
            return List.of();
        }
        List<ItemStack> sandwiches = new ArrayList<>();

        ModCompat.gatherJEISandwiches(sandwiches::add);
        ModCompat.gatherCreativeTabSandwiches(sandwiches::add);

        List<SequencedAssemblyRecipe> recipes = new ArrayList<>();

        for (ItemStack sandwich : sandwiches) {
            recipes.add(createSandwichRecipe(sandwich, "sandwich_assembly"));
        }

        CreateJEI.getTypedRecipesExcluding(ModRecipeTypes.SANDWICH_SPOUTING.get(), recipe -> recipe.value().getSerializer() != ModRecipeTypes.SANDWICH_FLUID_SPOUTING_SERIALIZER.get())
                .stream()
                .map(recipe -> (SandwichFluidSpoutingRecipe) recipe.value())
                .map(recipe -> builder(SandwichItem.makeSandwich(recipe.assemble(FluidStack.EMPTY)), "sandwich_spouting")
                        .addStep(FillingRecipe::new, r -> r.require(recipe.ingredient()))
                        .addStep(DeployerApplicationRecipe::new, r -> r.require(ModItems.BREAD_SLICE.get()))
                        .build()
                ).forEach(e -> recipes.add(e.value()));

        return recipes;
    }

    private static SequencedAssemblyRecipe createSandwichRecipe(ItemStack sandwich, String name) {
        SequencedAssemblyRecipeBuilder builder = builder(sandwich, name);
        SandwichContents contents = sandwich.getOrDefault(ModDataComponents.SANDWICH_CONTENTS, SandwichContents.EMPTY);

        for (int j = 1; j < contents.items().size(); j++) {
            ItemStack ingredient = contents.items().get(j);
            if (ingredient.is(Items.POTION)) {
                Holder<Potion> potion = ingredient.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).potion().orElse(Potions.WATER);
                builder.addStep(FillingRecipe::new, recipe -> recipe.require(PotionFluidHandler.potionIngredient(potion, PotionFluidHandler.getRequiredAmountForFilledBottle(null, null))));
            } else if (ingredient.is(Items.HONEY_BOTTLE)) {
                builder.addStep(FillingRecipe::new, recipe -> recipe.require(AllFluids.HONEY.get(), 250));
            } else {
                builder.addStep(DeployerApplicationRecipe::new, recipe -> recipe.require(Ingredient.of(ingredient)));
            }
        }

        return builder.build().value();
    }

    private static SequencedAssemblyRecipeBuilder builder(ItemStack sandwich, String name) {
        return new SequencedAssemblyRecipeBuilder(SomeAssemblyRequired.id("sequenced_assembly/%s".formatted(name)))
                .require(Ingredient.of(sandwich.getOrDefault(ModDataComponents.SANDWICH_CONTENTS, SandwichContents.EMPTY).items().getFirst()))
                .transitionTo(ModItems.SANDWICH.get())
                .loops(1)
                .addOutput(sandwich, 1);
    }

    public static void populateCreativeTab(Consumer<ItemStack> items) {

    }

    public static void populateJEI(Consumer<ItemStack> items) {
        CreateJEI.getTypedRecipesExcluding(ModRecipeTypes.SANDWICH_SPOUTING.get(), recipe -> recipe.value().getSerializer() != ModRecipeTypes.SANDWICH_FLUID_SPOUTING_SERIALIZER.get())
                .stream()
                .map(recipe -> (SandwichFluidSpoutingRecipe) recipe.value())
                .map(recipe -> SandwichItem.makeSandwich(recipe.assemble(FluidStack.EMPTY)))
                .forEach(items);
    }
}
