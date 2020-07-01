package tfar.beesourceful;

import com.google.common.collect.Sets;
import tfar.beesourceful.block.CentrifugeBlock;
import tfar.beesourceful.block.IronBeehiveBlock;
import tfar.beesourceful.blockentity.CentrifugeBlockEntity;
import tfar.beesourceful.entity.BeeProperties;
import tfar.beesourceful.recipe.CentrifugeRecipe;
import tfar.beesourceful.util.BeeType;
import tfar.beesourceful.util.ConfigurableCountRange;
import tfar.beesourceful.util.ConfigurableCountRangeConfig;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.OptionalDispenseBehavior;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.*;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.tileentity.BeehiveTileEntity;
import tfar.beesourceful.blockentity.IronBeehiveBlockEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.PointOfInterestType;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.ReplaceBlockConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.*;
import tfar.beesourceful.entity.BeeEntityType;

import java.util.*;

import static tfar.beesourceful.ModConfigs.SERVER_SPEC;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BeeSourceful.MODID)
public class BeeSourceful {

  public static final String MODID = "beesourceful";

  public static final ResourceLocation FLUID_STILL = new ResourceLocation("minecraft:block/water_still");
  public static final ResourceLocation FLUID_FLOWING = new ResourceLocation("minecraft:block/water_flow");

