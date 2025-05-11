package someassemblyrequired.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.registry.ModIngredients;

public final class UpdateIngredientsPacket implements CustomPacketPayload {

    public static final UpdateIngredientsPacket INSTANCE = new UpdateIngredientsPacket();

    public static final StreamCodec<ByteBuf, UpdateIngredientsPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    public static final CustomPacketPayload.Type<UpdateIngredientsPacket> TYPE = new CustomPacketPayload.Type<>(SomeAssemblyRequired.id("update_ingredients"));

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().level != null) {
                Minecraft.getInstance().level.registryAccess().lookup(ModIngredients.INGREDIENTS).ifPresent(ModIngredients::refresh);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
