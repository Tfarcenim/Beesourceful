package com.tfar.beesourceful.util;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHolder {
  private final ForgeConfigSpec.BooleanValue enable;
  private final ForgeConfigSpec.IntValue count;
  private final ForgeConfigSpec.IntValue bottomOffset;
  private final ForgeConfigSpec.IntValue topOffset;
  private final ForgeConfigSpec.IntValue maximum;


  public ConfigHolder(ForgeConfigSpec.BooleanValue enable, ForgeConfigSpec.IntValue count, ForgeConfigSpec.IntValue bottomOffset, ForgeConfigSpec.IntValue topOffset, ForgeConfigSpec.IntValue maximum){
    this.enable = enable;
    this.count = count;
    this.bottomOffset = bottomOffset;
    this.topOffset = topOffset;
    this.maximum = maximum;
  }

  public ForgeConfigSpec.BooleanValue getEnable() {
    return enable;
  }

  public ForgeConfigSpec.IntValue getTopOffset() {
    return topOffset;
  }

  public ForgeConfigSpec.IntValue getBottomOffset() {
    return bottomOffset;
  }

  public ForgeConfigSpec.IntValue getMaximum() {
    return maximum;
  }

  public ForgeConfigSpec.IntValue getCount() {
    return count;
  }
}
