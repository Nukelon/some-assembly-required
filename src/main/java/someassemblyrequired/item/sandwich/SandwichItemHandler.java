package someassemblyrequired.item.sandwich;

import com.mojang.datafixers.util.Pair;
import net.minecraft.ResourceLocationException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.registries.ForgeRegistries;
import someassemblyrequired.config.ModConfig;
import someassemblyrequired.ingredient.Ingredients;
import someassemblyrequired.mixin.FoodPropertiesMixin;
import someassemblyrequired.registry.ModFoods;
import someassemblyrequired.registry.ModItems;
import someassemblyrequired.registry.ModTags;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

public class SandwichItemHandler implements IItemHandler, IItemHandlerModifiable, INBTSerializable<ListTag>, Iterable<ItemStack> {

    protected final List<ItemStack> items;
    protected FoodProperties foodProperties;
    protected MobEffectInstance effect;

    public SandwichItemHandler() {
        this.items = new ArrayList<>();
        this.foodProperties = ModFoods.EMPTY;
    }

    public static Optional<SandwichItemHandler> get(@Nullable ICapabilityProvider capabilityProvider) {
        if (capabilityProvider == null) {
            return Optional.empty();
        }
        return capabilityProvider.getCapability(ForgeCapabilities.ITEM_HANDLER)
                .filter(handler -> handler instanceof SandwichItemHandler)
                .map(handler -> ((SandwichItemHandler) handler));
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public FoodProperties getFoodProperties() {
        return foodProperties;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int getItemCount() {
        return items.size();
    }

    public int getTotalHeight() {
        int size = 0;
        for (ItemStack item : items) {
            size += Ingredients.getHeight(item);
        }
        return size;
    }

    public int getTotalNutrition() {
        int result = 0;
        for (ItemStack stack : items) {
            result += Ingredients.getFood(stack, null).getNutrition();
        }
        return result;
    }

    public float getAverageSaturation() {
        float totalSaturation = 0;
        for (ItemStack stack : items) {
            FoodProperties food = Ingredients.getFood(stack, null);
            totalSaturation += food.getSaturationModifier() * food.getNutrition();
        }
        return totalSaturation / getTotalNutrition();
    }

    @Nullable
    public MobEffectInstance getEffect() {
        return effect;
    }

    private void updateFoodProperties() {
        if (isEmpty()) {
            foodProperties = ModFoods.EMPTY;
        } else {
            FoodProperties.Builder builder = new FoodProperties.Builder()
                    .nutrition(getTotalNutrition())
                    .saturationMod(getAverageSaturation());

            Set<Item> uniqueIngredients = new HashSet<>();

            for (ItemStack item : items) {
                FoodProperties food = Ingredients.getFood(item, null);

                for (Pair<Supplier<MobEffectInstance>, Float> pair : ((FoodPropertiesMixin) food).getEffectSuppliers()) {
                    builder.effect(pair.getFirst(), pair.getSecond());
                }

                if (food.getNutrition() > 0 && food.getEffects().isEmpty() && !item.is(ModTags.SANDWICH_BREAD)) {
                    uniqueIngredients.add(item.getItem());
                }
            }

            addBonusEffect(builder, uniqueIngredients.size());

            foodProperties = builder.build();
        }
    }

    public void addBonusEffect(FoodProperties.Builder builder, int uniqueIngredientCount) {
        String effectName = isBurger() ? ModConfig.server.burgerBonusEffect.get() : ModConfig.server.sandwichBonusEffect.get();
        List<Integer> durations = isBurger() ? ModConfig.server.burgerEffectDurations.get() : ModConfig.server.sandwichEffectDurations.get();;
        uniqueIngredientCount = Math.min(durations.size() - 1, uniqueIngredientCount);
        try {
            ResourceLocation effectId = new ResourceLocation(effectName);
            MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(effectId);
            if (effect != null && !durations.isEmpty()) {
                int duration = durations.get(uniqueIngredientCount);
                if (duration > 0) {
                    this.effect = new MobEffectInstance(effect, duration * 20, 0);
                    builder.effect(() -> this.effect, 1F);
                }
            }
        } catch (ResourceLocationException ignored) {

        }
    }
    public void add(ItemStack stack) {
        if (stack.isEmpty()) {
            throw new IllegalArgumentException();
        }
        items.add(stack);
        onContentsChanged();
    }

    public boolean canAdd(SandwichItemHandler sandwich) {
        return this.getTotalHeight() + sandwich.getTotalHeight() <= ModConfig.server.maximumSandwichHeight.get();
    }

    public void add(SandwichItemHandler sandwich) {
        items.addAll(sandwich.items);
        onContentsChanged();
    }

    public void pop() {
        items.remove(items.size() - 1);
        onContentsChanged();
    }

    public void setItems(List<ItemStack> items) {
        this.items.clear();
        this.items.addAll(items);
        onContentsChanged();
    }

    public ItemStack bottom() {
        return items.get(0);
    }

    public ItemStack top() {
        return items.get(items.size() - 1);
    }

    public boolean hasTopAndBottomBread() {
        return getItemCount() > 0 && bottom().is(ModTags.SANDWICH_BREAD) && top().is(ModTags.SANDWICH_BREAD);
    }

    public boolean isBurger() {
        return getItemCount() > 0 && bottom().is(ModTags.BURGER_BUNS) && top().is(ModTags.BURGER_BUNS);
    }

    public boolean isDoubleDeckerSandwich() {
        if (getItemCount() < 5) {
            return false;
        }
        if (!hasTopAndBottomBread()) {
            return false;
        }

        boolean foundBread = false;
        for (int i = 1; i < getItemCount() - 2; i++) {
            if (items.get(i).is(ModTags.SANDWICH_BREAD)) {
                if (foundBread) {
                    return false;
                }
                foundBread = true;
            }
        }

        return foundBread && !items.get(1).is(ModTags.SANDWICH_BREAD) && !items.get(getItemCount() - 2).is(ModTags.SANDWICH_BREAD);
    }

    public ItemStack getAsItem() {
        ItemStack result = new ItemStack(ModItems.SANDWICH.get());
        result.getOrCreateTagElement("BlockEntityTag").put("Sandwich", serializeNBT());
        return result;
    }

    @Override
    public int getSlots() {
        return items.size();
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        if (stack.isEmpty()) {
            throw new IllegalArgumentException();
        }
        validateSlotIndex(slot);
        items.set(slot, stack);
        onContentsChanged();
    }

    @Override
    @Nonnull
    public ItemStack getStackInSlot(int slot) {
        validateSlotIndex(slot);
        return items.get(slot);
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return stack;
    }

    @Override
    @Nonnull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return false;
    }

    @Override
    public ListTag serializeNBT() {
        return serializeItems(items);
    }

    public static ListTag serializeItems(List<ItemStack> items) {
        ListTag result = new ListTag();
        for (ItemStack stack : items) {
            result.add(stack.save(new CompoundTag()));
        }
        return result;
    }

    @Override
    public void deserializeNBT(ListTag listTag) {
        items.clear();
        for (int i = 0; i < listTag.size(); i++) {
            items.add(ItemStack.of(listTag.getCompound(i)));
        }
        onLoad();
    }

    @Override
    public Iterator<ItemStack> iterator() {
        return items.iterator();
    }

    protected void validateSlotIndex(int slot) {
        if (slot < 0 || slot >= items.size()) {
            throw new IndexOutOfBoundsException("Slot " + slot + " not in valid range - [0," + items.size() + ")");
        }
    }

    protected void onLoad() {
        updateFoodProperties();
    }

    protected void onContentsChanged() {
        updateFoodProperties();
    }
}
