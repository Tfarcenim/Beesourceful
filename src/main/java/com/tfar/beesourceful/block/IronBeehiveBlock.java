package com.tfar.beesourceful.block;

import com.tfar.beesourceful.IronBeehiveBlockEntity;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.BeehiveTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class IronBeehiveBlock extends BeehiveBlock {

  public IronBeehiveBlock(Properties p_i225756_1_) {
    super(p_i225756_1_);
  }

  @Nullable
  @Override
  public TileEntity createNewTileEntity(IBlockReader p_196283_1_) {
    return null;
  }

  public ActionResultType onUse(BlockState state, World p_225533_2_, BlockPos p_225533_3_, PlayerEntity player, Hand p_225533_5_, BlockRayTraceResult p_225533_6_) {
    ItemStack itemstack = player.getHeldItem(p_225533_5_);
    ItemStack itemstack1 = itemstack.copy();
    int i = state.get(HONEY_LEVEL);
    boolean flag = false;
    if (i >= 5) {
      if (itemstack.getItem() == Items.SHEARS) {
        p_225533_2_.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.field_226133_ah_, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        dropResourceHoneycomb(p_225533_2_, p_225533_3_);
        itemstack.damageItem(1, player, p_226874_1_ -> p_226874_1_.sendBreakAnimation(p_225533_5_));
        flag = true;
      } else if (itemstack.getItem() == Items.GLASS_BOTTLE) {
        itemstack.shrink(1);
        p_225533_2_.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        if (itemstack.isEmpty()) {
          player.setHeldItem(p_225533_5_, new ItemStack(Items.field_226638_pX_));
        } else if (!player.inventory.addItemStackToInventory(new ItemStack(Items.field_226638_pX_))) {
          player.dropItem(new ItemStack(Items.field_226638_pX_), false);
        }

        flag = true;
      }
    }

    if (flag) {
      if (!CampfireBlock.isLitCampfireInRange(p_225533_2_, p_225533_3_, 5)) {
        if (this.hasBees(p_225533_2_, p_225533_3_)) {
          this.angerNearbyBees(p_225533_2_, p_225533_3_);
        }

        this.takeHoney(p_225533_2_, state, p_225533_3_, player, BeehiveTileEntity.State.EMERGENCY);
      } else {
        this.takeHoney(p_225533_2_, state, p_225533_3_);
        if (player instanceof ServerPlayerEntity) {
          CriteriaTriggers.SAFELY_HARVEST_HONEY.test((ServerPlayerEntity) player, p_225533_3_, itemstack1);
        }
      }

      return ActionResultType.SUCCESS;
    } else {
      return ActionResultType.PASS;
    }
  }

  private void angerNearbyBees(World p_226881_1_, BlockPos p_226881_2_) {
    List<BeeEntity> lvt_3_1_ = p_226881_1_.getEntitiesWithinAABB(BeeEntity.class, (new AxisAlignedBB(p_226881_2_)).grow(8.0D, 6.0D, 8.0D));
    if (!lvt_3_1_.isEmpty()) {
      List<PlayerEntity> lvt_4_1_ = p_226881_1_.getEntitiesWithinAABB(PlayerEntity.class, (new AxisAlignedBB(p_226881_2_)).grow(8.0D, 6.0D, 8.0D));
      int lvt_5_1_ = lvt_4_1_.size();

      for (BeeEntity lvt_7_1_ : lvt_3_1_) {
        if (lvt_7_1_.getAttackTarget() == null) {
          lvt_7_1_.setBeeAttacker(lvt_4_1_.get(p_226881_1_.rand.nextInt(lvt_5_1_)));
        }
      }
    }
  }

  private boolean hasBees(World p_226882_1_, BlockPos p_226882_2_) {
    TileEntity lvt_3_1_ = p_226882_1_.getTileEntity(p_226882_2_);
    if (lvt_3_1_ instanceof BeehiveTileEntity) {
      BeehiveTileEntity lvt_4_1_ = (BeehiveTileEntity) lvt_3_1_;
      return !lvt_4_1_.hasNoBees();
    } else {
      return false;
    }
  }

  public static void dropResourceHoneyComb(IronBeehiveBlock block, World world, BlockPos pos) {
    block.dropResourceHoneycomb(world, pos);
  }

  public void dropResourceHoneycomb(World p_226878_0_, BlockPos p_226878_1_) {
    TileEntity blockEntity = p_226878_0_.getTileEntity(p_226878_1_);
    if (blockEntity instanceof IronBeehiveBlockEntity) {
      for (int i = 0; i < 3; ++i) {
        spawnAsEntity(p_226878_0_, p_226878_1_, new ItemStack(((IronBeehiveBlockEntity) blockEntity).getResourceHoneyComb()));
      }
    }
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new IronBeehiveBlockEntity();
  }
}
