package someassemblyrequired.data.providers;

import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.integration.ModCompat;
import someassemblyrequired.registry.ModItems;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@SuppressWarnings("ConstantConditions")
public class ItemModels extends ItemModelProvider {

    public ItemModels(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        super(packOutput, SomeAssemblyRequired.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        Set<Item> items = BuiltInRegistries.ITEM.stream()
                .filter(item -> BuiltInRegistries.ITEM.getKey(item).getNamespace().equals(SomeAssemblyRequired.MOD_ID))
                .collect(Collectors.toSet());

        removeAll(items, ModItems.SANDWICH.get());
        addSandwich();

        removeAll(items, ModItems.SPREAD.get());
        items.removeIf(item -> Ingredients.MODEL_OVERRIDES.contains(BuiltInRegistries.ITEM.wrapAsHolder(item)));
        addSpread();

        removeAll(items, ModItems.ENCHANTED_GOLDEN_APPLE_SLICES.get());
        addGeneratedModel("enchanted_golden_apple_slices", prefixItem("golden_apple_slices"));

        removeAll(items, item -> item instanceof BlockItem).forEach(
                block -> withExistingParent(BuiltInRegistries.ITEM.getKey(block).getPath(), prefixBlock(BuiltInRegistries.ITEM.getKey(block).getPath()))
        );

        items.forEach(this::addGeneratedModel);
    }

    private void addSpread() {
        HashMap<Holder<Item>, ItemModelBuilder> customModels = new HashMap<>();
        addCustomIngredientModels(customModels);

        ItemModelBuilder spreadModel = addGeneratedModel(ModItems.SPREAD.get());
        for (int i = 0; i < Ingredients.MODEL_OVERRIDES.size(); i++) {
            Holder<Item> item = Ingredients.MODEL_OVERRIDES.get(i);
            if (SomeAssemblyRequired.MOD_ID.equals(item.getKey().location().getNamespace())) {
                addGeneratedModel(item.value());
            }

            String path = getIngredientPath(item);

            ModelFile model;
            if (customModels.containsKey(item)) {
                model = customModels.get(item);
            } else if (existingFileHelper.exists(prefixItem(path), PackType.CLIENT_RESOURCES, ".json", "models")) {
                model = new ModelFile.UncheckedModelFile(prefixItem(path));
            } else {
                model = addGeneratedModel(path, prefixItem(path));
            }

            spreadModel.override()
                    .model(model)
                    .predicate(ResourceLocation.parse("custom_model_data"), i + 1)
                    .end();
        }
    }

    private void addCustomIngredientModels(HashMap<Holder<Item>, ItemModelBuilder> customModels) {
        addBeefPattyModels(customModels);

        String path = getIngredientPath(Items.POTATO);
        ResourceLocation texture = prefixItem(path);
        customModels.put(BuiltInRegistries.ITEM.wrapAsHolder(Items.POTATO),
                getBuilder(path)
                        .texture("potato", texture)
                        .element()
                        .from(5, 0, 4)
                        .to(11, 5, 12)
                        .face(Direction.NORTH).uvs(0, 11, 6, 16).end()
                        .face(Direction.EAST).uvs(8, 0, 16, 5).end()
                        .face(Direction.SOUTH).uvs(6, 11, 12, 16).end()
                        .face(Direction.WEST).uvs(0, 0, 8, 5).end()
                        .face(Direction.UP).uvs(0, 5, 8, 11).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).end()
                        .face(Direction.DOWN).uvs(8, 5, 16, 11).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
                        .faces(((direction, builder) -> builder.texture("#potato")))
                        .end()
        );

        path = getIngredientPath(ModItems.BURGER_BUN.get());
        texture = prefixItem(path);
        customModels.put(ModItems.BURGER_BUN,
                getBuilder(path)
                        .texture("burger_bun", texture)
                        .element()
                        .from(2, 0, 2)
                        .to(14, 6, 14)
                        .face(Direction.NORTH).uvs(6, 12, 12, 15).end()
                        .face(Direction.EAST).uvs(0, 12, 6, 15).end()
                        .face(Direction.SOUTH).uvs(6, 6, 12, 9).end()
                        .face(Direction.WEST).uvs(6, 9, 12, 12).end()
                        .face(Direction.UP).uvs(0, 0, 6, 6).end()
                        .face(Direction.DOWN).uvs(0, 6, 6, 12).end()
                        .faces(((direction, builder) -> builder.texture("#burger_bun")))
                        .end()
        );
        path = getIngredientPath(ModItems.BURGER_BUN_BOTTOM.get());
        customModels.put(ModItems.BURGER_BUN_BOTTOM,
                getBuilder(path)
                        .texture("burger_bun", texture)
                        .element()
                        .from(2, 0, 2)
                        .to(14, 2, 14)
                        .face(Direction.NORTH).uvs(6, 14, 12, 15).end()
                        .face(Direction.EAST).uvs(0, 14, 6, 15).end()
                        .face(Direction.SOUTH).uvs(6, 8, 12, 9).end()
                        .face(Direction.WEST).uvs(6, 11, 12, 12).end()
                        .face(Direction.UP).uvs(6, 0, 12, 6).end()
                        .face(Direction.DOWN).uvs(0, 6, 6, 12).end()
                        .faces(((direction, builder) -> builder.texture("#burger_bun")))
                        .end()
        );
        path = getIngredientPath(ModItems.BURGER_BUN_TOP.get());
        customModels.put(ModItems.BURGER_BUN_TOP,
                getBuilder(path)
                        .texture("burger_bun", texture)
                        .element()
                        .from(2, 0, 2)
                        .to(14, 4, 14)
                        .face(Direction.NORTH).uvs(6, 12, 12, 14).end()
                        .face(Direction.EAST).uvs(0, 12, 6, 14).end()
                        .face(Direction.SOUTH).uvs(6, 6, 12, 8).end()
                        .face(Direction.WEST).uvs(6, 9, 12, 11).end()
                        .face(Direction.UP).uvs(0, 0, 6, 6).end()
                        .face(Direction.DOWN).uvs(6, 0, 12, 6).end()
                        .faces(((direction, builder) -> builder.texture("#burger_bun")))
                        .end()
        );
    }

