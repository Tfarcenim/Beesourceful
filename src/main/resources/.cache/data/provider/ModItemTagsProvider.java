package tfar.beesourceful.data.provider;

import tfar.beesourceful.BeeSourceful;
import tfar.beesourceful.Tags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;

public class ModItemTagsProvider extends ItemTagsProvider {
  public ModItemTagsProvider(DataGenerator p_i48255_1_) {
    super(p_i48255_1_);
  }

  @Override
  protected void registerTags() {
    getBuilder(Tags.IRON_HONEYCOMBS).add(BeeSourceful.Objectholders.iron_honeycomb);
    getBuilder(Tags.GOLD_HONEYCOMBS).add(BeeSourceful.Objectholders.gold_honeycomb);
    getBuilder(Tags.REDSTONE_HONEYCOMBS).add(BeeSourceful.Objectholders.redstone_honeycomb);
    getBuilder(Tags.DIAMOND_HONEYCOMBS).add(BeeSourceful.Objectholders.diamond_honeycomb);
    getBuilder(Tags.EMERALD_HONEYCOMBS).add(BeeSourceful.Objectholders.emerald_honeycomb);
    getBuilder(Tags.LAPIS_HONEYCOMBS).add(BeeSourceful.Objectholders.lapis_honeycomb);
    getBuilder(Tags.QUARTZ_HONEYCOMBS).add(BeeSourceful.Objectholders.quartz_honeycomb);
    getBuilder(Tags.ENDER_HONEYCOMBS).add(BeeSourceful.Objectholders.ender_honeycomb);
  }
}
