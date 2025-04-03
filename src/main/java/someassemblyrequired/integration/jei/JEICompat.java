package someassemblyrequired.integration.jei;

import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.integration.ModCompat;
import someassemblyrequired.registry.ModBlocks;
import someassemblyrequired.registry.ModItems;

import java.util.ArrayList;
import java.util.List;

// TODO add sandwich alias to burgers
@JeiPlugin
@SuppressWarnings("unused")
public class JEICompat implements IModPlugin {

    private static final ResourceLocation ID = SomeAssemblyRequired.id("main");

    public static final RecipeType<SandwichingStationCategory.Recipe> SANDWICHING_STATION = RecipeType.create(SomeAssemblyRequired.MOD_ID, "sandwiching_station", SandwichingStationCategory.Recipe.class);
    private static final RecipeType<SequencedAssemblyRecipe> SEQUENCED_ASSEMBLY = RecipeType.create(ModCompat.CREATE, "sequenced_assembly", SequencedAssemblyRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        // if (ModCompat.isCreateLoaded()) {
        //     CreateJEICompat.registerRecipes(registration);
        // }
        // registration.addRecipes(SANDWICHING_STATION, List.of(new SandwichingStationCategory.Recipe()));
        // registration.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, List.of());
    }

    @Override
    public void registerAdvanced(IAdvancedRegistration registration) {
        // TODO ModCompat.isCreateLoaded()
        registration.addTypedRecipeManagerPlugin(SEQUENCED_ASSEMBLY, new SequencedAssemblyRecipeGenerator());
    }

    @Override
    public void registerExtraIngredients(IExtraIngredientRegistration registration) {
        List<ItemStack> sandwiches = new ArrayList<>();
        ModCompat.gatherJEISandwiches(sandwiches::add);
        registration.addExtraItemStacks(sandwiches);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new SandwichingStationCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(ModItems.SANDWICH.get(), new SandwichSubtypeInterpreter());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.SANDWICHING_STATION.get()), SANDWICHING_STATION);
    }
}
