package com.tfar.beesourceful.util;

import com.mojang.datafixers.Dynamic;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.placement.SimplePlacement;

import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ConfigurableCountRange extends SimplePlacement<ConfigurableCountRangeConfig> {
  public ConfigurableCountRange(Function<Dynamic<?>, ? extends ConfigurableCountRangeConfig> p_i51362_1_) {
    super(p_i51362_1_);
  }

  @Override
  protected Stream<BlockPos> getPositions(Random rand, ConfigurableCountRangeConfig config, BlockPos pos) {
    return IntStream.range(0, config.getConfigHolder().getCount().get()).mapToObj((i1) -> {
      int i = rand.nextInt(16) + pos.getX();
      int j = rand.nextInt(16) + pos.getZ();
      int k = rand.nextInt(config.getConfigHolder().getCount().get() -
              config.getConfigHolder().getCount().get()) + config.getConfigHolder().getMaximum().get();
      return new BlockPos(i, k, j);
    });  }
}
