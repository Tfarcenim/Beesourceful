package com.tfar.beesourceful.util;

import com.tfar.beesourceful.BeeSourceful;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.IronBeeEntity;

import java.util.function.Supplier;

public enum BeeType {
  iron(() -> BeeSourceful.Objectholders.Entities.iron_bee),
  gold(() -> BeeSourceful.Objectholders.Entities.gold_bee),
  redstone(() -> BeeSourceful.Objectholders.Entities.redstone_bee),
  ender(() -> BeeSourceful.Objectholders.Entities.ender_bee),
  quartz(() -> BeeSourceful.Objectholders.Entities.quartz_bee),
  lapis(() -> BeeSourceful.Objectholders.Entities.lapis_bee),
  emerald(() -> BeeSourceful.Objectholders.Entities.emerald_bee),
  diamond(() -> BeeSourceful.Objectholders.Entities.diamond_bee);
  public static final BeeType[] VALUES = values();
  public final Supplier<EntityType<? extends IronBeeEntity>> beeSupplier;
  BeeType(Supplier<EntityType<? extends IronBeeEntity>> beeSupplier){
    this.beeSupplier = beeSupplier;
  }
}
