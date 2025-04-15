package someassemblyrequired.integration.create;

import com.simibubi.create.AllFluids;
import com.simibubi.create.AllItems;
import com.simibubi.create.compat.jei.CreateJEI;
import com.simibubi.create.content.kinetics.deployer.DeployerRecipeSearchEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import someassemblyrequired.ingredient.Ingredients;
import someassemblyrequired.integration.create.ingredient.BuildersTeaBehavior;
import someassemblyrequired.integration.create.recipe.SandwichFluidSpoutingRecipe;
import someassemblyrequired.integration.create.recipe.deployer.SandwichDeployingRecipe;
import someassemblyrequired.item.sandwich.SandwichItem;
import someassemblyrequired.registry.ModRecipeTypes;

import java.util.List;
import java.util.function.Consumer;

public class CreateCompat {

    public static void setup() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(CreateCompat::onCommonSetup);

        MinecraftForge.EVENT_BUS.addListener(CreateCompat::onDeployerRecipeSearch);
    }

    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> Ingredients.addBehavior(AllItems.BUILDERS_TEA.get(), new BuildersTeaBehavior()));
    }

    public static void onDeployerRecipeSearch(DeployerRecipeSearchEvent event) {
        event.addRecipe(() -> SandwichDeployingRecipe.createRecipe(event.getInventory()), 150);
    }

    public static void populateJEI(Consumer<ItemStack> items) {
        CreateJEI.getTypedRecipesExcluding(ModRecipeTypes.SANDWICH_SPOUTING.get(), recipe -> recipe.getSerializer() != ModRecipeTypes.SANDWICH_FLUID_SPOUTING_SERIALIZER.get())
                .stream()
                .map(recipe -> (SandwichFluidSpoutingRecipe) recipe)
                .map(recipe -> {
                    ItemStack filling = recipe.assemble(FluidStack.EMPTY);
                    if (List.of(
                            Items.HONEY_BOTTLE,
                            AllFluids.CHOCOLATE.getBucket().orElseThrow()
                    ).contains(filling.getItem())) {
                        return SandwichItem.makeToastSandwich(filling);
                    }
                    return SandwichItem.makeSandwich(filling);
                })
                .forEach(items);
    }
}
