package io.github.invisighost7.minemod;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import io.github.invisighost7.minemod.block.GlowGlassBlock;

import java.util.function.Function;

public class ModBlocks {

    public static <B extends Block> B register(String name, Function<Block.Properties, B> blockFactory, Block.Properties settings) {
        ResourceKey<Block> blockKEY = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Main.MOD_ID, name));

        B block = blockFactory.apply(settings.setId(blockKEY));

        Registry.register(BuiltInRegistries.BLOCK, blockKEY, block);

        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Main.MOD_ID, name));
        BlockItem blockItem = new BlockItem(block, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix());
        Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);

        return block;


    }
    public static final Block GLOW_GLASS = register("glow_glass", GlowGlassBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS));
    public static void initialize() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS)
                .register((creativeTab) -> creativeTab.accept(ModBlocks.GLOW_GLASS));
    }
}
