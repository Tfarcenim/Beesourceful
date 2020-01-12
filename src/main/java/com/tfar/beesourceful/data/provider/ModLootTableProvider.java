package com.tfar.beesourceful.data.provider;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModLootTableProvider extends LootTableProvider {
  public ModLootTableProvider(DataGenerator p_i50789_1_) {
    super(p_i50789_1_);
  }

  @Override
  protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
    return ImmutableList.of(Pair.of(ModBlockLootTables::new, LootParameterSets.BLOCK));
  }

  @Override
  protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationresults) {
    map.forEach((name, table) -> LootTableManager.check(validationresults, name, table));
  }
}
