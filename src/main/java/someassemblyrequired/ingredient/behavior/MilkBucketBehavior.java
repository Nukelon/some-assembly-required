package someassemblyrequired.ingredient.behavior;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.EffectCures;

public class MilkBucketBehavior implements IngredientBehavior {

    @Override
    public void onEaten(ItemStack item, LivingEntity entity) {
        if (!entity.level().isClientSide) {
            entity.removeEffectsCuredBy(EffectCures.MILK);
        }
    }
}
