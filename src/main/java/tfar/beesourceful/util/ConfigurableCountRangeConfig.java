package tfar.beesourceful.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.placement.IPlacementConfig;

public class ConfigurableCountRangeConfig implements IPlacementConfig {

  final String beeType;

  public ConfigurableCountRangeConfig(String beeType) {
    this.beeType = beeType;
  }

  public static final Codec<ConfigurableCountRangeConfig> CODEC = RecordCodecBuilder.create((countRangeConfigInstance) -> countRangeConfigInstance.group(Codec.STRING.fieldOf("bee_type").forGetter((configurableCountRangeConfig) -> configurableCountRangeConfig.beeType)).apply(countRangeConfigInstance, ConfigurableCountRangeConfig::new));
}
