package tfar.beesourceful;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tags.*;
import net.minecraft.util.ResourceLocation;

public class Tags {
  public static final ITag.INamedTag<Block> NETHER_FLOWERS = BlockTags.makeWrapperTag(new ResourceLocation(BeeSourceful.MODID,"flowers/nether").toString());
  public static final ITag.INamedTag<Block> END_FLOWERS = BlockTags.makeWrapperTag(new ResourceLocation(BeeSourceful.MODID,"flowers/end").toString());

  public static final ITag.INamedTag<Item> IRON_HONEYCOMBS = ItemTags.makeWrapperTag(new ResourceLocation("forge", "honeycombs/iron").toString());
  public static final ITag.INamedTag<Item> GOLD_HONEYCOMBS = ItemTags.makeWrapperTag(new ResourceLocation("forge","honeycombs/gold").toString());
  public static final ITag.INamedTag<Item> REDSTONE_HONEYCOMBS = ItemTags.makeWrapperTag(new ResourceLocation("forge","honeycombs/redstone").toString());
  public static final ITag.INamedTag<Item> DIAMOND_HONEYCOMBS = ItemTags.makeWrapperTag(new ResourceLocation("forge","honeycombs/diamond").toString());
  public static final ITag.INamedTag<Item> EMERALD_HONEYCOMBS = ItemTags.makeWrapperTag(new ResourceLocation("forge","honeycombs/emerald").toString());
  public static final ITag.INamedTag<Item> LAPIS_HONEYCOMBS = ItemTags.makeWrapperTag(new ResourceLocation("forge","honeycombs/lapis").toString());
  public static final ITag.INamedTag<Item> QUARTZ_HONEYCOMBS = ItemTags.makeWrapperTag(new ResourceLocation("forge","honeycombs/quartz").toString());
  public static final ITag.INamedTag<Item> ENDER_HONEYCOMBS = ItemTags.makeWrapperTag(new ResourceLocation("forge","honeycombs/ender").toString());

  public static final ITag.INamedTag<Fluid> HONEY = FluidTags.makeWrapperTag(new ResourceLocation("forge","honey").toString());
}
