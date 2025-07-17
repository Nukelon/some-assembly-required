package someassemblyrequired.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.integration.ModCompat;
import someassemblyrequired.integration.jei.create.SequencedAssemblyRecipeGenerator;
import someassemblyrequired.item.sandwich.SandwichItemHandler;
import someassemblyrequired.registry.ModBlocks;
import someassemblyrequired.registry.ModItems;

import mezz.jei.api.runtime.IJeiRuntime;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@JeiPlugin
public class JEIPlugin implements IModPlugin {

    private static final ResourceLocation ID = SomeAssemblyRequired.id("main");
    private static boolean hasSequencedAssemblyCategory = false;

    public static final RecipeType<SandwichingStationCategory.Recipe> SANDWICHING_STATION = RecipeType.create(SomeAssemblyRequired.MOD_ID, "sandwiching_station", SandwichingStationCategory.Recipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerIngredientAliases(IIngredientAliasRegistration registration) {
        List<ItemStack> sandwiches = new ArrayList<>();
        ModCompat.gatherCreativeTabSandwiches(sandwiches::add);
        ModCompat.gatherJEISandwiches(sandwiches::add);
        sandwiches.removeIf(sandwich -> !SandwichItemHandler.get(sandwich).orElseThrow().isBurger());
        registration.addAliases(VanillaTypes.ITEM_STACK, sandwiches, ModItems.SANDWICH.get().getDescriptionId());
    }

    @Override
    public void registerAdvanced(IAdvancedRegistration registration) {
        if (ModCompat.isCreateLoaded()) {
            // Only register sequenced assembly integration when the recipe type is available
            registration.getJeiHelpers()
                    .getRecipeType(ResourceLocation.parse(ModCompat.CREATE + ":sequenced_assembly"))
                    .ifPresent(type -> SequencedAssemblyRecipeGenerator.register(registration));
        }
        registration.addTypedRecipeManagerPlugin(SANDWICHING_STATION, new SandwichingStationRecipeGenerator());
    }

    @Override
    public void registerExtraIngredients(IExtraIngredientRegistration registration) {
        List<ItemStack> sandwiches = new ArrayList<>();
        ModCompat.gatherJEISandwiches(sandwiches::add);
        sandwiches.sort(Comparator.comparingInt(stack ->
                -BuiltInRegistries.ITEM.getId(SandwichItemHandler.get(stack).orElseThrow().bottom().getItem())
        ));
        registration.addExtraItemStacks(sandwiches);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new SandwichingStationCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(ModItems.SANDWICH.get(), (stack, uidContext) ->
                SandwichItemHandler.get(stack).map(handler -> {
                    StringBuilder builder = new StringBuilder();
                    for (ItemStack item : handler.getItems()) {
                        builder.append(item.toString());
                        builder.append(item.getOrCreateTag());
                    }
                    return builder.toString();
                }).orElse("")
        );
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.SANDWICHING_STATION.get()), SANDWICHING_STATION);
    }
    @Override
    public void onRuntimeAvailable(IJeiRuntime runtime) {
        if (ModCompat.isCreateLoaded()) {
            hasSequencedAssemblyCategory = runtime.getRecipeManager()
                    .createRecipeCategoryLookup()
                    .limitTypes(List.of(SequencedAssemblyRecipeGenerator.SEQUENCED_ASSEMBLY))
                    .get()
                    .findAny()
                    .isPresent();
        }
    }

    public static boolean hasSequencedAssemblyCategory() {
        return hasSequencedAssemblyCategory;
    }
}
