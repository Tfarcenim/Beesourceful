package tfar.beesourceful.data.provider;

import tfar.beesourceful.BeeSourceful;
import tfar.beesourceful.Tags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.FluidTagsProvider;

public class ModFluidTagsProvider extends FluidTagsProvider {
  public ModFluidTagsProvider(DataGenerator p_i48255_1_) {
    super(p_i48255_1_);
  }

  @Override
  protected void registerTags() {
    getBuilder(Tags.HONEY).add(BeeSourceful.honey.get());
  }
}
