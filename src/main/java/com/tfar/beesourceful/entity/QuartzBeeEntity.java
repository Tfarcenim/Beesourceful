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
import net.minecraftforge.common.Tags;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class QuartzBeeEntity extends IronBeeEntity {
  public QuartzBeeEntity(EntityType<? extends BeeEntity> p_i225714_1_, World p_i225714_2_) {
    super(p_i225714_1_, p_i225714_2_);
  }

  public Block getOre(){
    return Blocks.NETHER_QUARTZ_ORE;
  }

  @Override
  public boolean validFillerBlock(Block block) {
    return block.isIn(Tags.Blocks.NETHERRACK);
  }

  public Block getAllowedHive() {
    return BeeSourceful.Objectholders.quartz_bee_nest;
  }

  @Nonnull
  public PointOfInterestType getHivePoi(){
    return BeeSourceful.Objectholders.POI.quartz_bee_nest;
  }

  public Item getHoneyComb(){
    return BeeSourceful.Objectholders.quartz_honeycomb;
  }

  @Override
  public boolean isFlowers(BlockPos pos) {
    return this.world.isBlockPresent(pos) && this.world.getBlockState(pos).getBlock()== Blocks.NETHER_WART;
  }

  protected final Predicate<BlockState> wartPredicate = state -> state.getBlock() == Blocks.NETHER_WART;

  @Override
  public Predicate<BlockState> getFlowerPredicate() {
    return wartPredicate;
  }

}
