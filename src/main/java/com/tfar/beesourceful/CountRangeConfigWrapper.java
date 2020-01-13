package com.tfar.beesourceful;

import net.minecraft.world.gen.placement.CountRangeConfig;

public class CountRangeConfigWrapper extends CountRangeConfig {
  public CountRangeConfigWrapper(int count, int offsetFromBottom, int margin) {
    super(count, offsetFromBottom, 0, margin);
  }
}
