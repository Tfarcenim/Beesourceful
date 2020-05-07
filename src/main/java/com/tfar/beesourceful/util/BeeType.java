package com.tfar.beesourceful.util;

import com.tfar.beesourceful.BeeSourceful;
import com.tfar.beesourceful.BeeSourceful.Objectholders;
import com.tfar.beesourceful.feature.OreBeeNestFeature;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.IronBeeEntity;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.ReplaceBlockConfig;

import java.util.function.Predicate;
import java.util.function.Supplier;

public enum BeeType {
  iron(() -> Objectholders.Entities.iron_bee,
          GenerationStage.Decoration.UNDERGROUND_ORES,
          Shortcuts.TRUE,
  Blocks.STONE.getDefaultState(),
          () -> Objectholders.iron_bee_nest,
          new OreBeeNestFeature(ReplaceBlockConfig::deserialize, () -> Objectholders.Entities.iron_bee)),

  gold(() -> Objectholders.Entities.gold_bee,
          GenerationStage.Decoration.UNDERGROUND_ORES,
          Shortcuts.TRUE,Blocks.STONE.getDefaultState(),
          () -> Objectholders.gold_bee_nest,
          new OreBeeNestFeature(ReplaceBlockConfig::deserialize, () -> Objectholders.Entities.gold_bee)),

  redstone(() -> Objectholders.Entities.redstone_bee,
          GenerationStage.Decoration.UNDERGROUND_ORES,
          Shortcuts.TRUE,Blocks.STONE.getDefaultState(),
          () -> Objectholders.redstone_bee_nest,
          new OreBeeNestFeature(ReplaceBlockConfig::deserialize,() -> Objectholders.Entities.redstone_bee)),

  ender(() -> Objectholders.Entities.ender_bee,
          GenerationStage.Decoration.TOP_LAYER_MODIFICATION,
          Shortcuts.TRUE,Blocks.END_STONE.getDefaultState(),
          () -> Objectholders.ender_bee_nest,
          new OreBeeNestFeature(ReplaceBlockConfig::deserialize, () -> Objectholders.Entities.ender_bee)),

  quartz(() -> Objectholders.Entities.quartz_bee,
          GenerationStage.Decoration.UNDERGROUND_ORES,
          biome -> biome.getCategory() == Biome.Category.NETHER,
          Blocks.NETHERRACK.getDefaultState(),
          () -> Objectholders.quartz_bee_nest,
          new OreBeeNestFeature(ReplaceBlockConfig::deserialize, () -> Objectholders.Entities.quartz_bee)),

  lapis(() -> Objectholders.Entities.lapis_bee,
          GenerationStage.Decoration.UNDERGROUND_ORES,
          Shortcuts.TRUE,Blocks.STONE.getDefaultState(),
          () -> Objectholders.lapis_bee_nest,
          new OreBeeNestFeature(ReplaceBlockConfig::deserialize, () -> Objectholders.Entities.lapis_bee)),

  emerald(() -> Objectholders.Entities.emerald_bee,
          GenerationStage.Decoration.UNDERGROUND_ORES,
  biome -> biome.getCategory() == Biome.Category.EXTREME_HILLS,
          Blocks.STONE.getDefaultState(),
          () -> Objectholders.emerald_bee_nest,
          new OreBeeNestFeature(ReplaceBlockConfig::deserialize,() -> Objectholders.Entities.emerald_bee)),

  diamond(() -> Objectholders.Entities.diamond_bee,
          GenerationStage.Decoration.UNDERGROUND_ORES,
          Shortcuts.TRUE,

  Blocks.STONE.getDefaultState(),
          () -> Objectholders.diamond_bee_nest,
          new OreBeeNestFeature(ReplaceBlockConfig::deserialize, () -> Objectholders.Entities.diamond_bee));

  public static final BeeType[] VALUES = values();

  public final Supplier<EntityType<? extends IronBeeEntity>> beeSupplier;
  public final GenerationStage.Decoration generation_stage;
  public final Predicate<Biome> allowed_biomes;
  public final BlockState replace_block;
  public final Supplier<Block> beehive;
  public final Feature<ReplaceBlockConfig> feature;

  BeeType(Supplier<EntityType<? extends IronBeeEntity>> beeSupplier,
          GenerationStage.Decoration generation_stage,
          Predicate<Biome> allowed_biomes,
          BlockState replace_block,
          Supplier<Block> beehive, Feature<ReplaceBlockConfig> feature){
    this.beeSupplier = beeSupplier;
    this.generation_stage = generation_stage;
    this.allowed_biomes = allowed_biomes;
    this.replace_block = replace_block;
    this.beehive = beehive;
    this.feature = feature;
  }
}
