package someassemblyrequired.block;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.config.ModConfig;
import someassemblyrequired.ingredient.Ingredients;
import someassemblyrequired.item.sandwich.SandwichContents;
import someassemblyrequired.registry.ModBlockEntityTypes;
import someassemblyrequired.registry.ModBlocks;
import someassemblyrequired.registry.ModDataComponents;
import someassemblyrequired.registry.ModItems;

import java.util.ArrayList;
import java.util.List;

public class SandwichBlockEntity extends BlockEntity {

    private SandwichContents contents = SandwichContents.EMPTY;

    public SandwichBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.SANDWICH.get(), pos, state);
    }

    public SandwichContents getContents() {
        return contents;
    }

    private void setContents(SandwichContents contents) {
        this.contents = contents;
        onSandwichUpdated();
    }

    public InteractionResult useWithoutItem(Player player) {
        if (level == null) {
            return InteractionResult.SUCCESS_NO_ITEM_USED;
        }

        ItemStack stack = contents.getLast();
        Ingredients.playRemoveSound(stack, level, player, getBlockPos());

        setContents(getContents().dropLast());

        BlockPos pos = getBlockPos();
        if (Ingredients.getFood(stack, player).usingConvertsTo().isEmpty() && !player.isCreative()) {
            double y = pos.getY() + Math.max(0.2, contents.getTotalHeight() * (1 / 32D) - 0.2);
            ItemEntity item = new ItemEntity(level, pos.getX() + 0.5, y, pos.getZ() + 0.5, stack);
            item.setPickUpDelay(5);
            level.addFreshEntity(item);
        }

        onSandwichUpdated();
        return InteractionResult.SUCCESS_NO_ITEM_USED;
    }

    public ItemInteractionResult useItemOn(ItemStack useItem, Player player, InteractionHand hand) {
        if (useItem.isEmpty()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        } else if (!Ingredients.canAddToSandwich(useItem)) {
            return ItemInteractionResult.CONSUME;
        }

        List<ItemStack> itemsToAdd = new ArrayList<>();
        if (useItem.is(ModItems.SANDWICH)) {
            itemsToAdd.addAll(SandwichContents.get(useItem));
        } else {
            itemsToAdd.add(useItem);
        }

        int totalHeight = getContents().getTotalHeight();
        for (ItemStack item : itemsToAdd) {
            totalHeight += Ingredients.getHeight(item);
        }

        if (totalHeight > ModConfig.server.maximumSandwichHeight.get()) {
            player.displayClientMessage(SomeAssemblyRequired.translate("message.full_sandwich"), true);
        } else {
            setContents(getContents().concat(itemsToAdd));
            Ingredients.playApplySound(useItem, getLevel(), player, getBlockPos());
            shrinkHeldItem(player, hand);
        }
        return ItemInteractionResult.SUCCESS;
    }

    private static void shrinkHeldItem(Player player, InteractionHand hand) {
        if (!player.isCreative()) {
            ItemStack heldItem = player.getItemInHand(hand);
            ItemStack container = Ingredients.getFood(heldItem, player).usingConvertsTo().orElse(ItemStack.EMPTY);
            heldItem.shrink(1);
            if (!container.isEmpty()) {
                ItemStack stack = container.copy();
                if (heldItem.isEmpty()) {
                    player.setItemInHand(hand, stack);
                } else if (!player.getInventory().add(stack)) {
                    player.drop(stack, false);
                }
            }
        }
    }

    public void onSandwichUpdated() {
        if (level != null) {
            if (contents.isEmpty()) {
                level.removeBlock(getBlockPos(), false);
            } else {
                BlockState state = level.getBlockState(getBlockPos());
                if (state.is(ModBlocks.SANDWICH.get())) {
                    BlockState newState = state.setValue(SandwichBlock.SIZE, SandwichBlock.getSizeFromSandwich(contents));
                    level.setBlockAndUpdate(getBlockPos(), newState);
                }
                setChanged();
            }
        }
    }

    @Override
    protected void applyImplicitComponents(DataComponentInput componentInput) {
        super.applyImplicitComponents(componentInput);
        contents = componentInput.getOrDefault(ModDataComponents.SANDWICH_CONTENTS.get(), SandwichContents.EMPTY);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        components.set(ModDataComponents.SANDWICH_CONTENTS.get(), contents);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void removeComponentsFromTag(CompoundTag tag) {
        tag.remove("Sandwich");
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveCustomOnly(registries);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registries) {
        loadAdditional(tag, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("Sandwich")) {
            SandwichContents.CODEC
                    .decode(registries.createSerializationContext(NbtOps.INSTANCE), tag.get("Sandwich"))
                    .ifError(error -> SomeAssemblyRequired.LOGGER.error(error.message()))
                    .map(Pair::getFirst)
                    .result().ifPresent(sandwich -> contents = sandwich);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Sandwich",
                SandwichContents.CODEC
                        .encodeStart(registries.createSerializationContext(NbtOps.INSTANCE), contents)
                        .getOrThrow()
        );
    }
}
