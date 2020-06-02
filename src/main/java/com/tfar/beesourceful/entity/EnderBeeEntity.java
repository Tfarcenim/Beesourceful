package com.tfar.beesourceful.entity;

import com.tfar.beesourceful.BeeSourceful;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.PointOfInterestType;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class EnderBeeEntity extends IronBeeEntity {
  public EnderBeeEntity(EntityType<? extends BeeEntity> p_i225714_1_, World p_i225714_2_) {
    super(p_i225714_1_, p_i225714_2_);
  }

  public Block getOre(){
    return Blocks.END_STONE;
  }

  public Block getAllowedHive() {
    return BeeSourceful.Objectholders.ender_bee_nest;
  }

  @Nonnull
  public PointOfInterestType getHivePoi(){
    return BeeSourceful.Objectholders.POI.ender_bee_nest;
  }

  public Item getHoneyComb(){
    return BeeSourceful.Objectholders.ender_honeycomb;
  }

  @Override
  public boolean isFlowers(BlockPos pos) {
    return this.world.isBlockPresent(pos) && this.world.getBlockState(pos).getBlock()== Blocks.CHORUS_FLOWER;
  }

  protected final Predicate<BlockState> chorusPredicate = state -> state.getBlock() == Blocks.CHORUS_FLOWER;

  @Override
  public Predicate<BlockState> getFlowerPredicate() {
    return chorusPredicate;
  }
}
