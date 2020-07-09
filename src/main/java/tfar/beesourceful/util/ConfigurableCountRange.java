package tfar.beesourceful.util;

import com.mojang.serialization.Codec;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.placement.SimplePlacement;
import tfar.beesourceful.DataClass;
import tfar.beesourceful.ModConfigs;
import tfar.beesourceful.entity.BeeProperties;

import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ConfigurableCountRange extends SimplePlacement<ConfigurableCountRangeConfig> {
	public ConfigurableCountRange(Codec<ConfigurableCountRangeConfig> p_i232100_1_) {
		super(p_i232100_1_);
	}

	@Override
	protected Stream<BlockPos> getPositions(Random rand, ConfigurableCountRangeConfig config, BlockPos pos) {
		BeeProperties beeType = BeeProperties.bee_registry.get(new ResourceLocation(config.beeType));
		DataClass configs = ModConfigs.configs.get(beeType);
		return configs.enabled.get() ? IntStream.range(0, configs.count.get()).mapToObj(a -> {
			int i = rand.nextInt(16) + pos.getX();
			int j = rand.nextInt(16) + pos.getZ();
			int k = rand.nextInt(configs.maximum.get() -
							configs.topOffset.get()) + configs.bottomOffset.get();
			return new BlockPos(i, k, j);
		}) : Stream.empty();
	}
}
