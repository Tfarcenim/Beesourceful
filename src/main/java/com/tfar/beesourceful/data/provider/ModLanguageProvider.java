package com.tfar.beesourceful.data.provider;

import com.tfar.beesourceful.BeeSourceful;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraftforge.common.data.LanguageProvider;

import java.util.Locale;

public class ModLanguageProvider extends LanguageProvider {
  public ModLanguageProvider(DataGenerator gen) {
    super(gen, BeeSourceful.MODID, "en_us");
  }

  @Override
  protected void addTranslations() {
    for (Item item : BeeSourceful.RegistryEvents.items) {
      String key = item.getTranslationKey();
      String[] parts = key.split("\\.");
      String name = parts[2];
      String name2 = name.replace("_"," ");
      String name3 = name2.substring(0,1).toUpperCase(Locale.ROOT)+name2.substring(1);
      add(item.getTranslationKey(), name3);
    }
  }
}
