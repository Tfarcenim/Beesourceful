package com.tfar.beesourceful.data.provider;

import com.tfar.beesourceful.BeeSourceful;
import com.tfar.beesourceful.block.IronBeehiveBlock;
import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;

import javax.annotation.Nonnull;

public class ModBlockLootTables extends BlockLootTables {
  @Override
  protected void addTables() {
    BeeSourceful.RegistryEvents.blocks.stream()
            .filter(block -> !(block instanceof IronBeehiveBlock))
            .forEach(this::registerDropSelfLootTable);
    BeeSourceful.RegistryEvents.blocks.stream()
            .filter(IronBeehiveBlock.class::isInstance)
            .filter(block -> block != BeeSourceful.Objectholders.iron_beehive)
            .forEach(block -> registerLootTable(block,BlockLootTables::createForBeeNest));
    registerLootTable(BeeSourceful.Objectholders.iron_beehive,BlockLootTables::createForBeehive);
  }

  @Nonnull
  @Override
  protected Iterable<Block> getKnownBlocks() {
    return BeeSourceful.RegistryEvents.blocks.stream()::iterator;
  }
}
