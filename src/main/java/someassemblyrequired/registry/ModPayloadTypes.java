package someassemblyrequired.registry;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import someassemblyrequired.network.UpdateIngredientsPacket;

public class ModPayloadTypes {

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        registrar.commonToClient(UpdateIngredientsPacket.TYPE, UpdateIngredientsPacket.STREAM_CODEC, UpdateIngredientsPacket::handle);
    }
}
