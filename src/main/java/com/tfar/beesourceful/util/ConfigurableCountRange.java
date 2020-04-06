package com.tfar.beesourceful.util;

import com.mojang.datafixers.Dynamic;
import com.tfar.beesourceful.DataClass;
import com.tfar.beesourceful.ModConfigs;
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
		BeeType beeType = config.beeType;
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
