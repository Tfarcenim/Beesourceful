package com.tfar.beesourceful.blockentity;

import com.tfar.beesourceful.BeeSourceful;
import com.tfar.beesourceful.CentrifugeContainer;
import com.tfar.beesourceful.block.CentrifugeBlock;
import com.tfar.beesourceful.inventory.AutomationSensitiveItemStackHandler;
import com.tfar.beesourceful.inventory.HoneyTank;
import com.tfar.beesourceful.recipe.CentrifugeRecipe;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public class CentrifugeBlockEntity extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

	public static final int HONEYCOMB_SLOT = 0;

	public static final int OUTPUT0 = 1;
	public static final int OUTPUT1 = 2;
	public static final int OUTPUT2 = 3;
	public static final int OUTPUT3 = 4;

	public boolean checkValid;
	public boolean processing;

	public AutomationSensitiveItemStackHandler h = new TileStackHandler(5);
	public LazyOptional<IItemHandler> lazyOptional = LazyOptional.of(() -> h);

	public HoneyTank fluidTank = new HoneyTank(10000);
	public LazyOptional<IFluidHandler> fluidOptional = LazyOptional.of(() -> fluidTank);

	public int time = 0;
	public CentrifugeRecipe recipe;
	public ItemStack failedMatch = ItemStack.EMPTY;
	public int totalTime = 0;

	public CentrifugeBlockEntity() {
		super(BeeSourceful.Objectholders.BlockEntities.centrifuge);
	}

	@Override
	public void tick() {
		if (!world.isRemote) {
			if (checkValid) {
				CentrifugeRecipe irecipe = getRecipe();
				processing = irecipe != null && this.canProcess(irecipe);
				world.setBlockState(pos, getBlockState().with(CentrifugeBlock.PROPERTY_ON, processing));
				checkValid = false;
				if (processing)
				totalTime = recipe.time;
			}
			if (processing) {
				time++;
				if (this.time == this.totalTime) {
					this.time = 0;
					CentrifugeRecipe irecipe = getRecipe();
					this.processItem(irecipe);
					this.markDirty();
				}
			} else time = 0;
		}
	}


	protected boolean canProcess(CentrifugeRecipe recipe) {
		List<ItemStack> outputs = recipe.outputs.stream().map(Pair::getLeft).collect(Collectors.toList());
		return outputs.stream().map(stack -> addItemStack(stack, true)).allMatch(ItemStack::isEmpty);
	}

	private void processItem(CentrifugeRecipe recipe) {
		if (this.canProcess(recipe)) {
			ItemStack comb = h.getStackInSlot(HONEYCOMB_SLOT);
			List<ItemStack> outputs = getOutputs(recipe);
			outputs.forEach(stack -> addItemStack(stack, false));
			comb.shrink(1);
			fluidTank.fill(recipe.fluid.copy(), IFluidHandler.FluidAction.EXECUTE);
		}
		time = 0;
	}

	public List<ItemStack> getOutputs(CentrifugeRecipe recipe) {
		return recipe.outputs.stream()
						.filter(pair -> pair.getRight() > Math.random())
						.map(Pair::getLeft).map(ItemStack::copy)
						.collect(Collectors.toList());
	}

	private ItemStack addItemStack(ItemStack stack, boolean simulate) {
		ItemStack rem = stack.copy();
		for (int i = 1; i < 5; i++) {
			rem = addItemStackToSlot(rem, i, simulate);
			if (rem.isEmpty()) return ItemStack.EMPTY;
		}
		return rem;
	}

	private ItemStack addItemStackToSlot(ItemStack toAdd, int slot, boolean simulate) {
		ItemStack existing = h.getStackInSlot(slot);
		//handle empty
		if (existing.isEmpty()) {
			if (!simulate)
				h.setStackInSlot(slot, toAdd.copy());
			return ItemStack.EMPTY;
		}
		//same item
		if (ItemStack.areItemsEqual(existing, toAdd) && ItemStack.areItemStackTagsEqual(existing, toAdd)) {
			int max = existing.getMaxStackSize();
			int total = existing.getCount() + toAdd.getCount();
			if (total <= max) {
				if (!simulate)
					h.setStackInSlot(slot, new ItemStack(toAdd.getItem(), total));
				return ItemStack.EMPTY;
			} else {
				if (!simulate)
					h.setStackInSlot(slot, new ItemStack(existing.getItem(), max));
				return new ItemStack(existing.getItem(), total - max);
			}
		} else
			//cant fit
			return toAdd;
	}

	protected CentrifugeRecipe getRecipe() {
		ItemStack input = h.getStackInSlot(HONEYCOMB_SLOT);
		if (input.isEmpty() || input == failedMatch) return null;
		if (recipe != null && recipe.matches(new RecipeWrapper(h), world)) return recipe;
		else {
			CentrifugeRecipe rec = world.getRecipeManager().
							getRecipe(CentrifugeRecipe.CENTRIFUGE, new RecipeWrapper(h), this.world).orElse(null);
			if (rec == null) failedMatch = input;
			else failedMatch = ItemStack.EMPTY;
			return recipe = rec;
		}
	}

	//from server
	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT nbt = new CompoundNBT();
		nbt.put("fluids",fluidTank.serializeNBT());
		return new SUpdateTileEntityPacket(pos,0,nbt);
	}

	//to client
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		CompoundNBT nbt = pkt.getNbtCompound();
		fluidTank.deserializeNBT(nbt.getCompound("fluids"));
	}

	@Override
	public void markDirty() {
		super.markDirty();
		world.notifyBlockUpdate(pos,getBlockState(),getBlockState(),3);
	}

	@Nonnull
	@Override
	public CompoundNBT write(CompoundNBT tag) {
		CompoundNBT inv = this.h.serializeNBT();
		CompoundNBT fluid = fluidTank.serializeNBT();
		tag.put("inv", inv);
		tag.put("fluid", fluid);
		tag.putInt("time", time);
		tag.putInt("totalTime", totalTime);
		return super.write(tag);
	}

	@Override
	public void read(CompoundNBT tag) {
		CompoundNBT invTag = tag.getCompound("inv");
		h.deserializeNBT(invTag);
		CompoundNBT fluid = tag.getCompound("fluid");
		fluidTank.deserializeNBT(fluid);
		time = tag.getInt("time");
		totalTime = tag.getInt("totalTime");
		super.read(tag);
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? lazyOptional.cast() :
						cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ? fluidOptional.cast() :
										super.getCapability(cap, side);
	}

	public AutomationSensitiveItemStackHandler.IAcceptor getAcceptor() {
		return (slot, stack, automation) -> !automation || slot == 0/*&& StackUtil.isValid(FurnaceRecipes.instance().getSmeltingResult(stack))*/;
	}

	public AutomationSensitiveItemStackHandler.IRemover getRemover() {
		return (slot, automation) -> !automation || slot == 1 || slot == 2 || slot == 3 || slot == 4;
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("block.beesourceful.centrifuge");
	}

	@Nullable
	@Override
	public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
		return new CentrifugeContainer(i, world, pos, playerInventory);
	}

	protected class TileStackHandler extends AutomationSensitiveItemStackHandler {

		protected TileStackHandler(int slots) {
			super(slots);
		}

		@Override
		public AutomationSensitiveItemStackHandler.IAcceptor getAcceptor() {
			return CentrifugeBlockEntity.this.getAcceptor();
		}

		@Override
		public AutomationSensitiveItemStackHandler.IRemover getRemover() {
			return CentrifugeBlockEntity.this.getRemover();
		}

		@Override
		protected void onContentsChanged(int slot) {
			super.onContentsChanged(slot);
			checkValid = true;
			markDirty();
		}
	}
}
