package com.tfar.beesourceful;

import net.minecraftforge.common.ForgeConfigSpec;

public class DataClass {

	public final ForgeConfigSpec.BooleanValue enabled;
	public final ForgeConfigSpec.IntValue count;
	public final ForgeConfigSpec.IntValue bottomOffset;
	public final ForgeConfigSpec.IntValue topOffset;
	public final ForgeConfigSpec.IntValue maximum;

	public DataClass(ForgeConfigSpec.BooleanValue enabled, ForgeConfigSpec.IntValue count, ForgeConfigSpec.IntValue bottomOffset, ForgeConfigSpec.IntValue topOffset, ForgeConfigSpec.IntValue maximum) {
		this.enabled = enabled;
		this.count = count;
		this.bottomOffset = bottomOffset;
		this.topOffset = topOffset;
		this.maximum = maximum;
	}
}
