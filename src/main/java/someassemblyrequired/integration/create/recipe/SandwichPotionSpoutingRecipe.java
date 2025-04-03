package someassemblyrequired.integration.create.recipe;

import com.mojang.serialization.MapCodec;
import com.simibubi.create.AllDataComponents;
import com.simibubi.create.AllFluids;
import com.simibubi.create.content.fluids.potion.PotionFluid;
import com.simibubi.create.content.fluids.potion.PotionFluidHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.fluids.FluidStack;
import someassemblyrequired.recipe.SandwichSpoutingRecipe;
import someassemblyrequired.registry.ModRecipeTypes;

public class SandwichPotionSpoutingRecipe extends SandwichSpoutingRecipe {

    private static final ItemStack BOTTLE = new ItemStack(Items.GLASS_BOTTLE);
    private static final SandwichPotionSpoutingRecipe INSTANCE = new SandwichPotionSpoutingRecipe();

    @Override
    public int getAmountRequired(FluidStack fluid) {
        return PotionFluidHandler.getRequiredAmountForFilledBottle(BOTTLE, fluid);
    }

    @Override
    public boolean matches(FluidStack fluid) {
        if (!fluid.getFluid().isSame(AllFluids.POTION.get()))
            return false;

        return fluid.get(AllDataComponents.POTION_FLUID_BOTTLE_TYPE) == PotionFluid.BottleType.REGULAR;
    }

    @Override
    public ItemStack assemble(FluidStack fluid) {
        return PotionFluidHandler.fillBottle(BOTTLE.copy(), fluid.copy());
    }



    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.SANDWICH_POTION_SPOUTING_SERIALIZER.get();
    }

    public static class Serializer implements RecipeSerializer<SandwichPotionSpoutingRecipe> {

        @Override
        public MapCodec<SandwichPotionSpoutingRecipe> codec() {
            return MapCodec.unit(INSTANCE);
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SandwichPotionSpoutingRecipe> streamCodec() {
            return StreamCodec.unit(INSTANCE);
        }
    }
}
