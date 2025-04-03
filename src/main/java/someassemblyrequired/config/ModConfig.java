package someassemblyrequired.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ModConfig {

    public final static ServerConfig server;
    public final static ModConfigSpec serverSpec;

    static {
        Pair<ServerConfig, ModConfigSpec> serverSpecPair = new ModConfigSpec.Builder().configure(ServerConfig::new);
        server = serverSpecPair.getLeft();
        serverSpec = serverSpecPair.getRight();
    }
}
