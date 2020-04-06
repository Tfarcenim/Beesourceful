package com.tfar.beesourceful;

import com.tfar.beesourceful.util.BeeType;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.EnumMap;
import java.util.Map;

public class ModConfigs {

  public static final ModConfigs SERVER;
  public static final ForgeConfigSpec SERVER_SPEC;

  public static Map<BeeType, DataClass> configs = new EnumMap<>(BeeType.class);

  static {
    final Pair<ModConfigs, ForgeConfigSpec> specPair2 = new ForgeConfigSpec.Builder().configure(ModConfigs::new);
    SERVER_SPEC = specPair2.getRight();
    SERVER = specPair2.getLeft();
  }

  private ModConfigs(ForgeConfigSpec.Builder builder) {
    builder.push("Worldgen");
    for (BeeType beeType : BeeType.VALUES) {
      configs.put(beeType,
              new DataClass(
                      builder.define("enable_" + beeType + "_bee_nest", true),
                      builder.defineInRange("count_" + beeType + "_bee_nest", 5,0,Integer.MAX_VALUE),
                      builder.defineInRange("bottom_offset_" + beeType + "_bee_nest",  0,0,Integer.MAX_VALUE),
                      builder.defineInRange("top_offset_" + beeType + "_bee_nest",  0,0,Integer.MAX_VALUE),
                      builder.defineInRange("maximum_" + beeType + "_bee_nest",  64,0,Integer.MAX_VALUE)
                      ));
    }
    builder.pop();
  }
  public static class Defaults {

  }
}
