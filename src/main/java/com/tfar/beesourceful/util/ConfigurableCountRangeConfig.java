package com.tfar.beesourceful.util;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;

public class ConfigurableCountRangeConfig implements IPlacementConfig {

  private final BeeType beeType;
  private final ConfigHolder configHolder;

  public ConfigurableCountRangeConfig(BeeType beeType,ConfigHolder configHolder) {
    this.beeType = beeType;

    this.configHolder = configHolder;
  }

  public <T> Dynamic<T> serialize(DynamicOps<T> ops) {
    return null;
  }

  public static ConfigurableCountRangeConfig deserialize(Dynamic<?> p_214733_0_) {
    return null;
  }

  public ConfigHolder getConfigHolder() {
    return configHolder;
  }
}
