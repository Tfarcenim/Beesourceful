package tfar.beesourceful.inventory;

import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fluids.FluidStack;

public class ImmutableFluidStack extends FluidStack {
	public ImmutableFluidStack(Fluid fluid, int amount) {
		super(fluid, amount);
	}

	public ImmutableFluidStack(Fluid fluid, int amount, CompoundNBT nbt) {
		super(fluid, amount, nbt);
	}

	public ImmutableFluidStack(FluidStack stack, int amount) {
		super(stack, amount);
	}

	@Override
	public void setAmount(int amount) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void grow(int amount) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void shrink(int amount) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setTag(CompoundNBT tag) {
		throw new UnsupportedOperationException();
	}

	@Override
	public CompoundNBT getOrCreateTag() {
		throw new UnsupportedOperationException();
	}

	@Override
	public CompoundNBT getOrCreateChildTag(String childName) {
		throw new UnsupportedOperationException();
	}
}