    private void addBeefPattyModels(HashMap<Holder<Item>, ItemModelBuilder> customModels) {
        for (Holder<Item> item : Arrays.asList(
                BuiltInRegistries.ITEM.wrapAsHolder(vectorwing.farmersdelight.common.registry.ModItems.BEEF_PATTY.get()),
                BuiltInRegistries.ITEM.wrapAsHolder(vectorwing.farmersdelight.common.registry.ModItems.MINCED_BEEF.get()),
                Ingredients.reference(ModCompat.MINERSDELIGHT, "vegan_patty")
        )) {
            String path = getIngredientPath(item);
            ResourceLocation texture = prefixItem(path);
            customModels.put(item,
                    getBuilder(path)
                            .texture("beef_patty", texture)
                            .element()
                            .from(2, 0, 2)
                            .to(14, 2, 14)
                            .face(Direction.NORTH).uvs(2, 0, 14, 2).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).end()
                            .face(Direction.EAST).uvs(14, 2, 16, 14).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
                            .face(Direction.SOUTH).uvs(2, 14, 14, 16).rotation(ModelBuilder.FaceRotation.ZERO).end()
                            .face(Direction.WEST).uvs(0, 2, 2, 14).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).end()
                            .face(Direction.UP).uvs(2, 2, 14, 14).end()
                            .face(Direction.DOWN).uvs(2, 2, 14, 14).end()
                            .faces(((direction, builder) -> builder.texture("#beef_patty")))
                            .end()
            );
        }
    }

    private String getIngredientPath(Holder<Item> item) {
        return "ingredient/" + item.getKey().location().toString().replace(':', '/');
    }

    private String getIngredientPath(Item item) {
        return "ingredient/" + getItemPath(item);
    }

    private String getItemPath(Item item) {
        return BuiltInRegistries.ITEM.getKey(item).toString().replace(':', '/');
    }

    private String getItemName(Item item) {
        return BuiltInRegistries.ITEM.getKey(item).getPath();
    }

    private void addSandwich() {
        getBuilder(getItemName(ModItems.SANDWICH.get())).parent(new ModelFile.UncheckedModelFile("builtin/entity"))
                .texture("particle", prefixItem("bread_slice"))
                .transforms()
                .transform(ItemDisplayContext.GUI)
                .rotation(30, 45, 0)
                .scale(0.8F)
                .end()
                .transform(ItemDisplayContext.GROUND)
                .rotation(0, 180, 0)
                .scale(0.5F)
                .end()
                .transform(ItemDisplayContext.HEAD)
                .rotation(0, 180, 0)
                .translation(0, 0.5F, 0)
                .end()
                .transform(ItemDisplayContext.FIXED)
                .rotation(0, 180, 0)
                .translation(0, -4, 0)
                .end()
                .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)
                .rotation(75, 315, 0)
                .translation(0, 2.5F, 0)
                .scale(0.55F)
                .end()
                .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
                .rotation(0, 315, 0)
                .translation(0, 1, 0)
                .scale(0.5F)
                .end()
                .end();
    }

    private ResourceLocation prefixBlock(String path) {
        return SomeAssemblyRequired.id("block/" + path);
    }

    private ResourceLocation prefixItem(String path) {
        return SomeAssemblyRequired.id("item/" + path);
    }

    private ItemModelBuilder addGeneratedModel(Item item) {
        // noinspection ConstantConditions
        String name = BuiltInRegistries.ITEM.getKey(item).getPath();
        return withExistingParent("item/" + name, "item/generated").texture("layer0", prefixItem(name));
    }

    private ItemModelBuilder addGeneratedModel(String name, ResourceLocation texture) {
        return withExistingParent(name, "item/generated").texture("layer0", texture);
    }

    private static void removeAll(Set<Item> set, ItemLike... items) {
        Set<Item> result = Arrays.stream(items).map(ItemLike::asItem).collect(Collectors.toSet());
        set.removeAll(result);
    }

    private static <T> Collection<T> removeAll(Set<T> set, Predicate<T> pred) {
        Set<T> result = set.stream().filter(pred).collect(Collectors.toSet());
        set.removeAll(result);

        if (result.size() <= 0) {
            throw new IllegalArgumentException();
        }

        return result;
    }
}
