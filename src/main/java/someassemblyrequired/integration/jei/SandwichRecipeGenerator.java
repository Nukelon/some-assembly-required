package someassemblyrequired.integration.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.advanced.ISimpleRecipeManagerPlugin;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import someassemblyrequired.item.sandwich.SandwichContents;
import someassemblyrequired.registry.ModIngredients;
import someassemblyrequired.registry.ModItems;
import someassemblyrequired.registry.ModTags;

import java.util.ArrayList;
import java.util.List;

public abstract class SandwichRecipeGenerator<RECIPE> implements ISimpleRecipeManagerPlugin<RECIPE> {

    protected abstract int getMaxInputs();

    @Override
    public boolean isHandledInput(ITypedIngredient<?> input) {
        if (input.getType() == VanillaTypes.ITEM_STACK) {
            ItemStack item = input.getItemStack().orElse(ItemStack.EMPTY);
            return !item.is(ModItems.SANDWICH)
                    && (item.getFoodProperties(null) != null || ModIngredients.hasIngredientFor(item.getItem()))
                    && !item.is(ModTags.SANDWICH_BREAD);
        }
        return false;
    }

    @Override
    public boolean isHandledOutput(ITypedIngredient<?> output) {
        if (output.getType() == VanillaTypes.ITEM_STACK) {
            ItemStack stack = output.getItemStack().orElse(ItemStack.EMPTY);
            return stack.is(ModItems.SANDWICH)
                    && SandwichContents.get(stack).items().size() >= 2
                    && SandwichContents.get(stack).items().size() <= getMaxInputs();
        }
        return false;
    }

    @Override
    public List<RECIPE> getRecipesForInput(ITypedIngredient<?> input) {
        List<RECIPE> recipes = new ArrayList<>();
        ItemStack stack = input.getItemStack().orElse(ItemStack.EMPTY);
        BuiltInRegistries.ITEM.getTag(ModTags.SANDWICH_BREAD).ifPresent(tag -> {
            for (Holder<Item> holder : tag) {
                if (!holder.is(ModTags.BURGER_BUNS)) {
                    ItemStack bread = new ItemStack(holder.value());
                    recipes.add(createRecipe(bread, stack, bread));
                }
            }
            ItemStack bottom = new ItemStack(ModItems.BURGER_BUN_BOTTOM);
            ItemStack top = new ItemStack(ModItems.BURGER_BUN_TOP);
            recipes.add(createRecipe(bottom, stack, top));
        });
        return recipes;
    }

    protected RECIPE createRecipe(ItemStack bottomBread, ItemStack input, ItemStack topBread) {
        if (input.getCount() != 1) {
            input = input.copy();
            input.setCount(1);
        }
        return getRecipeForSandwich(new SandwichContents(List.of(bottomBread, input, topBread)));
    }

    @Override
    public List<RECIPE> getRecipesForOutput(ITypedIngredient<?> output) {
        ItemStack stack = output.getItemStack().orElse(ItemStack.EMPTY);
        return List.of(getRecipeForSandwich(SandwichContents.get(stack)));
    }

    protected abstract RECIPE getRecipeForSandwich(SandwichContents contents);

    @Override
    public List<RECIPE> getAllRecipes() {
        return List.of();
    }
}
