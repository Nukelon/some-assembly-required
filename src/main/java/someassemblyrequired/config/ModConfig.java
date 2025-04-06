package someassemblyrequired.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ModConfig {

    public static final ServerConfig server;
    public static final ModConfigSpec serverSpec;

    public static final ClientConfig client;
    public static final ModConfigSpec clientSpec;

    static {
        Pair<ServerConfig, ModConfigSpec> serverSpecPair = new ModConfigSpec.Builder().configure(ServerConfig::new);
        server = serverSpecPair.getLeft();
        serverSpec = serverSpecPair.getRight();
        Pair<ClientConfig, ModConfigSpec> clientSpecPair = new ModConfigSpec.Builder().configure(ClientConfig::new);
        client = clientSpecPair.getLeft();
        clientSpec = clientSpecPair.getRight();
    }
}
