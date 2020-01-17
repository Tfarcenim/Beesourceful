package com.tfar.beesourceful.data.provider;

import com.tfar.beesourceful.BeeSourceful;
import com.tfar.beesourceful.ItemTagList;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;

public class ModItemTagsProvider extends ItemTagsProvider {
  public ModItemTagsProvider(DataGenerator p_i48255_1_) {
    super(p_i48255_1_);
  }

  @Override
  protected void registerTags() {
    getBuilder(ItemTagList.IRON_HONEYCOMBS).add(BeeSourceful.Objectholders.iron_honeycomb);
    getBuilder(ItemTagList.GOLD_HONEYCOMBS).add(BeeSourceful.Objectholders.gold_honeycomb);
    getBuilder(ItemTagList.REDSTONE_HONEYCOMBS).add(BeeSourceful.Objectholders.redstone_honeycomb);
    getBuilder(ItemTagList.DIAMOND_HONEYCOMBS).add(BeeSourceful.Objectholders.diamond_honeycomb);
    getBuilder(ItemTagList.EMERALD_HONEYCOMBS).add(BeeSourceful.Objectholders.emerald_honeycomb);
    getBuilder(ItemTagList.LAPIS_HONEYCOMBS).add(BeeSourceful.Objectholders.lapis_honeycomb);
    getBuilder(ItemTagList.QUARTZ_HONEYCOMBS).add(BeeSourceful.Objectholders.quartz_honeycomb);
    getBuilder(ItemTagList.ENDER_HONEYCOMBS).add(BeeSourceful.Objectholders.ender_honeycomb);
  }
}
