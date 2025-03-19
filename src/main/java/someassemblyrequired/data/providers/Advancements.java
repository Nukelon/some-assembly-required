package someassemblyrequired.data.providers;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.item.sandwich.SandwichItem;
import someassemblyrequired.registry.ModAdvancementTriggers;
import someassemblyrequired.registry.ModItems;
import someassemblyrequired.registry.ModTags;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class Advancements extends ForgeAdvancementProvider {

    public Advancements(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
        super(output, registries, existingFileHelper, List.of(Advancements::generate));
    }
    private static void generate(HolderLookup.Provider registries, Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {
        ResourceLocation plantSeed = new ResourceLocation("minecraft:husbandry/plant_seed");

        ResourceLocation obtainBreadSliceId = SomeAssemblyRequired.id("obtain_bread_slice");
        Advancement obtainBreadSlice = advancement(obtainBreadSliceId, new ItemStack(ModItems.BREAD_SLICE.get()), false)
                .parent(plantSeed)
                .addCriterion(obtainBreadSliceId.getPath(), InventoryChangeTrigger.TriggerInstance.hasItems(
                        ItemPredicate.Builder.item().of(ModTags.BREAD_SLICES).build()
                )).save(saver, obtainBreadSliceId, existingFileHelper);

        ResourceLocation obtainSandwichId = SomeAssemblyRequired.id("obtain_sandwich");
        var obtainSandwich = advancement(obtainSandwichId, SandwichItem.makeSandwich(ModItems.TOMATO_SLICES.get(), ModItems.CHOPPED_CARROT.get()), false)
                .parent(obtainBreadSlice)
                .addCriterion(obtainSandwichId.getPath(), InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.SANDWICH.get()
                )).save(saver, obtainSandwichId, existingFileHelper);

        ResourceLocation obtainToastedBreadSliceId = SomeAssemblyRequired.id("obtain_toasted_bread_slice");
        advancement(obtainToastedBreadSliceId, new ItemStack(ModItems.TOASTED_BREAD_SLICE.get()), false)
                .parent(obtainBreadSlice)
                .addCriterion(obtainToastedBreadSliceId.getPath(), InventoryChangeTrigger.TriggerInstance.hasItems(
                        ModItems.TOASTED_BREAD_SLICE.get()
                )).save(saver, obtainToastedBreadSliceId, existingFileHelper);

        ResourceLocation consumePotionSandwichId = SomeAssemblyRequired.id("consume_potion_sandwich");
        advancement(consumePotionSandwichId, SandwichItem.makeSandwich(Potions.NIGHT_VISION), true)
                .parent(obtainSandwich)
                .addCriterion(consumePotionSandwichId.getPath(), ModAdvancementTriggers.CONSUME_POTION_SANDWICH.instance())
                .save(saver, consumePotionSandwichId, existingFileHelper);

        ResourceLocation consumeDoubleDeckerSandwichId = SomeAssemblyRequired.id("consume_double_decker_sandwich");
        advancement(consumeDoubleDeckerSandwichId, SandwichItem.makeSandwich(
                ModItems.TOMATO_SLICES.get(),
                ModItems.CHOPPED_CARROT.get(),
                ModItems.BREAD_SLICE.get(),
                ModItems.TOMATO_SLICES.get(),
                ModItems.CHOPPED_CARROT.get()
        ), true)
                .parent(obtainSandwich)
                .addCriterion(consumeDoubleDeckerSandwichId.getPath(), ModAdvancementTriggers.CONSUME_DOUBLE_DECKER_SANDWICH.instance())
                .save(saver, consumeDoubleDeckerSandwichId, existingFileHelper);

        ResourceLocation consume100SandwichesId = SomeAssemblyRequired.id("consume_1000_sandwiches");
        advancement(consume100SandwichesId, SandwichItem.makeSandwich(
                Items.GOLD_BLOCK
        ), FrameType.CHALLENGE, true)
                .parent(obtainSandwich)
                .addCriterion(consume100SandwichesId.getPath(), new PlayerTrigger.TriggerInstance(ModAdvancementTriggers.CONSUME_1000_SANDWICHES.getId(), ContextAwarePredicate.ANY))
                .save(saver, consume100SandwichesId, existingFileHelper);

        ResourceLocation consume1000BurgersId = SomeAssemblyRequired.id("consume_1000_burgers");
        advancement(consume1000BurgersId, SandwichItem.of(
                new ItemStack(ModItems.BURGER_BUN_BOTTOM.get()),
                new ItemStack(Items.GOLD_BLOCK),
                new ItemStack(ModItems.BURGER_BUN_TOP.get())
        ), FrameType.CHALLENGE, true)
                .parent(obtainSandwich)
                .addCriterion(consume1000BurgersId.getPath(), new PlayerTrigger.TriggerInstance(ModAdvancementTriggers.CONSUME_1000_BURGERS.getId(), ContextAwarePredicate.ANY))
                .save(saver, consume1000BurgersId, existingFileHelper);
    }

    private static Advancement.Builder advancement(ResourceLocation id, ItemStack icon, FrameType frameType, boolean hidden) {
        return Advancement.Builder.advancement().display(display(id.getPath(), icon, frameType, hidden));
    }

    private static Advancement.Builder advancement(ResourceLocation id, ItemStack icon, boolean hidden) {
        return Advancement.Builder.advancement().display(display(id.getPath(), icon, hidden));
    }

    private static DisplayInfo display(String title, ItemStack icon, boolean hidden) {
        return display(title, icon, FrameType.TASK, hidden);
    }

    private static DisplayInfo display(String title, ItemStack icon, FrameType frameType, boolean hidden) {
        return new DisplayInfo(
                icon,
                Component.translatable("%s.advancement.%s.title".formatted(SomeAssemblyRequired.MOD_ID, title)),
                Component.translatable("%s.advancement.%s.description".formatted(SomeAssemblyRequired.MOD_ID, title)),
                null,
                frameType,
                true,
                true,
                hidden
        );
    }
}
