package someassemblyrequired;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import someassemblyrequired.block.SandwichBlockRenderer;
import someassemblyrequired.item.sandwich.SandwichItemRenderer;
import someassemblyrequired.registry.ModBlockEntityTypes;
import someassemblyrequired.registry.ModDataComponents;
import someassemblyrequired.registry.ModItems;

public class SomeAssemblyRequiredClient {

    public SomeAssemblyRequiredClient(IEventBus modEventBus) {
        modEventBus.addListener(this::onClientSetup);
        modEventBus.addListener(this::onRegisterColorHandlers);
        modEventBus.addListener(this::onRegisterClientExtensions);
    }

    public void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> BlockEntityRenderers.register(ModBlockEntityTypes.SANDWICH.get(), SandwichBlockRenderer::new));
    }

    public void onRegisterColorHandlers(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> {
            int color = stack.getOrDefault(ModDataComponents.SPREAD_COLOR, 0xFFFFFFFF);
            return tintIndex == 0 ? color : 0xFFFFFFFF;
        }, ModItems.SPREAD.get());
    }

    public void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new IClientItemExtensions() {

            private final BlockEntityWithoutLevelRenderer renderer = new SandwichItemRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }

        }, ModItems.SANDWICH.get());
    }
}
