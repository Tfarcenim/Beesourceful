package com.tfar.beesourceful;

import com.google.common.collect.Sets;
import com.tfar.beesourceful.block.IronBeehiveBlock;
import com.tfar.beesourceful.entity.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.passive.IronBeeEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BeeSourceful.MODID)
public class BeeSourceful {
  // Directly reference a log4j logger.

  public static final String MODID = "beesourceful";

  public BeeSourceful() {
    // Register the setup method for modloading
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    // Register the doClientStuff method for modloading
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);


  }

  private void setup(final FMLCommonSetupEvent event) {
    Map<BlockState, PointOfInterestType> pointOfInterestTypeMap = new HashMap<>();
    Objectholders.iron_bee_nest.getStateContainer().getValidStates().forEach(state -> pointOfInterestTypeMap.put(state,
            Objectholders.POI.iron_bee_nest));
    Objectholders.iron_beehive.getStateContainer().getValidStates().forEach(state -> pointOfInterestTypeMap.put(state,
            Objectholders.POI.iron_beehive));
    PointOfInterestType.field_221073_u.putAll(pointOfInterestTypeMap);
  }

  private void doClientStuff(final FMLClientSetupEvent event) {
  }

  @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
  public static class RegistryEvents {

    public static final Set<Block> blocks = new HashSet<>();
    public static final Set<Item> items = new HashSet<>();

    @SubscribeEvent
    public static void block(final RegistryEvent.Register<Block> event) {
      Block.Properties properties = Block.Properties.from(Blocks.field_226906_mb_);
      Block.Properties honeycomb = Block.Properties.from(Blocks.field_226908_md_);
      register(new IronBeehiveBlock(properties), "iron_beehive", event.getRegistry());
      for (BeeType beeType : BeeType.values()) {
        register(new IronBeehiveBlock(properties), beeType + "_bee_nest", event.getRegistry());
        register(new Block(properties), beeType + "_honeycomb_block", event.getRegistry());
      }
    }

    @SubscribeEvent
    public static void item(final RegistryEvent.Register<Item> event) {
      Item.Properties properties = new Item.Properties().group(ItemGroup.BUILDING_BLOCKS);
      Item.Properties properties1 = new Item.Properties().group(ItemGroup.MATERIALS);
      for (Block block : blocks) {
        register(new BlockItem(block, properties), block.getRegistryName(), event.getRegistry());
      }

      for (BeeType beeType : BeeType.values()) {
        register(new Item(properties1), beeType + "_honeycomb", event.getRegistry());
      }
    }

    @SubscribeEvent
    public static void blockentity(final RegistryEvent.Register<TileEntityType<?>> event) {
      register(TileEntityType.Builder
              .create(IronBeehiveBlockEntity::new,
                      blocks.stream()
                              .filter(IronBeehiveBlock.class::isInstance)
                              .toArray(Block[]::new)
              )
              .build(null), "iron_beehive", event.getRegistry());
    }

    @SubscribeEvent
    public static void entity(final RegistryEvent.Register<EntityType<?>> event) {
      register(
              EntityType.Builder
                      .create(IronBeeEntity::new, EntityClassification.CREATURE)
                      .size(0.7F, 0.6F)
                      .build("")
              , "iron_bee", event.getRegistry());
      register(
              EntityType.Builder
                      .create(RedstoneBeeEntity::new, EntityClassification.CREATURE)
                      .size(0.7F, 0.6F)
                      .build("")
              , "redstone_bee", event.getRegistry());
      register(
              EntityType.Builder
                      .create(GoldBeeEntity::new, EntityClassification.CREATURE)
                      .size(0.7F, 0.6F)
                      .build("")
              , "gold_bee", event.getRegistry());
      register(
              EntityType.Builder
                      .create(EmeraldBeeEntity::new, EntityClassification.CREATURE)
                      .size(0.7F, 0.6F)
                      .build("")
              , "emerald_bee", event.getRegistry());
      register(
              EntityType.Builder
                      .create(DiamondBeeEntity::new, EntityClassification.CREATURE)
                      .size(0.7F, 0.6F)
                      .build("")
              , "diamond_bee", event.getRegistry());
      register(
              EntityType.Builder
                      .create(LapisBeeEntity::new, EntityClassification.CREATURE)
                      .size(0.7F, 0.6F)
                      .build("")
              , "lapis_bee", event.getRegistry());
      register(
              EntityType.Builder
                      .create(QuartzBeeEntity::new, EntityClassification.CREATURE)
                      .size(0.7F, 0.6F)
                      .build("")
              , "quartz_bee", event.getRegistry());
      register(
              EntityType.Builder
                      .create(EnderBeeEntity::new, EntityClassification.CREATURE)
                      .size(0.7F, 0.6F)
                      .build("")
              , "ender_bee", event.getRegistry());
    }

    @SubscribeEvent
    public static void poi(final RegistryEvent.Register<PointOfInterestType> event) {
      register(new PointOfInterestType("iron_beehive",
                      Sets.newHashSet(Objectholders.iron_beehive.getStateContainer().getValidStates()),
                      0, 1),
              "iron_beehive", event.getRegistry());

      for (BeeType beeType : BeeType.values()) {
        register(new PointOfInterestType(beeType + "_bee_nest",
                        new HashSet<>(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(beeType + "_bee_nest"))
                                .getStateContainer().getValidStates()), 0, 1)
                , beeType + "_bee_nest", event.getRegistry());
      }
    }

    private static <T extends IForgeRegistryEntry<T>> void register(T obj, String name, IForgeRegistry<T> registry) {
      register(obj, new ResourceLocation(MODID, name), registry);
    }

    private static <T extends IForgeRegistryEntry<T>> void register(T obj, ResourceLocation name, IForgeRegistry<T> registry) {
      registry.register(obj.setRegistryName(name));
      if (obj instanceof Block) blocks.add((Block)obj);
      if (obj instanceof Item) items.add((Item)obj);
    }
  }

  @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
  @SuppressWarnings("unused")
  public static class ClientEvents {
    @SubscribeEvent
    public static void registerModels(FMLClientSetupEvent event) {
      for (BeeType beeType: BeeType.values())
      RenderingRegistry.registerEntityRenderingHandler(
              (EntityType<BeeEntity>)ForgeRegistries.ENTITIES.getValue(new ResourceLocation(MODID,beeType+"_bee")),
              (EntityRendererManager p_i226033_1_) -> new IronBeeRenderer(p_i226033_1_,beeType));
    }
  }

  @ObjectHolder(MODID)
  public static class Objectholders {
    public static final Block iron_beehive = null;

    public static final Item iron_honeycomb = null;
    public static final Item gold_honeycomb = null;
    public static final Item redstone_honeycomb = null;
    public static final Item diamond_honeycomb = null;
    public static final Item emerald_honeycomb = null;
    public static final Item ender_honeycomb = null;
    public static final Item quartz_honeycomb = null;
    public static final Item lapis_honeycomb = null;


    public static final Block
            iron_bee_nest = null,
            gold_bee_nest = null,
            redstone_bee_nest = null,
            diamond_bee_nest = null,
            emerald_bee_nest = null,
            ender_bee_nest = null,
            quartz_bee_nest = null,
            lapis_bee_nest = null;

    @ObjectHolder(MODID)
    public static class POI {
      public static final PointOfInterestType iron_beehive = null;

      public static final PointOfInterestType iron_bee_nest = null;
      public static final PointOfInterestType gold_bee_nest = null;
      public static final PointOfInterestType redstone_bee_nest = null;
      public static final PointOfInterestType diamond_bee_nest = null;
      public static final PointOfInterestType emerald_bee_nest = null;
      public static final PointOfInterestType ender_bee_nest = null;
      public static final PointOfInterestType quartz_bee_nest = null;
      public static final PointOfInterestType lapis_bee_nest = null;
    }

    @ObjectHolder(MODID)
    public static class BlockEntities {
      public static final TileEntityType<IronBeehiveBlockEntity> iron_beehive = null;
    }

    @ObjectHolder(MODID)
    public static class Entities {
      public static final EntityType<IronBeeEntity> iron_bee = null;
      public static final EntityType<IronBeeEntity> gold_bee = null;
      public static final EntityType<IronBeeEntity> lapis_bee = null;
      public static final EntityType<IronBeeEntity> redstone_bee = null;
      public static final EntityType<IronBeeEntity> diamond_bee = null;
      public static final EntityType<IronBeeEntity> emerald_bee = null;
      public static final EntityType<IronBeeEntity> quartz_bee = null;
      public static final EntityType<IronBeeEntity> ender_bee = null;
    }
  }
}
