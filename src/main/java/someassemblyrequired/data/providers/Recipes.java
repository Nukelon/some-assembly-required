package someassemblyrequired.data.providers;

import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.data.providers.recipe.SandwichSpoutingRecipeBuilder;
import someassemblyrequired.data.providers.recipe.farmersdelight.CuttingRecipes;
import someassemblyrequired.registry.ModBlocks;
import someassemblyrequired.registry.ModItems;

import java.util.concurrent.CompletableFuture;

public class Recipes extends RecipeProvider {

    public Recipes(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        addCraftingRecipes(output);
        addCookingRecipes(output);
        CuttingRecipes.addCuttingRecipes(output);
        SandwichSpoutingRecipeBuilder.addFillingRecipes(output);
    }

    private void addCraftingRecipes(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.SANDWICHING_STATION.get())
                .pattern("AA")
                .pattern("BB")
                .define('A', Items.SMOOTH_STONE)
                .define('B', ItemTags.PLANKS)
                .unlockedBy("has_smooth_stone", createItemCriterion(Items.SMOOTH_STONE))
                .save(output, getRecipeLocation(ModBlocks.SANDWICHING_STATION.get(), "crafting_shaped"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.BURGER_BUN.get())
                .requires(Ingredient.of(Tags.Items.CROPS_WHEAT), 2)
                .requires(Ingredient.of(Tags.Items.SEEDS))
                .unlockedBy("has_wheat", createItemCriterion(Items.WHEAT))
                .save(output, getRecipeLocation(ModItems.BURGER_BUN.get(), "crafting_shapeless"));
    }

    private void addCookingRecipes(RecipeOutput output) {
        addBreadCookingRecipe(output, 200, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, "smelting");
        addBreadCookingRecipe(output, 100, RecipeSerializer.SMOKING_RECIPE, SmokingRecipe::new, "smoking");
        addBreadCookingRecipe(output, 600, RecipeSerializer.CAMPFIRE_COOKING_RECIPE, CampfireCookingRecipe::new, "campfire_cooking");
    }

    private <T extends AbstractCookingRecipe> void addBreadCookingRecipe(RecipeOutput output, int time, RecipeSerializer<T> serializer, AbstractCookingRecipe.Factory<T> factory, String type) {
        SimpleCookingRecipeBuilder
                .generic(Ingredient.of(ModItems.BREAD_SLICE.get()), RecipeCategory.FOOD, ModItems.TOASTED_BREAD_SLICE.get(), 0.35F, time, serializer, factory)
                .unlockedBy("has_bread", createItemCriterion(ModItems.BREAD_SLICE.get()))
                .save(output, getRecipeLocation(ModItems.TOASTED_BREAD_SLICE.get(), type));
    }

    private ResourceLocation getRecipeLocation(ItemLike result, String location) {
        return SomeAssemblyRequired.id(location + "/" + BuiltInRegistries.ITEM.getKey(result.asItem()).getPath());
    }

    private Criterion<InventoryChangeTrigger.TriggerInstance> createItemCriterion(ItemLike itemProvider) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(itemProvider);
    }
}
