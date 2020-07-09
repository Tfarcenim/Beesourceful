package tfar.beesourceful.data.provider;

import tfar.beesourceful.BeeSourceful;
import net.minecraft.data.DataGenerator;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraftforge.common.data.LanguageProvider;
import org.codehaus.plexus.util.StringUtils;

public class ModLanguageProvider extends LanguageProvider {
  public ModLanguageProvider(DataGenerator gen) {
    super(gen, BeeSourceful.MODID, "en_us");
  }

  @Override
  protected void addTranslations() {
    for (Item item : BeeSourceful.RegistryEvents.items) {
      add(item.getTranslationKey(), getNameFromItem(item));
    }

    for (EntityType<?> entityType : BeeSourceful.entities){
      add(entityType.getTranslationKey(),getNameFromEntity(entityType));
    }
    add("fluid.beesourceful.honey","Honey");
    add("gui.beesourceful.category.centrifuge","Centrifuge");
  }

  public static String getNameFromItem(Item item){
    return StringUtils.capitaliseAllWords(item.getTranslationKey().split("\\.")[2].replace("_", " "));
  }

  public static String getNameFromEntity(EntityType<?> entityType){
    return StringUtils.capitaliseAllWords(entityType.getTranslationKey().split("\\.")[2].replace("_", " "));
  }
}
