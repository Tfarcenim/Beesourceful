package tfar.beesourceful.data.provider;

import tfar.beesourceful.Tags;
import net.minecraft.block.Blocks;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;

public class ModBlockTagsProvider extends BlockTagsProvider {
  public ModBlockTagsProvider(DataGenerator p_i48255_1_) {
    super(p_i48255_1_);
  }

  @Override
  protected void registerTags() {
    getBuilder(Tags.NETHER_FLOWERS).add(Blocks.NETHER_WART);
    getBuilder(Tags.END_FLOWERS).add(Blocks.CHORUS_FLOWER);
  }
}
