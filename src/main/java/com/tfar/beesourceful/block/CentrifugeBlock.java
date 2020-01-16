package com.tfar.beesourceful.block;

import com.tfar.beesourceful.CentrifugeBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class CentrifugeBlock extends Block {
  public CentrifugeBlock(Properties p_i48440_1_) {
    super(p_i48440_1_);
  }

  @Override
  public ActionResultType onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand p_225533_5_, BlockRayTraceResult p_225533_6_) {
    if (!world.isRemote) {
      INamedContainerProvider blockEntity = state.getContainer(world,pos);
      if (blockEntity != null) {
        NetworkHooks.openGui((ServerPlayerEntity) player, blockEntity, pos);
        player.addStat(Stats.OPEN_BARREL);
      }
    }
    return ActionResultType.SUCCESS;
  }

  @Nullable
  @Override
  public INamedContainerProvider getContainer(BlockState state, World worldIn, BlockPos pos) {
    return (INamedContainerProvider)worldIn.getTileEntity(pos);
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new CentrifugeBlockEntity();
  }
}
