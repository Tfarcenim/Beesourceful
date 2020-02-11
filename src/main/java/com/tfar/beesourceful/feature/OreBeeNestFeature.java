package com.tfar.beesourceful.feature;

import com.mojang.datafixers.Dynamic;
import com.tfar.beesourceful.ModConfigs;
import com.tfar.beesourceful.util.BeeType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.IronBeeEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.IronBeehiveBlockEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.ReplaceBlockConfig;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OreBeeNestFeature extends Feature<ReplaceBlockConfig> {
  public final BeeType beeType;
  public OreBeeNestFeature(Function<Dynamic<?>, ? extends ReplaceBlockConfig> p_i49878_1_, BeeType beeType) {
    super(p_i49878_1_);
    this.beeType = beeType;
  }

  @Override
  public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> p_212245_2_, Random rand, BlockPos pos, ReplaceBlockConfig config) {
    //if (!ModConfigs.enable_hives.get(beeType).get())return false;
    if (world.getBlockState(pos).getBlock() == config.target.getBlock() && hasSpace(world,pos)) {
      world.setBlockState(pos, config.state, 2);
      TileEntity blockEntity = world.getTileEntity(pos);
      if (blockEntity instanceof IronBeehiveBlockEntity){
        IronBeehiveBlockEntity nest = (IronBeehiveBlockEntity)blockEntity;
        for (int i = 0;i < 4;i++) {
          IronBeeEntity ironBeeEntity = beeType.beeSupplier.get().create(world.getWorld());
          CompoundNBT compoundnbt = new CompoundNBT();
          ironBeeEntity.writeUnlessPassenger(compoundnbt);
          IronBeehiveBlockEntity.Bee2 beehivetileentity$bee = new IronBeehiveBlockEntity.Bee2(compoundnbt, 0, 2400);
          nest.bees.add(beehivetileentity$bee);
        }
      }
    }
    return true;
  }

  public boolean hasSpace(IWorld world,BlockPos pos){
    return Arrays.stream(horizontals).anyMatch(direction -> world.getBlockState(pos.offset(direction)).isAir(world, pos));
  }

  protected static final Direction[] horizontals = Arrays.stream(Direction.values())
          .filter(direction -> direction.getAxis().isHorizontal()).collect(Collectors.toList()).toArray(new Direction[0]);

}
