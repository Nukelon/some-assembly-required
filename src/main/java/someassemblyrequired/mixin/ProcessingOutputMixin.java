package someassemblyrequired.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import someassemblyrequired.registry.ModDataComponents;
import someassemblyrequired.registry.ModItems;

/*
 * TODO temporary fix until Create v6.0.5
 *  https://github.com/Creators-of-Create/Create/pull/8105
 */
@Pseudo
@Mixin(ProcessingOutput.class)
public class ProcessingOutputMixin {

    @Shadow @Final
    private DataComponentPatch patch;

    @ModifyReturnValue(method = "getStack", at = @At("RETURN"))
    public ItemStack getStack(ItemStack original) {
        if (original.is(ModItems.SANDWICH) && !original.has(ModDataComponents.SANDWICH_CONTENTS)) {
            original.applyComponents(patch);
        }
        return original;
    }

    @ModifyReturnValue(method = "rollOutput", at = @At("RETURN"))
    public ItemStack rollOutput(ItemStack original) {
        if (original.is(ModItems.SANDWICH) && !original.has(ModDataComponents.SANDWICH_CONTENTS)) {
            original.applyComponents(patch);
        }
        return original;
    }
}
