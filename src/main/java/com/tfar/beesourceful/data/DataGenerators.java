package com.tfar.beesourceful.data;

import com.tfar.beesourceful.data.provider.*;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber (bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
  @SubscribeEvent
  public static void gatherData(GatherDataEvent e){
    DataGenerator gen = e.getGenerator();
    ExistingFileHelper helper = e.getExistingFileHelper();

    if (e.includeClient()){
      gen.addProvider(new ModBlockstateProvider(gen,helper));
      gen.addProvider(new ModItemModelProvider(gen,helper));
      gen.addProvider(new ModLanguageProvider(gen));
    }
    if (e.includeServer()){
      gen.addProvider(new ModLootTableProvider(gen));
      gen.addProvider(new ModRecipeProvider(gen));
      gen.addProvider(new ModItemTagsProvider(gen));
      gen.addProvider(new ModFluidTagsProvider(gen));
    }
  }
}
