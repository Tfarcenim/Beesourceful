package com.tfar.beesourceful.data.provider;

import com.tfar.beesourceful.BeeSourceful;
import com.tfar.beesourceful.block.CentrifugeBlock;
import com.tfar.beesourceful.block.IronBeehiveBlock;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelFile;

public class ModBlockstateProvider extends BlockStateProvider {
  public ModBlockstateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
    super(gen, BeeSourceful.MODID, exFileHelper);
  }

  @Override
  protected void registerStatesAndModels() {

    centrifuge();
    ironBeehive();
    liquidHoneyBlock();

    BeeSourceful.RegistryEvents.blocks.stream().filter(IronBeehiveBlock.class::isInstance)
            .filter(block -> block != BeeSourceful.Objectholders.iron_beehive)
            .forEach(block -> {
              String name = block.getRegistryName().getPath();
              ModelFile model = models().getBuilder(name)
                      .parent(models().getExistingFile(mcLoc("block/orientable_with_bottom")))
                      .texture("particle", new ResourceLocation(BeeSourceful.MODID, "block/" + name + "_side"))
                      .texture("bottom", new ResourceLocation(BeeSourceful.MODID, "block/" + name + "_bottom"))
                      .texture("top", new ResourceLocation(BeeSourceful.MODID, "block/" + name + "_top"))
                      .texture("front", new ResourceLocation(BeeSourceful.MODID, "block/" + name + "_front"))
                      .texture("side", new ResourceLocation(BeeSourceful.MODID, "block/" + name + "_side"));

              String nameHoney = block.getRegistryName().getPath() + "_honey";
              ModelFile modelHoney = models().getBuilder(nameHoney)
                      .parent(models().getExistingFile(mcLoc("block/orientable_with_bottom")))
                      .texture("particle", new ResourceLocation(BeeSourceful.MODID, "block/" + name + "_side"))
                      .texture("bottom", new ResourceLocation(BeeSourceful.MODID, "block/" + name + "_bottom"))
                      .texture("top", new ResourceLocation(BeeSourceful.MODID, "block/" + name + "_top"))
                      .texture("front", new ResourceLocation(BeeSourceful.MODID, "block/" + name + "_front_honey"))
                      .texture("side", new ResourceLocation(BeeSourceful.MODID, "block/" + name + "_side"));
              getVariantBuilder(block).forAllStates(state -> state.get(IronBeehiveBlock.HONEY_LEVEL) == 5 ?
                      ConfiguredModel.builder().modelFile(modelHoney).build() : ConfiguredModel.builder().modelFile(model).build());
            });

    BeeSourceful.RegistryEvents.blocks.stream()
            .filter(block -> block.getRegistryName().getPath().contains("honeycomb"))
            .forEach(block -> {
              String name = block.getRegistryName().getPath();
              ModelFile model = models().getBuilder(name)
                      .parent(models().getExistingFile(mcLoc("block/cube_all")))
                      .texture("all", new ResourceLocation(BeeSourceful.MODID, "block/" + name));
              getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder().modelFile(model).build());
            });

  }

  protected void liquidHoneyBlock() {
    Block honeyblock = BeeSourceful.honey_block.get();

    String name = honeyblock.getRegistryName().getPath();
    ModelFile model = models().getBuilder(name)
            .texture("particle",mcLoc("block/water_still"));

    getVariantBuilder(honeyblock).forAllStatesExcept(state ->  ConfiguredModel.builder()
                    .modelFile(model).build(),
            BlockStateProperties.LEVEL_0_15);

  }

  protected void centrifuge(){
    Block centrifuge = BeeSourceful.Objectholders.centrifuge;
    String name = centrifuge.getRegistryName().getPath();
    ModelFile model = models().getBuilder(name)
            .parent(models().getExistingFile(mcLoc("block/orientable_vertical")))
            .texture("front", new ResourceLocation(BeeSourceful.MODID, "block/" + name + "_front"))
            .texture("side", new ResourceLocation(BeeSourceful.MODID, "block/" + name + "_side"));

    ModelFile modelOn = models().getBuilder(name+"_on")
            .parent(models().getExistingFile(mcLoc("block/orientable_vertical")))
            .texture("front", new ResourceLocation(BeeSourceful.MODID, "block/" + name + "_front_on"))
            .texture("side", new ResourceLocation(BeeSourceful.MODID, "block/" + name + "_side"));
    getVariantBuilder(centrifuge).forAllStates(state -> {

      Direction dir = state.get(CentrifugeBlock.PROPERTY_FACING);
      int rotX = 0;
      int rotY = 0;

      switch (dir){
        case EAST:rotX=90;rotY=90;break;
        case WEST:rotX=90;rotY=270;break;
        case SOUTH:rotX=90;rotY=180;break;
        case NORTH:rotX=90;break;
        case DOWN:rotX=180;break;
        case UP:default:
      }

      return state.get(CentrifugeBlock.PROPERTY_ON) ?
              ConfiguredModel.builder().modelFile(modelOn).rotationX(rotX).rotationY(rotY).build() :
              ConfiguredModel.builder().modelFile(model).rotationX(rotX).rotationY(rotY).build();
    });

  }

  protected void ironBeehive() {
    ResourceLocation iron_block = mcLoc("block/iron_block");
    Block iron_beehive = BeeSourceful.Objectholders.iron_beehive;
    String name = iron_beehive.getRegistryName().getPath();
    String nameHoney = iron_beehive.getRegistryName().getPath() + "_honey";

    ModelFile model = models().getBuilder(name)
            .parent(models().getExistingFile(mcLoc("block/orientable_with_bottom")))
            .texture("particle", iron_block)
            .texture("bottom", iron_block)
            .texture("top", iron_block)
            .texture("front", new ResourceLocation(BeeSourceful.MODID, "block/" + name + "_front"))
            .texture("side", new ResourceLocation(BeeSourceful.MODID, "block/" + name + "_side"));

    ModelFile modelHoney = models().getBuilder(nameHoney)
            .parent(models().getExistingFile(mcLoc("block/orientable_with_bottom")))
            .texture("particle", iron_block)
            .texture("bottom", iron_block)
            .texture("top", iron_block)
            .texture("front", "block/" + name + "_front_honey")
            .texture("side", "block/" + name + "_side");

    getVariantBuilder(iron_beehive).forAllStates(state -> state.get(IronBeehiveBlock.HONEY_LEVEL) == 5 ?
            ConfiguredModel.builder().modelFile(modelHoney).build() : ConfiguredModel.builder().modelFile(model).build());
  }
}
