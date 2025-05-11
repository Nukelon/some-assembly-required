package someassemblyrequired.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.config.ModConfig;
import someassemblyrequired.integration.ModCompat;
import someassemblyrequired.integration.jei.create.SequencedAssemblyRecipeGenerator;
import someassemblyrequired.item.sandwich.SandwichContents;
import someassemblyrequired.registry.ModBlocks;
import someassemblyrequired.registry.ModItems;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@JeiPlugin
public class JEIPlugin implements IModPlugin {

    private static final ResourceLocation ID = SomeAssemblyRequired.id("main");

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
        sandwiches.removeIf(sandwich -> !SandwichContents.get(sandwich).isBurger());
        registration.addAliases(VanillaTypes.ITEM_STACK, sandwiches, ModItems.SANDWICH.get().getDescriptionId());
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addIngredientInfo(ModItems.SANDWICHING_STATION.get(),
                SomeAssemblyRequired.translate("sandwiching_station.info.creating_sandwiches.1"),
                SomeAssemblyRequired.translate("sandwiching_station.info.creating_sandwiches.2"),
                SomeAssemblyRequired.translate("sandwiching_station.info.creating_sandwiches.3"),
                SomeAssemblyRequired.translate("sandwiching_station.info.creating_sandwiches.4")
        );
        registration.addIngredientInfo(ModItems.SANDWICHING_STATION.get(),
                SomeAssemblyRequired.translate("sandwiching_station.info.bonus_effects.1"),
                SomeAssemblyRequired.translate("sandwiching_station.info.bonus_effects.2",
                        getMinIngredients(ModConfig.server.sandwichEffectDurations.get()),
                        Component.translatable(ModConfig.server.getSandwichBonusEffect(false).map(holder -> holder.value().getDescriptionId()).orElse("unknown")),
                        getMinIngredients(ModConfig.server.burgerEffectDurations.get()),
                        Component.translatable(ModConfig.server.getSandwichBonusEffect(true).map(holder -> holder.value().getDescriptionId()).orElse("unknown"))
                ),
                SomeAssemblyRequired.translate("sandwiching_station.info.bonus_effects.3")
        );
    }

    private int getMinIngredients(List<Integer> durations) {
        for (int i = 0; i < durations.size(); i++) {
            if (durations.get(i) > 0) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public void registerAdvanced(IAdvancedRegistration registration) {
        if (ModCompat.isCreateLoaded()) {
            SequencedAssemblyRecipeGenerator.register(registration);
        }
        registration.addTypedRecipeManagerPlugin(SANDWICHING_STATION, new SandwichingStationRecipeGenerator());
    }

    @Override
    public void registerExtraIngredients(IExtraIngredientRegistration registration) {
        List<ItemStack> sandwiches = new ArrayList<>();
        ModCompat.gatherJEISandwiches(sandwiches::add);
        sandwiches.sort(Comparator.comparingInt(stack ->
                -BuiltInRegistries.ITEM.getId(SandwichContents.get(stack).getFirst().getItem())
        ));
        registration.addExtraItemStacks(sandwiches);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new SandwichingStationCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(ModItems.SANDWICH.get(), new ISubtypeInterpreter<>() {
            @Override
            public Object getSubtypeData(ItemStack ingredient, UidContext context) {
                return SandwichContents.get(ingredient);
            }

            @Override
            public String getLegacyStringSubtypeInfo(ItemStack ingredient, UidContext context) {
                return getSubtypeData(ingredient, context).toString();
            }
        });
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.SANDWICHING_STATION.get()), SANDWICHING_STATION);
    }
}
