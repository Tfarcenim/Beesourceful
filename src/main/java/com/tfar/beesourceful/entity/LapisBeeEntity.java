package com.tfar.beesourceful.entity;

import com.tfar.beesourceful.BeeSourceful;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.item.Item;
import net.minecraft.village.PointOfInterestType;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class LapisBeeEntity extends IronBeeEntity {
  public LapisBeeEntity(EntityType<? extends BeeEntity> p_i225714_1_, World p_i225714_2_) {
    super(p_i225714_1_, p_i225714_2_);
  }

  public Block getOre(){
    return Blocks.LAPIS_ORE;
  }

  public Block getAllowedHive() {
    return BeeSourceful.Objectholders.lapis_bee_nest;
  }

  @Nonnull
  public PointOfInterestType getHivePoi(){
    return BeeSourceful.Objectholders.POI.lapis_bee_nest;
  }

  public Item getHoneyComb(){
    return BeeSourceful.Objectholders.lapis_honeycomb;
  }
}
