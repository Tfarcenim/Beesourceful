package com.tfar.beesourceful.data.provider;

import com.tfar.beesourceful.BeeSourceful;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.BlockItem;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;

public class ModItemModelProvider extends ItemModelProvider {
  public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
    super(generator, BeeSourceful.MODID, existingFileHelper);
  }

  @Override
  protected void registerModels() {
    BeeSourceful.RegistryEvents.items.forEach(item -> {
      String path = item.getRegistryName().getPath();
      if (item instanceof BlockItem)
      getBuilder(path).parent(new ModelFile.UncheckedModelFile(modLoc("block/" + path)));
      else getBuilder(path).parent(new ModelFile.UncheckedModelFile(modLoc(path)));
    });

  }

  @Override
  public String getName() {
    return "Item Models";
  }
}
