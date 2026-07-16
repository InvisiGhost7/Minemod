package io.github.invisighost7.minemod.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class GlowGlassBlock extends Block {
    public GlowGlassBlock(BlockBehaviour.Properties properties) {
        super(properties.lightLevel((state) -> 15));
    }
}
