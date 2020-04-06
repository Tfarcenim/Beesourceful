package com.tfar.beesourceful.util;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.world.gen.placement.IPlacementConfig;

public class ConfigurableCountRangeConfig implements IPlacementConfig {

  final BeeType beeType;

  public ConfigurableCountRangeConfig(BeeType beeType) {
    this.beeType = beeType;
  }

  public <T> Dynamic<T> serialize(DynamicOps<T> ops) {
    return new Dynamic<>(ops, ops.createMap(ImmutableMap.of(ops.createString("bee_type"),ops.createString(beeType.toString()))));
  }

  public static ConfigurableCountRangeConfig deserialize(Dynamic<?> ops) {
    BeeType beeType = BeeType.valueOf(ops.get("bee_type").asString(null));
    return new ConfigurableCountRangeConfig(beeType);
  }
}
