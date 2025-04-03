package someassemblyrequired.ingredient;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import someassemblyrequired.registry.ModSoundEvents;

import java.util.Optional;

@SuppressWarnings("unused")
public record IngredientProperties(
        Holder<Item> item,
        Optional<FoodProperties> food,
        Optional<Component> displayName,
        Optional<Component> fullName,
        ItemStack displayItem,
        Holder<SoundEvent> sound,
        int height,
        boolean renderAsItem
) implements IngredientPropertiesBase {

    public static final Codec<IngredientProperties> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.ITEM.holderByNameCodec().fieldOf("item").forGetter(IngredientProperties::item),
            FoodProperties.DIRECT_CODEC.optionalFieldOf("food").forGetter(IngredientProperties::food),
            ComponentSerialization.CODEC.optionalFieldOf("display_name").forGetter(IngredientProperties::displayName),
            ComponentSerialization.CODEC.optionalFieldOf("full_name").forGetter(IngredientProperties::fullName),
            ItemStack.CODEC.optionalFieldOf("display_item", ItemStack.EMPTY).forGetter(IngredientProperties::displayItem),
            BuiltInRegistries.SOUND_EVENT.holderByNameCodec().optionalFieldOf("sound", ModSoundEvents.ADD_ITEM).forGetter(IngredientProperties::sound),
            ExtraCodecs.intRange(1, 32).optionalFieldOf("height", 1).forGetter(IngredientProperties::height),
            Codec.BOOL.optionalFieldOf("render_as_item", true).forGetter(IngredientProperties::renderAsItem)
    ).apply(instance, IngredientProperties::new));
}
