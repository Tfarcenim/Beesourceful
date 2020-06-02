package com.tfar.beesourceful;

import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.FlowingFluid;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class LiquidHoneyBlock extends FlowingFluidBlock {
	protected LiquidHoneyBlock(FlowingFluid fluidIn, Properties builder) {
		super(fluidIn, builder);
	}

	/**
	 * @param supplier    A fluid supplier such as {@link RegistryObject <Fluid>}
	 * @param p_i48368_1_
	 */
	public LiquidHoneyBlock(Supplier<? extends FlowingFluid> supplier, Properties p_i48368_1_) {
		super(supplier, p_i48368_1_);
	}
}
