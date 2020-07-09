package tfar.beesourceful;

import com.google.common.collect.Sets;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.*;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.PointOfInterestType;
import net.minecraft.world.gen.feature.ReplaceBlockConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.*;
import tfar.beesourceful.block.CentrifugeBlock;
import tfar.beesourceful.block.IronBeehiveBlock;
import tfar.beesourceful.blockentity.CentrifugeBlockEntity;
import tfar.beesourceful.blockentity.IronBeehiveBlockEntity;
import tfar.beesourceful.entity.BeeEntityType;
import tfar.beesourceful.entity.BeeProperties;
import tfar.beesourceful.recipe.CentrifugeRecipe;
import tfar.beesourceful.util.ConfigurableCountRange;
import tfar.beesourceful.util.ConfigurableCountRangeConfig;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static tfar.beesourceful.ModConfigs.SERVER_SPEC;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BeeSourceful.MODID)
public class BeeSourceful {

  public static final String MODID = "beesourceful";

  public static final ResourceLocation FLUID_STILL = new ResourceLocation("minecraft:block/water_still");
  public static final ResourceLocation FLUID_FLOWING = new ResourceLocation("minecraft:block/water_flow");

  public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
  public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
  public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, MODID);
  public static final Set<BeeEntityType> entities = new HashSet<>();


  public static RegistryObject<FlowingFluid> honey = FLUIDS.register("honey", () ->
          new HoneyFluid.Source(BeeSourceful.honey_properties)
  );
  public static RegistryObject<FlowingFluid> flowing_honey = FLUIDS.register("flowing_honey", () ->
          new HoneyFluid.Flowing(BeeSourceful.honey_properties)
  );

  public static RegistryObject<FlowingFluidBlock> honey_block = BLOCKS.register("honey", () ->
          new LiquidHoneyBlock(honey, Block.Properties.create(Material.WATER).doesNotBlockMovement().hardnessAndResistance(100.0F).noDrops())
  );
  public static RegistryObject<Item> honey_bucket = ITEMS.register("honey_bucket", () ->
          new BucketItem(honey, new Item.Properties().containerItem(Items.BUCKET).maxStackSize(1).group(ItemGroup.MISC))
  );

  public static final ForgeFlowingFluid.Properties honey_properties =
          new ForgeFlowingFluid.Properties(honey, flowing_honey, FluidAttributes.builder(FLUID_STILL, FLUID_FLOWING).color(0xffff9000))
                  .bucket(honey_bucket).block(honey_block);

  public BeeSourceful() {
    BeeProperties.loadCustomBees();

    // Register the setup method for modloading
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    // Register the doClientStuff method for modloading
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
    ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_SPEC);

    IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

    BLOCKS.register(modEventBus);
    ITEMS.register(modEventBus);
    FLUIDS.register(modEventBus);
  }

  public static final Placement<ConfigurableCountRangeConfig> CONFIGURABLE_COUNT_RANGE = new ConfigurableCountRange(ConfigurableCountRangeConfig.CODEC);

  private void setup(final FMLCommonSetupEvent event) {

    DeferredWorkQueue.runLater(() -> {
      entities.forEach( beeType -> {
                GlobalEntityTypeAttributes.put(beeType,BeeEntity.func_234182_eX_().func_233813_a_());
              });

      Map<BlockState, PointOfInterestType> pointOfInterestTypeMap = new HashMap<>();

      RegistryEvents.blocks.stream().filter(IronBeehiveBlock.class::isInstance).forEach(block ->
              block.getStateContainer().getValidStates().forEach(state ->
                      pointOfInterestTypeMap.put(state, ForgeRegistries.POI_TYPES.getValue(block.getRegistryName()))));

      PointOfInterestType.POIT_BY_BLOCKSTATE.putAll(pointOfInterestTypeMap);

      ForgeRegistries.BIOMES.forEach(biome -> entities.forEach(beeType -> {
        if (beeType.beeProperties.allowed_biomes.test(biome))
          biome.addFeature(beeType.beeProperties.generation_stage,
                  beeType.beeProperties.feature.withConfiguration(new ReplaceBlockConfig(beeType.beeProperties.replace_block,
                          beeType.beeProperties.getHive().
                                  getDefaultState().with(IronBeehiveBlock.TICK, false)
                  )).withPlacement(CONFIGURABLE_COUNT_RANGE.
                          configure(new ConfigurableCountRangeConfig(beeType.getRegistryName().toString()))));
      }));
    });
  }

  private void doClientStuff(final FMLClientSetupEvent event) {
    ScreenManager.registerFactory(Objectholders.Containers.centrifuge, CentrifugeScreen::new);
  }

  @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
  public static class RegistryEvents {

    public static final Set<Block> blocks = new HashSet<>();
    public static final Set<Item> items = new HashSet<>();

    @SubscribeEvent
    public static void block(final RegistryEvent.Register<Block> event) {
      for (Map.Entry<ResourceLocation,BeeProperties> beeEntry : BeeProperties.bee_registry.entrySet()) {
        BeeEntityType bee = (BeeEntityType) new BeeEntityType(false, beeEntry.getValue()).setRegistryName(beeEntry.getKey());
        entities.add(bee);
      }
      Block.Properties ironbeehive = Block.Properties.create(Material.IRON).hardnessAndResistance(2).sound(SoundType.METAL);
      Block.Properties honeycomb = Block.Properties.from(Blocks.HONEYCOMB_BLOCK);
      register(new IronBeehiveBlock(ironbeehive), "iron_beehive", event.getRegistry());
      for (BeeEntityType beeType : entities) {
        register(new IronBeehiveBlock(Block.Properties.from(Blocks.BEE_NEST)), beeType.getRegistryName().getPath() + "_nest", event.getRegistry());
        register(new Block(honeycomb), beeType.beeProperties.name + "_honeycomb_block", event.getRegistry());
      }
      register(new CentrifugeBlock(ironbeehive), "centrifuge", event.getRegistry());
    }

    @SubscribeEvent
    public static void item(final RegistryEvent.Register<Item> event) {
      Item.Properties properties = new Item.Properties().group(ItemGroup.BUILDING_BLOCKS);
      Item.Properties properties1 = new Item.Properties().group(ItemGroup.MATERIALS);
      Item.Properties egg = new Item.Properties().group(ItemGroup.MISC);

			ITag.INamedTag<Block> small_flowers = BlockTags.SMALL_FLOWERS;

      ITag.INamedTag<Block> nether_flowers = Tags.NETHER_FLOWERS;
      ITag.INamedTag<Block> end_flowers = Tags.END_FLOWERS;
      String s = "_spawn_egg";

      for (BeeEntityType beeEntry : entities) {
        register(new SpawnEggItem(beeEntry,6,6,egg),new ResourceLocation(beeEntry.getRegistryName()+s),event.getRegistry());
        register(new Item(properties1), beeEntry.beeProperties.name + "_honeycomb", event.getRegistry());
      }

      for (Block block : blocks) {
        register(new BlockItem(block, properties), block.getRegistryName(), event.getRegistry());
      }
      register(new Item(properties1),"beeswax",event.getRegistry());
    }

    @SubscribeEvent
    public static void recipe(final RegistryEvent.Register<IRecipeSerializer<?>> event) {
      register(new CentrifugeRecipe.Serializer<>(CentrifugeRecipe::new), "centrifuge", event.getRegistry());
    }

    @SubscribeEvent
    public static void container(final RegistryEvent.Register<ContainerType<?>> event) {
    register(IForgeContainerType.create((id,inv,c) -> new CentrifugeContainer(id, inv.player.world, c.readBlockPos(),inv))
            ,"centrifuge",event.getRegistry());
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

      register(TileEntityType.Builder
              .create(CentrifugeBlockEntity::new, Objectholders.centrifuge)
              .build(null), "centrifuge", event.getRegistry());
    }

    @SubscribeEvent
    public static void entity(final RegistryEvent.Register<EntityType<?>> event) {
      entities.forEach(entityType -> registerNoSet(entityType,entityType.getRegistryName(),event.getRegistry()));
    }

    @SubscribeEvent
    public static void poi(final RegistryEvent.Register<PointOfInterestType> event) {
      register(new PointOfInterestType("iron_beehive",
                      Sets.newHashSet(Objectholders.iron_beehive.getStateContainer().getValidStates()),
                      0, 1),
              "iron_beehive", event.getRegistry());

      for (BeeEntityType beeType : entities) {
        register(new PointOfInterestType(beeType.getRegistryName() + "_nest",
                        new HashSet<>(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(beeType.getRegistryName().getPath() + "_nest"))
                                .getStateContainer().getValidStates()), 0, 1)
                , beeType.getRegistryName().getPath() + "_nest", event.getRegistry());
      }
    }

    private static <T extends IForgeRegistryEntry<T>> void register(T obj, String name, IForgeRegistry<T> registry) {
      register(obj, new ResourceLocation(MODID, name), registry);
    }

    private static <T extends IForgeRegistryEntry<T>> void register(T obj, ResourceLocation name, IForgeRegistry<T> registry) {
      registry.register(obj.setRegistryName(name));
      if (obj instanceof Block) blocks.add((Block) obj);
      if (obj instanceof Item) items.add((Item) obj);
    }

    private static <T extends IForgeRegistryEntry<T>> void registerNoSet(T obj, ResourceLocation name, IForgeRegistry<T> registry) {
      registry.register(obj);
      if (obj instanceof Block) blocks.add((Block) obj);
      if (obj instanceof Item) items.add((Item) obj);
    }
  }

  @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
  public static class ClientEvents {
    @SubscribeEvent
    public static void registerModels(FMLClientSetupEvent event) {
      for (BeeEntityType beeType : entities)
        RenderingRegistry.registerEntityRenderingHandler(
                (EntityType<BeeEntity>) ForgeRegistries.ENTITIES.getValue(new ResourceLocation(MODID, beeType.getRegistryName().getPath())),
                p_i226033_1_ -> new IronBeeRenderer(p_i226033_1_, beeType));
    }
  }

  @ObjectHolder(MODID)
  public static class Objectholders {
    public static final Block iron_beehive = null;
    public static final Block centrifuge = null;

    public static final Item iron_honeycomb = null;
    public static final Item gold_honeycomb = null;
    public static final Item redstone_honeycomb = null;
    public static final Item diamond_honeycomb = null;
    public static final Item emerald_honeycomb = null;
    public static final Item ender_honeycomb = null;
    public static final Item quartz_honeycomb = null;
    public static final Item lapis_honeycomb = null;

    public static final Item beeswax = null;

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
      public static final TileEntityType<CentrifugeBlockEntity> centrifuge = null;
    }

    @ObjectHolder(MODID)
    public static class Entities {
      public static final BeeEntityType iron_bee = null;
      public static final BeeEntityType gold_bee = null;
      public static final BeeEntityType lapis_bee = null;
      public static final BeeEntityType redstone_bee = null;
      public static final BeeEntityType diamond_bee = null;
      public static final BeeEntityType emerald_bee = null;
      public static final BeeEntityType quartz_bee = null;
      public static final BeeEntityType ender_bee = null;
    }

    @ObjectHolder(MODID)
    public static class Recipes {
      public static final IRecipeSerializer<CentrifugeRecipe> centrifuge = null;
    }

    @ObjectHolder(MODID)
    public static class Containers {
      public static final ContainerType<CentrifugeContainer> centrifuge = null;
    }
  }
}
