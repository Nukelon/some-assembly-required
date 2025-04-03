package someassemblyrequired.recipe;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import someassemblyrequired.registry.ModRecipeTypes;

public abstract class SandwichSpoutingRecipe implements Recipe<RecipeInput> {

    public abstract int getAmountRequired(FluidStack fluid);

    public abstract boolean matches(FluidStack fluid);

    public abstract ItemStack assemble(FluidStack fluid);

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.SANDWICH_SPOUTING.get();
    }

    @Override
    public final boolean isSpecial() {
        return true;
    }

    @Override
    public final boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return false;
    }

    @Override
    public boolean matches(RecipeInput input, Level level) {
        return false;
    }

    @Override
    public ItemStack assemble(RecipeInput input, HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    public static class EmptySerializer implements RecipeSerializer<SandwichSpoutingRecipe> {

        @Override
        public MapCodec<SandwichSpoutingRecipe> codec() {
            return MapCodec.unit(() -> {
                throw new UnsupportedOperationException();
            });
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SandwichSpoutingRecipe> streamCodec() {
            // noinspection ConstantConditions
            return StreamCodec.unit(null);
        }
    }
}
