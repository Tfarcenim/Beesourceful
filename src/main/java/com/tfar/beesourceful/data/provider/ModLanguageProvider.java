package com.tfar.beesourceful.data.provider;

import com.tfar.beesourceful.BeeSourceful;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraftforge.common.data.LanguageProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

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

      String[] parts2 = name2.split(" ");

      List<String> newStrings = Arrays.stream(parts2).map(s -> s.substring(0, 1).toUpperCase(Locale.ROOT) + s.substring(1)).collect(Collectors.toList());

      StringBuilder s1 = new StringBuilder();
      for (String s : newStrings) {
        s1.append(s);
      }

      add(item.getTranslationKey(), s1.toString());
    }
  }
}
