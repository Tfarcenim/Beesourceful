package com.tfar.beesourceful;

import com.google.common.collect.Sets;
import com.tfar.beesourceful.block.CentrifugeBlock;
import com.tfar.beesourceful.block.IronBeehiveBlock;
import com.tfar.beesourceful.entity.*;
import com.tfar.beesourceful.feature.OreBeeNestFeature;
import com.tfar.beesourceful.recipe.CentrifugeRecipe;
import net.minecraft.block.*;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.OptionalDispenseBehavior;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.passive.IronBeeEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.*;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.BeehiveTileEntity;
import net.minecraft.tileentity.IronBeehiveBlockEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.PointOfInterestType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.ReplaceBlockConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.extensions.IForgeContainerType;
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

import java.util.*;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BeeSourceful.MODID)
public class BeeSourceful {

  public static final String MODID = "beesourceful";

  public BeeSourceful() {
    // Register the setup method for modloading
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    // Register the doClientStuff method for modloading
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
  }

  private void setup(final FMLCommonSetupEvent event) {

    Map<BlockState, PointOfInterestType> pointOfInterestTypeMap = new HashMap<>();

    RegistryEvents.blocks.stream().filter(IronBeehiveBlock.class::isInstance).forEach(block ->
            block.getStateContainer().getValidStates().forEach(state ->
                    pointOfInterestTypeMap.put(state, ForgeRegistries.POI_TYPES.getValue(block.getRegistryName()))));

    PointOfInterestType.field_221073_u.putAll(pointOfInterestTypeMap);

    ForgeRegistries.BIOMES.forEach(biome -> {
      biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Objectholders.Features.iron_bee_nest
              .configure(new ReplaceBlockConfig(Blocks.STONE.getDefaultState(),
                      Objectholders.iron_bee_nest.getDefaultState()
              )).createDecoratedFeature(Placement.COUNT_RANGE.
                      configure(new CountRangeConfigWrapper(5, 0, 64))));

      biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Objectholders.Features.redstone_bee_nest
              .configure(new ReplaceBlockConfig(Blocks.STONE.getDefaultState(),
                      Objectholders.redstone_bee_nest.getDefaultState()
              )).createDecoratedFeature(Placement.COUNT_RANGE.
                      configure(new CountRangeConfigWrapper(5, 0, 16))));

      biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Objectholders.Features.gold_bee_nest
              .configure(new ReplaceBlockConfig(Blocks.STONE.getDefaultState(),
                      Objectholders.redstone_bee_nest.getDefaultState()
              )).createDecoratedFeature(Placement.COUNT_RANGE.
                      configure(new CountRangeConfigWrapper(3, 0, 32))));

      biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Objectholders.Features.diamond_bee_nest
              .configure(new ReplaceBlockConfig(Blocks.STONE.getDefaultState(),
                      Objectholders.diamond_bee_nest.getDefaultState()
              )).createDecoratedFeature(Placement.COUNT_RANGE.
                      configure(new CountRangeConfigWrapper(4, 0, 16))));

      biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Objectholders.Features.lapis_bee_nest
              .configure(new ReplaceBlockConfig(Blocks.STONE.getDefaultState(),
                      Objectholders.lapis_bee_nest.getDefaultState()
              )).createDecoratedFeature(Placement.COUNT_RANGE.
                      configure(new CountRangeConfigWrapper(4, 0, 32))));

      if (biome.getCategory() == Biome.Category.EXTREME_HILLS)
        biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES,
                Objectholders.Features.emerald_bee_nest
                        .configure(new ReplaceBlockConfig(Blocks.STONE.getDefaultState(),
                                Objectholders.emerald_bee_nest.getDefaultState()
                        )).createDecoratedFeature(Placement.COUNT_RANGE.
                        configure(new CountRangeConfigWrapper(5, 0, 16))));

      biome.addFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, Objectholders.Features.ender_bee_nest
              .configure(new ReplaceBlockConfig(Blocks.END_STONE.getDefaultState(),
                      Objectholders.ender_bee_nest.getDefaultState()
              )).createDecoratedFeature(Placement.COUNT_RANGE.
                      configure(new CountRangeConfigWrapper(20, 0, 80))));

      if (biome.getCategory() == Biome.Category.NETHER) biome.
              addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Objectholders.Features.quartz_bee_nest
                      .configure(new ReplaceBlockConfig(Blocks.NETHERRACK.getDefaultState(),
                              Objectholders.quartz_bee_nest.getDefaultState()
                      )).createDecoratedFeature(Placement.COUNT_RANGE.
                              configure(new CountRangeConfigWrapper(5, 0, 128))));
    });

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

    @SubscribeEvent
    public static void block(final RegistryEvent.Register<Block> event) {
      Block.Properties properties = Block.Properties.from(Blocks.field_226906_mb_);
      Block.Properties honeycomb = Block.Properties.from(Blocks.field_226908_md_);
      register(new IronBeehiveBlock(properties), "iron_beehive", event.getRegistry());
      for (BeeType beeType : BeeType.values()) {
        register(new IronBeehiveBlock(properties), beeType + "_bee_nest", event.getRegistry());
        register(new Block(honeycomb), beeType + "_honeycomb_block", event.getRegistry());
      }
      register(new CentrifugeBlock(properties), "centrifuge", event.getRegistry());
    }

    @SubscribeEvent
    public static void item(final RegistryEvent.Register<Item> event) {
      Item.Properties properties = new Item.Properties().group(ItemGroup.BUILDING_BLOCKS);
      Item.Properties properties1 = new Item.Properties().group(ItemGroup.MATERIALS);
      for (Block block : blocks) {
        register(new BlockItem(block, properties), block.getRegistryName(), event.getRegistry());
      }
      register(new Item(properties1),"beeswax",event.getRegistry());
      for (BeeType beeType : BeeType.values()) {
        register(new Item(properties1), beeType + "_honeycomb", event.getRegistry());
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

    @SubscribeEvent
    public static void feature(final RegistryEvent.Register<Feature<?>> event) {
      for (BeeType beeType : BeeType.values()) {
        register(new OreBeeNestFeature(ReplaceBlockConfig::deserialize, (EntityType<? extends IronBeeEntity>) ForgeRegistries.ENTITIES.getValue(new ResourceLocation(MODID, beeType + "_bee"))), beeType + "_bee_nest", event.getRegistry());
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
  }

  @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
  @SuppressWarnings("unused")
  public static class ClientEvents {
    @SubscribeEvent
    public static void registerModels(FMLClientSetupEvent event) {
      for (BeeType beeType : BeeType.values())
        RenderingRegistry.registerEntityRenderingHandler(
                (EntityType<BeeEntity>) ForgeRegistries.ENTITIES.getValue(new ResourceLocation(MODID, beeType + "_bee")),
                (EntityRendererManager p_i226033_1_) -> new IronBeeRenderer(p_i226033_1_, beeType));
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
      public static final EntityType<IronBeeEntity> iron_bee = null;
      public static final EntityType<IronBeeEntity> gold_bee = null;
      public static final EntityType<IronBeeEntity> lapis_bee = null;
      public static final EntityType<IronBeeEntity> redstone_bee = null;
      public static final EntityType<IronBeeEntity> diamond_bee = null;
      public static final EntityType<IronBeeEntity> emerald_bee = null;
      public static final EntityType<IronBeeEntity> quartz_bee = null;
      public static final EntityType<IronBeeEntity> ender_bee = null;
    }

    @ObjectHolder(MODID)
    public static class Recipes {
      public static final IRecipeSerializer<CentrifugeRecipe> centrifuge = null;
    }

    @ObjectHolder(MODID)
    public static class Features {
      public static final Feature<ReplaceBlockConfig> iron_bee_nest = null;
      public static final Feature<ReplaceBlockConfig> redstone_bee_nest = null;
      public static final Feature<ReplaceBlockConfig> gold_bee_nest = null;
      public static final Feature<ReplaceBlockConfig> ender_bee_nest = null;
      public static final Feature<ReplaceBlockConfig> diamond_bee_nest = null;
      public static final Feature<ReplaceBlockConfig> emerald_bee_nest = null;
      public static final Feature<ReplaceBlockConfig> quartz_bee_nest = null;
      public static final Feature<ReplaceBlockConfig> lapis_bee_nest = null;

    }
    @ObjectHolder(MODID)
    public static class Containers {
      public static final ContainerType<CentrifugeContainer> centrifuge = null;
    }
  }
}