  public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, MODID);
  public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, MODID);
  public static final DeferredRegister<Fluid> FLUIDS = new DeferredRegister<>(ForgeRegistries.FLUIDS, MODID);


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

  public static final Placement<ConfigurableCountRangeConfig> CONFIGURABLE_COUNT_RANGE = new ConfigurableCountRange(ConfigurableCountRangeConfig::deserialize);

  private void setup(final FMLCommonSetupEvent event) {

    Map<BlockState, PointOfInterestType> pointOfInterestTypeMap = new HashMap<>();

    RegistryEvents.blocks.stream().filter(IronBeehiveBlock.class::isInstance).forEach(block ->
            block.getStateContainer().getValidStates().forEach(state ->
                    pointOfInterestTypeMap.put(state, ForgeRegistries.POI_TYPES.getValue(block.getRegistryName()))));

    PointOfInterestType.field_221073_u.putAll(pointOfInterestTypeMap);

    ForgeRegistries.BIOMES.forEach(biome -> BeeType.bee_registry.values().forEach(beeType -> {
      if (beeType.allowed_biomes.test(biome))
        biome.addFeature(beeType.generation_stage,
                beeType.feature.configure(new ReplaceBlockConfig(beeType.replace_block,
                                beeType.beeSupplier.get().beeProperties.hive_block.
																				getDefaultState().with(IronBeehiveBlock.TICK,false)
                        )).createDecoratedFeature(CONFIGURABLE_COUNT_RANGE.
                        configure(new ConfigurableCountRangeConfig(beeType))));
    }));

    //todo, replace with mixin
      DispenserBlock.registerDispenseBehavior(Items.SHEARS.asItem(), new OptionalDispenseBehavior() {
        /**
         * Dispense the specified stack, play the dispense sound and spawn particles.
         */
        @SuppressWarnings("deprecation")
        protected ItemStack dispenseStack(IBlockSource p_82487_1_, ItemStack stack) {
          World world = p_82487_1_.getWorld();
          if (!world.isRemote()) {
            this.successful = false;
            BlockPos blockpos = p_82487_1_.getBlockPos().offset(p_82487_1_.getBlockState().get(DispenserBlock.FACING));

            for (Entity entity : world.getEntitiesInAABBexcluding(null, new AxisAlignedBB(blockpos), e -> !e.isSpectator() && e instanceof IShearable)) {
              IShearable target = (IShearable) entity;
              if (target.isShearable(stack, world, blockpos)) {
                List<ItemStack> drops = target.onSheared(stack, entity.world, blockpos,
                        EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack));
                Random rand = new Random();
                drops.forEach(d -> {
                  ItemEntity ent = entity.entityDropItem(d, 1.0F);
                  ent.setMotion(ent.getMotion().add((double) ((rand.nextFloat() - rand.nextFloat()) * 0.1F), (double) (rand.nextFloat() * 0.05F), (double) ((rand.nextFloat() - rand.nextFloat()) * 0.1F)));
                });
                if (stack.attemptDamageItem(1, world.rand, null)) {
                  stack.setCount(0);
                }

                this.successful = true;
                break;
              }
            }

            if (!this.successful) {
              BlockState blockstate = world.getBlockState(blockpos);
              if (blockstate.isIn(BlockTags.field_226151_aa_)) {
                int i = blockstate.get(BeehiveBlock.HONEY_LEVEL);
                if (i >= 5) {
                  if (stack.attemptDamageItem(1, world.rand, null)) {
                    stack.setCount(0);
                  }

                  BeehiveBlock.dropHoneycomb(world, blockpos);
                  ((BeehiveBlock) blockstate.getBlock()).takeHoney(world, blockstate, blockpos, null, BeehiveTileEntity.State.BEE_RELEASED);
                  this.successful = true;
                }
              } else if (blockstate.getBlock() instanceof IronBeehiveBlock) {
                int i = blockstate.get(BeehiveBlock.HONEY_LEVEL);
                if (i >= 5) {
                  if (stack.attemptDamageItem(1, world.rand, null)) {
                    stack.setCount(0);
                  }

                  IronBeehiveBlock.dropResourceHoneyComb((IronBeehiveBlock) blockstate.getBlock(), world, blockpos);
                  ((BeehiveBlock) blockstate.getBlock()).takeHoney(world, blockstate, blockpos, null,
                          BeehiveTileEntity.State.BEE_RELEASED);
                  this.successful = true;
                }
              }
            }
          }
          return stack;
        }
      });
  }

  private void doClientStuff(final FMLClientSetupEvent event) {
    ScreenManager.registerFactory(Objectholders.Containers.centrifuge, CentrifugeScreen::new);
  }

  @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
  public static class RegistryEvents {

    public static final Set<Block> blocks = new HashSet<>();
    public static final Set<Item> items = new HashSet<>();
    public static final Set<EntityType<?>> entities = new HashSet<>();

    @SubscribeEvent
    public static void block(final RegistryEvent.Register<Block> event) {

      BeeType.loadCustomBees();

      Block.Properties ironbeehive = Block.Properties.create(Material.IRON).hardnessAndResistance(2).sound(SoundType.METAL);
      Block.Properties honeycomb = Block.Properties.from(Blocks.field_226908_md_);
      register(new IronBeehiveBlock(ironbeehive), "iron_beehive", event.getRegistry());
      for (BeeType beeType : BeeType.bee_registry.values()) {
        register(new IronBeehiveBlock(Block.Properties.from(Blocks.field_226905_ma_)), beeType.id.getPath() + "_bee_nest", event.getRegistry());
        register(new Block(honeycomb), beeType.id.getPath() + "_honeycomb_block", event.getRegistry());
      }
      register(new CentrifugeBlock(ironbeehive), "centrifuge", event.getRegistry());
    }

    @SubscribeEvent
    public static void item(final RegistryEvent.Register<Item> event) {
      Item.Properties properties = new Item.Properties().group(ItemGroup.BUILDING_BLOCKS);
      Item.Properties properties1 = new Item.Properties().group(ItemGroup.MATERIALS);
      Item.Properties egg = new Item.Properties().group(ItemGroup.MISC);

			Tag<Block> small_flowers = BlockTags.field_226149_I_;

			Tag<Block> nether_flowers = Tags.NETHER_FLOWERS;
			Tag<Block> end_flowers = Tags.END_FLOWERS;

      EntityType<?> iron_bee = new BeeEntityType(false,
							new BeeProperties(() -> Objectholders.POI.iron_bee_nest,Objectholders.iron_bee_nest,Blocks.IRON_ORE,
											() -> Objectholders.iron_honeycomb, small_flowers)).setRegistryName("iron_bee");


			EntityType<?> redstone_bee = new BeeEntityType(false,
							new BeeProperties(() -> Objectholders.POI.redstone_bee_nest,Objectholders.redstone_bee_nest,Blocks.REDSTONE_ORE,
											() -> Objectholders.redstone_honeycomb, small_flowers)).setRegistryName("redstone_bee");

			EntityType<?> gold_bee = new BeeEntityType(false,
							new BeeProperties(() -> Objectholders.POI.gold_bee_nest,Objectholders.gold_bee_nest,Blocks.GOLD_ORE,
											() -> Objectholders.gold_honeycomb, small_flowers)).setRegistryName("gold_bee");

			EntityType<?> lapis_bee = new BeeEntityType(false,
							new BeeProperties(() -> Objectholders.POI.lapis_bee_nest,Objectholders.lapis_bee_nest,Blocks.LAPIS_ORE,
											() -> Objectholders.lapis_honeycomb, small_flowers)).setRegistryName("lapis_bee");

			EntityType<?> emerald_bee = new BeeEntityType(false,
							new BeeProperties(() -> Objectholders.POI.emerald_bee_nest,Objectholders.emerald_bee_nest,Blocks.EMERALD_ORE,
											() -> Objectholders.emerald_honeycomb, small_flowers)).setRegistryName("emerald_bee");

			EntityType<?> diamond_bee = new BeeEntityType(false,
							new BeeProperties(() -> Objectholders.POI.diamond_bee_nest,Objectholders.diamond_bee_nest,Blocks.DIAMOND_ORE,
											() -> Objectholders.diamond_honeycomb, small_flowers)).setRegistryName("diamond_bee");

			EntityType<?> quartz_bee = new BeeEntityType(true,
							new BeeProperties(() -> Objectholders.POI.quartz_bee_nest,Objectholders.quartz_bee_nest,Blocks.NETHER_QUARTZ_ORE,
											() -> Objectholders.quartz_honeycomb, nether_flowers)).setRegistryName("quartz_bee");

			EntityType<?> ender_bee = new BeeEntityType(false,
							new BeeProperties(() -> Objectholders.POI.ender_bee_nest,Objectholders.ender_bee_nest,Blocks.END_STONE,
											() -> Objectholders.ender_honeycomb, end_flowers)).setRegistryName("ender_bee");


			String s = "_spawn_egg";

      register(new SpawnEggItem(iron_bee,6,6,egg),new ResourceLocation(iron_bee.getRegistryName()+s),event.getRegistry());
      register(new SpawnEggItem(redstone_bee,6,6,egg),new ResourceLocation(redstone_bee.getRegistryName()+s),event.getRegistry());
      register(new SpawnEggItem(gold_bee,6,6,egg),new ResourceLocation(gold_bee.getRegistryName()+s),event.getRegistry());
      register(new SpawnEggItem(lapis_bee,6,6,egg),new ResourceLocation(lapis_bee.getRegistryName()+s),event.getRegistry());
      register(new SpawnEggItem(emerald_bee,6,6,egg),new ResourceLocation(emerald_bee.getRegistryName()+s),event.getRegistry());
      register(new SpawnEggItem(diamond_bee,6,6,egg),new ResourceLocation(diamond_bee.getRegistryName()+s),event.getRegistry());
      register(new SpawnEggItem(quartz_bee,6,6,egg),new ResourceLocation(quartz_bee.getRegistryName()+s),event.getRegistry());
      register(new SpawnEggItem(ender_bee,6,6,egg),new ResourceLocation(ender_bee.getRegistryName()+s),event.getRegistry());

      entities.add(iron_bee);
      entities.add(gold_bee);
      entities.add(redstone_bee);
      entities.add(diamond_bee);
      entities.add(emerald_bee);
      entities.add(quartz_bee);
      entities.add(ender_bee);
      entities.add(lapis_bee);

      for (Block block : blocks) {
        register(new BlockItem(block, properties), block.getRegistryName(), event.getRegistry());
      }
      register(new Item(properties1),"beeswax",event.getRegistry());
      for (BeeType beeType : BeeType.bee_registry.values()) {
        register(new Item(properties1), beeType.id.getPath() + "_honeycomb", event.getRegistry());
      }
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

      for (BeeType beeType : BeeType.bee_registry.values()) {
        register(new PointOfInterestType(beeType.id + "_bee_nest",
                        new HashSet<>(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(beeType.id.getPath() + "_bee_nest"))
                                .getStateContainer().getValidStates()), 0, 1)
                , beeType.id.getPath() + "_bee_nest", event.getRegistry());
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
  @SuppressWarnings("unused")
  public static class ClientEvents {
    @SubscribeEvent
    public static void registerModels(FMLClientSetupEvent event) {
      for (BeeType beeType : BeeType.bee_registry.values())
        RenderingRegistry.registerEntityRenderingHandler(
                (EntityType<BeeEntity>) ForgeRegistries.ENTITIES.getValue(new ResourceLocation(MODID, beeType.id.getPath() + "_bee")),
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
