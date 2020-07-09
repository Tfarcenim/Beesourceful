package tfar.beesourceful;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import tfar.beesourceful.entity.BeeEntityType;
import tfar.beesourceful.entity.BeeProperties;

import java.util.HashMap;
import java.util.Map;

public class ModConfigs {

  public static final ModConfigs SERVER;
  public static final ForgeConfigSpec SERVER_SPEC;

  public static Map<BeeProperties, DataClass> configs = new HashMap<>();

  static {
    final Pair<ModConfigs, ForgeConfigSpec> specPair2 = new ForgeConfigSpec.Builder().configure(ModConfigs::new);
    SERVER_SPEC = specPair2.getRight();
    SERVER = specPair2.getLeft();
  }

  private ModConfigs(ForgeConfigSpec.Builder builder) {
    builder.push("worldgen");
    for (Map.Entry<ResourceLocation,BeeProperties> beeType : BeeProperties.bee_registry.entrySet()) {
      builder.push(beeType.getKey().getPath()+"_bee_nest");
      configs.put(beeType.getValue(),
              new DataClass(
                      builder.define("enabled", true),
                      builder.defineInRange("count", 5,0,Integer.MAX_VALUE),
                      builder.defineInRange("bottom_offset",  0,0,Integer.MAX_VALUE),
                      builder.defineInRange("top_offset",  0,0,Integer.MAX_VALUE),
                      builder.defineInRange("maximum",  64,0,Integer.MAX_VALUE)
                      ));
      builder.pop();
    }
    builder.pop();
  }
}
