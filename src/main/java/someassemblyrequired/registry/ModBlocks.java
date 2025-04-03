package someassemblyrequired.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import someassemblyrequired.SomeAssemblyRequired;
import someassemblyrequired.block.SandwichBlock;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, SomeAssemblyRequired.MOD_ID);

    public static final DeferredHolder<Block, Block> SANDWICH = BLOCKS.register(
            "sandwich",
            () -> new SandwichBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.WOOD)
                            .strength(0.15F)
                            .noCollission()
                            .isRedstoneConductor((state, world, pos) -> false)
                            .sound(ModSoundTypes.SANDWICH_SOUND_TYPE)
                            .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredHolder<Block, Block> SANDWICHING_STATION = BLOCKS.register(
            "sandwiching_station",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .instrument(NoteBlockInstrument.BASS)
                            .mapColor(MapColor.STONE)
                            .strength(2.5F)
                            .sound(SoundType.WOOD)
            )
    );
}
