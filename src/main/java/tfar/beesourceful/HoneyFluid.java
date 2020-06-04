package tfar.beesourceful;

import net.minecraft.world.IWorldReader;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public abstract class HoneyFluid extends ForgeFlowingFluid {
	protected HoneyFluid(Properties properties) {
		super(properties);
	}

	public static class Source extends ForgeFlowingFluid.Source {
		public Source(Properties properties) {
			super(properties);
		}

		@Override
		public int getTickRate(IWorldReader world) {
			return world.getDimension().isNether() ? 20 : 60;
		}

	}

	public static class Flowing extends ForgeFlowingFluid.Flowing {
		public Flowing(Properties properties) {
			super(properties);
		}

		@Override
		public int getTickRate(IWorldReader world) {
			return world.getDimension().isNether() ? 20 : 60;
		}

	}
}
