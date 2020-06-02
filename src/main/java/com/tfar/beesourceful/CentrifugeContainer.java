package com.tfar.beesourceful;

import com.tfar.beesourceful.blockentity.CentrifugeBlockEntity;
import com.tfar.beesourceful.inventory.OutputSlot;
import com.tfar.beesourceful.inventory.SlotItemHandlerUnconditioned;
import com.tfar.beesourceful.util.FunctionalIntReferenceHolder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class CentrifugeContainer extends Container {

  public CentrifugeBlockEntity centrifugeBlockEntity;
  public PlayerEntity player;

  public CentrifugeContainer(int id, World world, BlockPos pos, PlayerInventory inv) {
    super(BeeSourceful.Objectholders.Containers.centrifuge, id);

    this.player = inv.player;

    centrifugeBlockEntity = (CentrifugeBlockEntity) world.getTileEntity(pos);

    this.trackInt(new FunctionalIntReferenceHolder(() -> centrifugeBlockEntity.time, v -> centrifugeBlockEntity.time = v));
    this.trackInt(new FunctionalIntReferenceHolder(() -> centrifugeBlockEntity.totalTime, v -> centrifugeBlockEntity.totalTime = v));

    this.addSlot(new SlotItemHandlerUnconditioned(centrifugeBlockEntity.h, 0, 26, 38));

    this.addSlot(new OutputSlot(centrifugeBlockEntity.h, 1, 116, 29));
    this.addSlot(new OutputSlot(centrifugeBlockEntity.h, 2, 134, 29));
    this.addSlot(new OutputSlot(centrifugeBlockEntity.h, 3, 116, 47));
    this.addSlot(new OutputSlot(centrifugeBlockEntity.h, 4, 134, 47));

    for (int i = 0; i < 3; ++i) {
      for (int j = 0; j < 9; ++j) {
        this.addSlot(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
      }
    }

    for (int k = 0; k < 9; ++k) {
      this.addSlot(new Slot(inv, k, 8 + k * 18, 142));
    }
  }
  /**
   * Determines whether supplied player can use this container
   *
   * @param p_75145_1_
   */
  @Override
  public boolean canInteractWith(PlayerEntity p_75145_1_) {
    return true;
  }

  @Nonnull
  @Override
  public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
    ItemStack itemstack = ItemStack.EMPTY;
    Slot slot = this.inventorySlots.get(index);
    if (slot != null && slot.getHasStack()) {
      ItemStack itemstack1 = slot.getStack();
      itemstack = itemstack1.copy();
      if (index <= 4) {
        if (!this.mergeItemStack(itemstack1, 5, inventorySlots.size(), true)) {
          return ItemStack.EMPTY;
        }
      } else if (!this.mergeItemStack(itemstack1, 0, 2, false)) {
        return ItemStack.EMPTY;
      }

      if (itemstack1.isEmpty()) {
        slot.putStack(ItemStack.EMPTY);
      } else {
        slot.onSlotChanged();
      }
    }
    return itemstack;
  }
}
