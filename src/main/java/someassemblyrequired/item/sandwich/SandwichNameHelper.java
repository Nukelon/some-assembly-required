package someassemblyrequired.item.sandwich;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.ingredient.Ingredients;
import someassemblyrequired.registry.ModItems;
import someassemblyrequired.registry.ModTags;

import java.util.ArrayList;
import java.util.List;

public class SandwichNameHelper {

    public static Component getSandwichDisplayName(ItemStack stack) {
        SandwichContents sandwich = SandwichContents.get(stack);

        if (sandwich.items().isEmpty()
                || BuiltInRegistries.ITEM.getTags().noneMatch(p -> p.getFirst().equals(ModTags.SANDWICH_BREAD))
        ) {
            return translateItem("sandwich");
        }

        String sandwichName = sandwich.items().getLast().is(ModTags.BURGER_BUNS)
                && sandwich.items().getFirst().is(ModTags.BURGER_BUNS)
                ? "burger" : "sandwich";

        int amountOfBread = getAmountOfBread(sandwich);

        // full bread sandwich
        if (sandwich.items().size() == amountOfBread) {
            return getBreadSandwichName(sandwich, sandwichName);
        }

        List<ItemStack> uniqueIngredients = getUniqueIngredientsExcludingBread(sandwich);

        // potion sandwich
        if (uniqueIngredients.size() == 1 && uniqueIngredients.getFirst().is(Items.POTION)) {
            PotionContents contents = uniqueIngredients.getFirst().getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
            Holder<Potion> potion = contents.potion().orElse(Potions.WATER);
            if (potion.value().getEffects().size() == 1) {
                return translateItem("potion_%s".formatted(sandwichName), potion.value().getEffects().getFirst().getEffect().value().getDisplayName());
            }
        }

        boolean isOpenFacedSandwich = amountOfBread == 1 && sandwich.items().size() > 1;

        if (!uniqueIngredients.isEmpty() && uniqueIngredients.size() <= 3) {
            Component ingredientList = listIngredients(uniqueIngredients);
            if (sandwich.isDoubleDecker()) {
                return translateItem("double_decker_ingredients_%s".formatted(sandwichName), ingredientList);
            } else if (isOpenFacedSandwich) {
                return translateItem("open_faced_ingredients_sandwich", ingredientList);
            } else {
                return translateItem("ingredients_%s".formatted(sandwichName), ingredientList);
            }
        }

        if (sandwich.isDoubleDecker()) {
            return translateItem("double_decker_%s".formatted(sandwichName));
        } else if (isOpenFacedSandwich) {
            return translateItem("open_faced_sandwich");
        } else {
            return translateItem(sandwichName);
        }
    }

    private static List<ItemStack> getUniqueIngredientsExcludingBread(SandwichContents sandwich) {
        List<ItemStack> result = new ArrayList<>();
        for (ItemStack ingredient : sandwich.items()) {
            if (!ingredient.is(ModTags.SANDWICH_BREAD) && result.stream().noneMatch(stack -> ItemStack.matches(ingredient, stack))) {
                result.add(ingredient);
            }
        }
        return result;
    }

    private static int getAmountOfBread(SandwichContents sandwich) {
        int result = 0;
        for (ItemStack ingredient : sandwich.items()) {
            if (ingredient.is(ModTags.SANDWICH_BREAD)) {
                result++;
            }
        }
        return result;
    }

    private static Component getBreadSandwichName(SandwichContents sandwich, String sandwichName) {
        if ((sandwich.items().size() == 3)
                && sandwich.items().getFirst().getItem() != ModItems.TOASTED_BREAD_SLICE.get()
                && sandwich.items().get(1).getItem() == ModItems.TOASTED_BREAD_SLICE.get()
                && sandwich.items().get(2).getItem() != ModItems.TOASTED_BREAD_SLICE.get()) {
            return translateItem("ingredients_%s".formatted(sandwichName), Ingredients.getDisplayName(sandwich.items().get(1)));
        }
        return translateItem("bread_%s".formatted(sandwichName));
    }

    private static Component listIngredients(List<ItemStack> ingredients) {
        List<Component> ingredientNames = ingredients.stream().map(Ingredients::getDisplayName).toList();
        return SomeAssemblyRequired.translate("tooltip.ingredient_list.%s".formatted(ingredientNames.size()), ingredientNames.toArray());
    }

    private static Component translateItem(String name, Object... args) {
        return Component.translatable("item.%s.%s".formatted(SomeAssemblyRequired.MOD_ID, name), args);
    }
}
