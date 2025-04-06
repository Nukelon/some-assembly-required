package someassemblyrequired.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import someassemblyrequired.SomeAssemblyRequired;

public class ClientConfig {

    public final ModConfigSpec.BooleanValue listItemsInTooltip;

    ClientConfig(ModConfigSpec.Builder builder) {
        listItemsInTooltip = builder
                .comment("Whether a sandwich's ingredients should be listed in the tooltip")
                .translation(translate("list_ingredients_in_tooltip"))
                .define("list_ingredients_in_tooltip", false);
    }

    private String translate(String name) {
        return "%s.config.client.%s".formatted(SomeAssemblyRequired.MOD_ID, name);
    }
}
