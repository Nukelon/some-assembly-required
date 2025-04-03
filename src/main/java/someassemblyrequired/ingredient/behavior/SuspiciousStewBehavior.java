package someassemblyrequired.ingredient.behavior;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.SuspiciousStewEffects;

public class SuspiciousStewBehavior implements IngredientBehavior {

    @Override
    public void onEaten(ItemStack item, LivingEntity entity) {
        SuspiciousStewEffects suspicioussteweffects = item.getOrDefault(DataComponents.SUSPICIOUS_STEW_EFFECTS, SuspiciousStewEffects.EMPTY);

        for (SuspiciousStewEffects.Entry entry : suspicioussteweffects.effects()) {
            entity.addEffect(entry.createEffectInstance());
        }
    }
}
