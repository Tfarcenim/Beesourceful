package com.tfar.beesourceful.data.provider;

import com.tfar.beesourceful.BeeSourceful;
import com.tfar.beesourceful.Tags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.FluidTagsProvider;
import net.minecraft.data.ItemTagsProvider;

public class ModFluidTagsProvider extends FluidTagsProvider {
  public ModFluidTagsProvider(DataGenerator p_i48255_1_) {
    super(p_i48255_1_);
  }

  @Override
  protected void registerTags() {
    getBuilder(Tags.HONEY).add(BeeSourceful.honey.get());
  }
}
