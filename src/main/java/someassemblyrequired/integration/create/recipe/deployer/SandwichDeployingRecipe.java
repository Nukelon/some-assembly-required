package someassemblyrequired.integration.create.recipe.deployer;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.config.ModConfig;
import someassemblyrequired.ingredient.Ingredients;
import someassemblyrequired.item.sandwich.SandwichContents;
import someassemblyrequired.item.sandwich.SandwichItem;
import someassemblyrequired.registry.ModItems;
import someassemblyrequired.registry.ModTags;

import java.util.Optional;

public class SandwichDeployingRecipe extends ProcessingRecipe<RecipeWrapper> {

    private static final IRecipeTypeInfo TYPE_INFO = new TypeInfo();
    private static final ResourceLocation RECIPE_ID = SomeAssemblyRequired.id("sandwich_deploying");

    public SandwichDeployingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(TYPE_INFO, params);
    }

    public static Optional<RecipeHolder<SandwichDeployingRecipe>> createRecipe(RecipeWrapper inventory) {
        if (!matches(inventory)) {
            return Optional.empty();
        }
        return Optional.of(createRecipe(inventory.getItem(0), inventory.getItem(1)));
    }

    public static boolean matches(RecipeWrapper inventory) {
        ItemStack item = inventory.getItem(0);

        if (item.isEmpty() || !item.is(ModTags.SANDWICH_BREAD) && !item.is(ModItems.SANDWICH.get())) {
            return false;
        }

        ItemStack ingredient = inventory.getItem(1);
        if (!Ingredients.canAddToSandwich(ingredient) || ingredient.is(ModItems.SANDWICH.get())) {
            return false;
        }

        return ModConfig.server.maximumSandwichHeight.get() >= Ingredients.getHeight(ingredient)
                + SandwichContents.maybeGet(item)
                        .map(SandwichContents::getTotalHeight)
                        .orElse(Ingredients.getHeight(item));
    }

    public static RecipeHolder<SandwichDeployingRecipe> createRecipe(ItemStack sandwich, ItemStack ingredient) {
        sandwich = sandwich.copy();
        sandwich.setCount(1);
        ingredient = ingredient.copy();
        ingredient.setCount(1);

        ItemStack container = Ingredients.getFood(ingredient, null).usingConvertsTo().orElse(ItemStack.EMPTY);
        ItemStack result = SandwichItem.of(sandwich, ingredient);

        SandwichDeployingRecipe recipe = new ProcessingRecipeBuilder<>(SandwichDeployingRecipe::new, RECIPE_ID)
                .withItemOutputs(
                        new ProcessingOutput(result, 1),
                        container.isEmpty() ? ProcessingOutput.EMPTY : new ProcessingOutput(container, 1)
                ).build();

        return new RecipeHolder<>(RECIPE_ID, recipe);
    }

    @Override
    public boolean matches(RecipeWrapper inventory, Level level) {
        return false;
    }

    @Override
    protected int getMaxInputCount() {
        return 2;
    }

    @Override
    protected int getMaxOutputCount() {
        return 2;
    }

    private static class TypeInfo implements IRecipeTypeInfo {

        @Override
        public ResourceLocation getId() {
            return null;
        }

        @Override
        public <T extends RecipeSerializer<?>> T getSerializer() {
            return null;
        }

        @Override
        public <I extends RecipeInput, R extends Recipe<I>> RecipeType<R> getType() {
            return AllRecipeTypes.DEPLOYING.getType();
        }
    }
}
