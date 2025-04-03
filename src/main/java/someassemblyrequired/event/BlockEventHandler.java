package someassemblyrequired.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.block.SandwichBlockEntity;
import someassemblyrequired.ingredient.Ingredients;
import someassemblyrequired.item.sandwich.SandwichItem;
import someassemblyrequired.registry.ModItems;
import someassemblyrequired.registry.ModTags;

public class BlockEventHandler {

    public static void register() {
        NeoForge.EVENT_BUS.addListener(BlockEventHandler::onRightClickBlock);
    }

    private static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.isCanceled() || event.getUseBlock() != TriState.DEFAULT || event.getUseItem() != TriState.DEFAULT) {
            return;
        }

        BlockPos pos = event.getHitVec().getBlockPos();
        Level level = event.getLevel();
        BlockState state = level.getBlockState(pos);
        Player player = event.getEntity();
        InteractionHand hand = event.getHand();

        if (player.isShiftKeyDown()) {
            return;
        }

        if (state.is(ModTags.SANDWICHING_STATIONS)) {
            InteractionResult result;
            if (level.getBlockEntity(pos.above()) instanceof SandwichBlockEntity blockEntity) {
                if (player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty() && player.getItemInHand(InteractionHand.OFF_HAND).isEmpty()) {
                    result = blockEntity.useWithoutItem(player);
                } else if (!player.getItemInHand(hand).isEmpty()) {
                    result = blockEntity.useItemOn(player.getItemInHand(hand), player, hand).result();
                } else {
                    result = InteractionResult.PASS;
                }
            } else {
                result = tryPlaceSandwich(event);
            }

            if (result != InteractionResult.PASS) {
                event.setCanceled(true);
                event.setCancellationResult(result);
            }
        }
    }

    private static InteractionResult tryPlaceSandwich(PlayerInteractEvent.RightClickBlock event) {
        ItemStack heldItem = event.getItemStack().copy();
        Player player = event.getEntity();
        InteractionHand hand = event.getHand();
        BlockHitResult hitResult = event.getHitVec();
        BlockPos pos = hitResult.getBlockPos();
        heldItem.setCount(1);

        if (!Ingredients.canAddToSandwich(heldItem)) {
            return InteractionResult.PASS;
        } else if (!heldItem.is(ModItems.SANDWICH.get()) && !heldItem.is(ModTags.SANDWICH_BREAD)) {
            player.displayClientMessage(SomeAssemblyRequired.translate("message.bottom_bread"), true);
            return InteractionResult.SUCCESS;
        }

        ItemStack sandwich = SandwichItem.of(heldItem);
        UseOnContext useOnContext = new UseOnContext(player, hand, hitResult);
        InteractionResult placeResult = ModItems.SANDWICH.get().place(useOnContext, pos.above(), sandwich);

        if (sandwich.isEmpty()) {
            player.getItemInHand(hand).shrink(1);
        }

        return placeResult;
    }
}
