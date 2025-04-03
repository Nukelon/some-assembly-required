package someassemblyrequired.item.sandwich;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.block.SandwichBlock;
import someassemblyrequired.ingredient.Ingredients;
import someassemblyrequired.integration.ModCompat;
import someassemblyrequired.registry.*;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class SandwichItem extends BlockItem {

    public SandwichItem(Block block, Properties builder) {
        super(block, builder);
    }

    public static ItemStack makeSandwich(Holder<Potion> potion) {
        return makeSandwich(PotionContents.createItemStack(Items.POTION, potion));
    }

    public static ItemStack makeSandwich(ItemLike... items) {
        return makeSandwich(Arrays.stream(items)
                .map(ItemStack::new)
                .toArray(ItemStack[]::new));
    }

    public static ItemStack makeSandwich(ItemStack... items) {
        ArrayList<ItemStack> list = new ArrayList<>();
        list.add(new ItemStack(ModItems.BREAD_SLICE.get()));
        list.addAll(Arrays.asList(items));
        list.add(new ItemStack(ModItems.BREAD_SLICE.get()));
        return of(list);
    }

    public static ItemStack makeBurger(ItemLike... items) {
        return makeBurger(Arrays.stream(items)
                .map(ItemStack::new)
                .toArray(ItemStack[]::new));
    }

    public static ItemStack makeBurger(ItemStack... items) {
        ArrayList<ItemStack> list = new ArrayList<>();
        list.add(new ItemStack(ModItems.BURGER_BUN_BOTTOM.get()));
        list.addAll(Arrays.asList(items));
        list.add(new ItemStack(ModItems.BURGER_BUN_TOP.get()));
        return of(list);
    }

    public static ItemStack of(ItemStack... items) {
        return of(Arrays.asList(items));
    }

    public static ItemStack of(List<ItemStack> items) {
        List<ItemStack> flattenedItems = new ArrayList<>();
        for (ItemStack item : items) {
            if (item.getCount() != 1) {
                throw new IllegalArgumentException();
            } else if (item.is(ModItems.SANDWICH.get())) {
                flattenedItems.addAll(SandwichContents.get(item).items());
            } else {
                flattenedItems.add(item);
            }
        }

        ItemStack result = new ItemStack(ModItems.SANDWICH.get());
        SandwichContents contents = new SandwichContents(flattenedItems);
        result.set(ModDataComponents.SANDWICH_CONTENTS.get(), contents);
        return result;
    }

    @Override
    public FoodProperties getFoodProperties(ItemStack stack, @Nullable LivingEntity entity) {
        return stack.getOrDefault(ModDataComponents.SANDWICH_CONTENTS.get(), SandwichContents.EMPTY).createFoodProperties(entity);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        SandwichContents sandwich = SandwichContents.get(stack);

        sandwich.items().stream()
                .collect(
                        Collectors.groupingBy(
                                item -> Ingredients.getFullName(item).plainCopy(),
                                LinkedHashMap::new,
                                Collectors.counting()
                        )
                )
                .forEach(
                        (item, count) -> {
                            if (count > 1) {
                                tooltip.add(SomeAssemblyRequired.translate("tooltip.ingredient_count", item, count).withStyle(ChatFormatting.GRAY));
                            } else {
                                tooltip.add(item.withStyle(ChatFormatting.GRAY));
                            }
                        }
                );

        FoodProperties food = getFoodProperties(stack, null);
        if (ModCompat.isFarmersDelightLoaded() && food != null && !food.effects().isEmpty()) {
            tooltip.add(CommonComponents.EMPTY);
            TextUtils.addFoodEffectTooltip(stack, tooltip::add, 1, context.tickRate());
        }
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
        boolean isPlacingOnTable = context.getLevel().getBlockState(context.getClickedPos().below()).is(ModTags.SANDWICHING_STATIONS);
        if (context.getPlayer() != null && (context.getPlayer().isShiftKeyDown() || isPlacingOnTable)) {
            return super.placeBlock(context, state);
        }
        return false;
    }

    public InteractionResult place(UseOnContext useOnContext, BlockPos pos, ItemStack sandwich) {
        BlockPos clickedPos = pos.below();
        BlockPlaceContext placeContext = BlockPlaceContext.at(new BlockPlaceContext(useOnContext), clickedPos, Direction.UP);
        if (!placeContext.canPlace()) {
            return InteractionResult.FAIL;
        }
        placeContext = this.updatePlacementContext(placeContext);
        if (placeContext == null) {
            return InteractionResult.FAIL;
        }
        BlockState blockstate = this.getPlacementState(placeContext);
        if (blockstate == null) {
            return InteractionResult.FAIL;
        }

        SandwichContents contents = SandwichContents.get(sandwich);
        if (contents.items().isEmpty()) {
            return InteractionResult.FAIL;
        }
        int size = SandwichBlock.getSizeFromSandwich(contents);
        blockstate = blockstate.setValue(SandwichBlock.SIZE, size);

        if (!placeBlock(placeContext, blockstate)) {
            return InteractionResult.FAIL;
        }
        Level level = placeContext.getLevel();
        Player player = placeContext.getPlayer();
        BlockState placedState = level.getBlockState(pos);
        if (placedState.is(blockstate.getBlock())) {
            updateCustomBlockEntityTag(pos, level, player, sandwich, placedState);
            updateBlockEntityComponents(level, pos, sandwich);
            placedState.getBlock().setPlacedBy(level, pos, placedState, player, sandwich);
            if (player instanceof ServerPlayer serverPlayer) {
                CriteriaTriggers.PLACED_BLOCK.trigger(serverPlayer, pos, sandwich);
            }
        }

        level.gameEvent(player, GameEvent.BLOCK_PLACE, pos);
        SoundType soundType = placedState.getSoundType(level, pos, player);
        if (player != null) {
            level.playSound(player, pos, getPlaceSound(placedState, level, pos, player), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
        }
        if (player == null || !player.getAbilities().instabuild) {
            sandwich.shrink(1);
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private static void updateBlockEntityComponents(Level level, BlockPos pos, ItemStack stack) {
        BlockEntity blockentity = level.getBlockEntity(pos);
        if (blockentity != null) {
            blockentity.applyComponentsFromItemStack(stack);
            blockentity.setChanged();
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity entity) {
        SandwichContents sandwich = SandwichContents.get(stack);
        for (ItemStack item : sandwich.items()) {
            Ingredients.applyIngredientBehaviours(item, entity);
        }
        if (entity instanceof ServerPlayer player) {
            if (sandwich.isBurger()) {
                player.awardStat(ModStatistics.BURGERS_EATEN.get());
            } else {
                player.awardStat(ModStatistics.SANDWICHES_EATEN.get());
            }
            triggerAdvancements(stack, player);
        }

        return super.finishUsingItem(stack, world, entity);
    }

    private void triggerAdvancements(ItemStack stack, ServerPlayer player) {
        SandwichContents sandwich = SandwichContents.get(stack);
        if (sandwich.isBurger()) {
            if (player.getStats().getValue(Stats.CUSTOM.get(ModStatistics.BURGERS_EATEN.get())) >= 1000) {
                ModAdvancementTriggers.CONSUME_1000_BURGERS.get().trigger(player);
            }
        } else {
            if (player.getStats().getValue(Stats.CUSTOM.get(ModStatistics.SANDWICHES_EATEN.get())) >= 1000) {
                ModAdvancementTriggers.CONSUME_1000_SANDWICHES.get().trigger(player);
            }
        }
    }

    @Override
    public Component getName(ItemStack stack) {
        if (stack.has(DataComponents.CUSTOM_NAME)) {
            return super.getName(stack);
        }
        return SandwichNameHelper.getSandwichDisplayName(stack);
    }
}
