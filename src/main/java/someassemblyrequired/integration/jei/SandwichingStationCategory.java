package someassemblyrequired.integration.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.registry.ModBlocks;

import java.util.List;

public class SandwichingStationCategory implements IRecipeCategory<SandwichingStationCategory.Recipe> {

    private final IDrawable icon;
    private final IDrawable slot;
    private final IDrawable arrow;

    public SandwichingStationCategory(IGuiHelper helper) {
        ResourceLocation texture = SomeAssemblyRequired.id("textures/jei/sandwiching_station.png");
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.SANDWICHING_STATION.get()));
        slot = helper.createDrawable(texture, 0, 0, 18, 18);
        arrow = helper.createDrawable(texture, 18, 0, 44, 18);
    }

    @Override
    public RecipeType<Recipe> getRecipeType() {
        return JEIPlugin.SANDWICHING_STATION;
    }

    @Override
    public Component getTitle() {
        return ModBlocks.SANDWICHING_STATION.get().getName();
    }

    @Override
    public int getHeight() {
        return 120;
    }

    @Override
    public int getWidth() {
        return 100;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, Recipe recipe, IFocusGroup focuses) {
        int slotSize = 18;
        int slots = recipe.toppings.size() + 1;
        int totalSlotSize = slotSize + (slots - 1) * (1 + slotSize);
        int inputY = getHeight() / 2 - totalSlotSize / 2;

        int spacingX = 6;
        int outputX = getWidth() - spacingX - slotSize;
        int outputY = getHeight() / 2 - slotSize / 2;

        builder.addSlot(RecipeIngredientRole.OUTPUT, outputX, outputY)
                .setBackground(slot, -1, -1)
                .addItemStack(recipe.result);

        for (int slot = 0; slot < slots - 1; slot++) {
            builder.addSlot(RecipeIngredientRole.INPUT, spacingX, inputY)
                    .setBackground(this.slot, -1, -1)
                    .addItemStack(recipe.toppings.get(recipe.toppings.size() - slot - 1));
            inputY += slotSize + 1;
        }

        builder.addSlot(RecipeIngredientRole.INPUT, spacingX, inputY)
                .setBackground(slot, -1, -1)
                .addItemStack(recipe.prefix);
    }

    @Override
    public void draw(Recipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        arrow.draw(guiGraphics, getWidth() / 2 - arrow.getWidth() / 2, getHeight() / 2 - arrow.getHeight() / 2);
    }

    public record Recipe(ItemStack prefix, List<ItemStack> toppings, ItemStack result) { }
}
