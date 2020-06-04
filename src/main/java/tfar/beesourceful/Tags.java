package tfar.beesourceful;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags.Wrapper;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class Tags {
  public static final Tag<Block> NETHER_FLOWERS = new BlockTags.Wrapper(new ResourceLocation(BeeSourceful.MODID,"flowers/nether"));
  public static final Tag<Block> END_FLOWERS = new BlockTags.Wrapper(new ResourceLocation(BeeSourceful.MODID,"flowers/end"));

  public static final Tag<Item> IRON_HONEYCOMBS = new Wrapper(new ResourceLocation("forge","honeycombs/iron"));
  public static final Tag<Item> GOLD_HONEYCOMBS = new Wrapper(new ResourceLocation("forge","honeycombs/gold"));
  public static final Tag<Item> REDSTONE_HONEYCOMBS = new Wrapper(new ResourceLocation("forge","honeycombs/redstone"));
  public static final Tag<Item> DIAMOND_HONEYCOMBS = new Wrapper(new ResourceLocation("forge","honeycombs/diamond"));
  public static final Tag<Item> EMERALD_HONEYCOMBS = new Wrapper(new ResourceLocation("forge","honeycombs/emerald"));
  public static final Tag<Item> LAPIS_HONEYCOMBS = new Wrapper(new ResourceLocation("forge","honeycombs/lapis"));
  public static final Tag<Item> QUARTZ_HONEYCOMBS = new Wrapper(new ResourceLocation("forge","honeycombs/quartz"));
  public static final Tag<Item> ENDER_HONEYCOMBS = new Wrapper(new ResourceLocation("forge","honeycombs/ender"));

  public static final Tag<Fluid> HONEY = new FluidTags.Wrapper(new ResourceLocation("forge","honey"));
}
