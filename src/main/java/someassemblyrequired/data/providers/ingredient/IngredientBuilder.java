package someassemblyrequired.data.providers.ingredient;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.ingredient.IngredientProperties;
import someassemblyrequired.registry.ModDataComponents;
import someassemblyrequired.registry.ModItems;
import someassemblyrequired.registry.ModSoundEvents;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("UnusedReturnValue")
public class IngredientBuilder {

    private final Holder<Item> item;

    @Nullable
    private Component displayName;
    @Nullable
    private Component fullName;
    @Nullable
    private SoundEvent soundEvent;

    private ItemStack displayItem = ItemStack.EMPTY;

    private int height = 1;

    private boolean renderAsItem = true;

    private FoodProperties foodProperties = null;

    private boolean hidden = false;

    public IngredientBuilder(Holder<Item> item) {
        this.item = item;
    }

    public IngredientProperties build() {
        return new IngredientProperties(item,
                Optional.ofNullable(foodProperties),
                Optional.ofNullable(displayName),
                Optional.ofNullable(fullName),
                displayItem,
                soundEvent == null ? ModSoundEvents.ADD_ITEM : BuiltInRegistries.SOUND_EVENT.getHolderOrThrow(BuiltInRegistries.SOUND_EVENT.getResourceKey(soundEvent).orElseThrow()),
                height,
                renderAsItem,
                hidden
        );
    }

    public Holder<Item> getItem() {
        return item;
    }

    public ItemStack getDisplayItem() {
        return displayItem;
    }

    public IngredientBuilder setDisplayName(Component displayName) {
        this.displayName = displayName;
        return this;
    }

    public IngredientBuilder setDisplayName(String translationKey) {
        return setDisplayName(Component.translatable(translationKey));
    }

    public IngredientBuilder setDisplayName(Holder<Item> item) {
        return setDisplayName(getDefaultTranslationKey(item));
    }

    public IngredientBuilder setCustomDisplayName() {
        return setDisplayName(getItem());
    }

    public IngredientBuilder setFullName(Component fullName) {
        this.fullName = fullName;
        return this;
    }

    public IngredientBuilder setFullName(String translationKey) {
        return setFullName(Component.translatable(translationKey));
    }

    public IngredientBuilder setFullName(Holder<Item> item) {
        return setFullName(getDefaultTranslationKey(item));
    }

    public IngredientBuilder setCustomFullName() {
        return setFullName(getDefaultTranslationKey(getItem()));
    }

    public IngredientBuilder setDisplayItem(ItemStack displayItem) {
        this.displayItem = displayItem;
        return this;
    }

    public IngredientBuilder setSpread(int color) {
        return setSpread(color, 1);
    }

    public IngredientBuilder setSpread(int color, double alpha) {
        ItemStack spread = new ItemStack(ModItems.SPREAD.get());
        int a = (int) (alpha * 0xFF);
        spread.set(ModDataComponents.SPREAD_COLOR, (color & 0xFFFFFF) | (a << 24));
        return setDisplayItem(spread);
    }

    public IngredientBuilder setWetSound() {
        return setSound(ModSoundEvents.ADD_ITEM_WET.get());
    }

    public IngredientBuilder setMoistSound() {
        return setSound(ModSoundEvents.ADD_ITEM_MOIST.get());
    }

    public IngredientBuilder setSlimySound() {
        return setSound(ModSoundEvents.ADD_ITEM_SLIMY.get());
    }

    public IngredientBuilder setLeafySound() {
        return setSound(ModSoundEvents.ADD_ITEM_LEAFY.get());
    }

    public IngredientBuilder setSound(SoundEvent soundEvent) {
        this.soundEvent = soundEvent;
        return this;
    }

    public IngredientBuilder setHeight(int height) {
        this.height = height;
        return this;
    }

    public IngredientBuilder customModel() {
        this.renderAsItem = false;
        return this;
    }

    public IngredientBuilder hidden() {
        this.hidden = true;
        return this;
    }

    public IngredientBuilder setFoodProperties(FoodProperties foodProperties) {
        this.foodProperties = foodProperties;
        return this;
    }

    private static String getDefaultTranslationKey(Holder<Item> item) {
        ResourceLocation id = Objects.requireNonNull(item.getKey()).location();
        if ("minecraft".equals(id.getNamespace()) || SomeAssemblyRequired.MOD_ID.equals(id.getNamespace())) {
            return "%s.ingredient.%s".formatted(SomeAssemblyRequired.MOD_ID, id.getPath());
        } else {
            return "%s.ingredient.%s.%s".formatted(SomeAssemblyRequired.MOD_ID, id.getNamespace(), id.getPath());
        }
    }
}
