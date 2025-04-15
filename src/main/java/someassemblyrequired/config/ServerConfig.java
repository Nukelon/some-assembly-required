package someassemblyrequired.config;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.integration.ModCompat;

import java.util.ArrayList;
import java.util.List;

public class ServerConfig {

    public final ForgeConfigSpec.IntValue maximumSandwichHeight;

    public final ForgeConfigSpec.BooleanValue generateChestLoot;

    public final ForgeConfigSpec.ConfigValue<String> sandwichBonusEffect;
    public final ForgeConfigSpec.ConfigValue<String> burgerBonusEffect;

    public final ForgeConfigSpec.ConfigValue<List<Integer>> sandwichEffectDurations;
    public final ForgeConfigSpec.ConfigValue<List<Integer>> burgerEffectDurations;

    ServerConfig(ForgeConfigSpec.Builder builder) {
        maximumSandwichHeight = builder
                .comment("The maximum amount of items a sandwich can contain")
                .translation(translate("maximum_sandwich_height"))
                .defineInRange("maximum_sandwich_height", 32, 2, Integer.MAX_VALUE);

        generateChestLoot = builder
                .comment("Whether randomly generated sandwiches should be added to chests in villages and some other locations")
                .translation(translate("generate_chest_loot"))
                .define("generate_chest_loot", true);

        sandwichBonusEffect = builder
                .comment("The effect applied by sandwiches, depending on the number of unique ingredients")
                .translation(translate("sandwich_bonus_effect"))
                .define("sandwich_effect", new ResourceLocation(ModCompat.FARMERSDELIGHT, "comfort").toString());
        burgerBonusEffect = builder
                .comment("The effect applied by burgers, depending on the number of unique ingredients")
                .translation(translate("burger_bonus_effect"))
                .define("burger_effect", new ResourceLocation(ModCompat.FARMERSDELIGHT, "nourishment").toString());

        sandwichEffectDurations = builder
                .comment("The durations of the effect applied by sandwiches in seconds, depending on the number of unique ingredients")
                .translation(translate("sandwich_bonus_effect_durations"))
                .define("sandwich_effect_durations", new ArrayList<>(List.of(0, 0, 60, 120, 180, 240, 300)));
        burgerEffectDurations = builder
                .comment("The durations of the effect applied by burgers in seconds, depending on the number of unique ingredients")
                .translation(translate("burger_bonus_effect_durations"))
                .define("burger_effect_durations", new ArrayList<>(List.of(0, 0, 60, 120, 180, 240, 300)));
    }

    private String translate(String name) {
        return "%s.config.server.%s".formatted(SomeAssemblyRequired.MOD_ID, name);
    }
}
