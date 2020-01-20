package com.tfar.beesourceful.data.provider;

import com.tfar.beesourceful.BeeSourceful;
import joptsimple.internal.Strings;
import net.minecraft.data.DataGenerator;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraftforge.common.data.LanguageProvider;
import org.codehaus.plexus.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ModLanguageProvider extends LanguageProvider {
  public ModLanguageProvider(DataGenerator gen) {
    super(gen, BeeSourceful.MODID, "en_us");
  }

  @Override
  protected void addTranslations() {
    for (Item item : BeeSourceful.RegistryEvents.items) {
      String key = item.getTranslationKey();

      add(key, getNameFromItem(item));
    }

    for (EntityType<?> entityType : BeeSourceful.RegistryEvents.entities){
      add(entityType.getTranslationKey(),getNameFromEntity(entityType));
    }
  }

  public static String getNameFromItem(Item item){
    return StringUtils.capitaliseAllWords(item.getTranslationKey().split("\\.")[2].replace("_", " "));

  }

  public static String getNameFromEntity(EntityType<?> entityType){
    return StringUtils.capitaliseAllWords(entityType.getTranslationKey().split("\\.")[2].replace("_", " "));
  }
}
