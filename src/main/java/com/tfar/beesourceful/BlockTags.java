package com.tfar.beesourceful;

import net.minecraft.block.Block;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

import static com.tfar.beesourceful.BeeSourceful.MODID;

public class BlockTags {
  public static final Tag<Block> IRON_BEEHIVES = new net.minecraft.tags.BlockTags.Wrapper(new ResourceLocation(MODID, "iron_heehives"));

}
