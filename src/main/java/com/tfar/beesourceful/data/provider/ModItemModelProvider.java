package com.tfar.beesourceful.data.provider;

import com.tfar.beesourceful.BeeSourceful;
import net.minecraft.data.DataGenerator;
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
      getBuilder(path)
              .parent(new ModelFile.UncheckedModelFile(modLoc("block/" + path)));
    });

  }

  @Override
  public String getName() {
    return "Item Models";
  }
}
