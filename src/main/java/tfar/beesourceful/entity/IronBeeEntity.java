package tfar.beesourceful.entity;

import tfar.beesourceful.BeeSourceful;
import net.minecraft.entity.passive.BeeEntity;
import tfar.beesourceful.blockentity.IronBeehiveBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.BreedGoal;
import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.PointOfInterest;
import net.minecraft.village.PointOfInterestManager;
import net.minecraft.village.PointOfInterestType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.Tags;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("EntityConstructor")
public class IronBeeEntity extends BeeEntity {

  public IronBeeEntity(EntityType<? extends BeeEntity> p_i225714_1_, World p_i225714_2_) {
    super(p_i225714_1_, p_i225714_2_);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new BeeEntity.StingGoal(this, 1.4, true));
    this.goalSelector.addGoal(1, new BeeEntity.EnterBeehiveGoal());
    this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
    this.goalSelector.addGoal(3, new TemptGoal(this, 1.25D, Ingredient.fromTag(ItemTags.field_226159_I_), false));
    this.pollinateGoal = new PollinateGoal2();
    this.goalSelector.addGoal(4, this.pollinateGoal);
    this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.25D));
    this.goalSelector.addGoal(5, new UpdateBeehiveGoal2());
    this.moveToHiveGoal = new FindBeehiveGoal2();
    this.goalSelector.addGoal(5, this.moveToHiveGoal);
    this.moveToFlowerGoal = new FindFlowerGoal();
    this.goalSelector.addGoal(6, this.moveToFlowerGoal);
    this.goalSelector.addGoal(7, new FindPollinationTargetGoal2());
    this.goalSelector.addGoal(8, new WanderGoal());
    this.goalSelector.addGoal(9, new SwimGoal(this));
    this.targetSelector.addGoal(1, (new AngerGoal(this)).setCallsForHelp());
    this.targetSelector.addGoal(2, new AttackPlayerGoal(this));
  }

  @Override
  public boolean isHiveValid() {
    if (!this.hasHive()) {
      return false;
    } else {
      TileEntity blockEntity = this.world.getTileEntity(this.hivePos);
      return blockEntity instanceof IronBeehiveBlockEntity
              && ((IronBeehiveBlockEntity) blockEntity).isAllowedBee(this);
    }
  }

  @Override
  public final boolean isFlowers(BlockPos p_226439_1_) {
    return this.world.isBlockPresent(p_226439_1_) && world.getBlockState(p_226439_1_).getBlock().isIn(getType().beeProperties.flowers);
  }

  public boolean doesHiveHaveSpace(BlockPos pos) {
    TileEntity blockEntity = this.world.getTileEntity(pos);
    return blockEntity instanceof IronBeehiveBlockEntity && !((IronBeehiveBlockEntity) blockEntity).isFullOfBees();
  }

  protected class UpdateBeehiveGoal2 extends BeeEntity.UpdateBeehiveGoal {

    public UpdateBeehiveGoal2() {
      super();
    }

    /**
     * ()Ljava/util/List;
     */
    public List<BlockPos> getNearbyFreeHives() {
      BlockPos blockpos = new BlockPos(IronBeeEntity.this);
      PointOfInterestManager pointofinterestmanager = ((ServerWorld) world).getPointOfInterestManager();
      Stream<PointOfInterest> stream = pointofinterestmanager.func_219146_b(pointOfInterestType ->
                      pointOfInterestType == IronBeeEntity.this.getHivePoi()
                              || pointOfInterestType == BeeSourceful.Objectholders.POI.iron_beehive, blockpos,
              20, PointOfInterestManager.Status.ANY);
      return stream.map(PointOfInterest::getPos).filter(IronBeeEntity.this::doesHiveHaveSpace)
              .sorted(Comparator.comparingDouble(pos -> pos.distanceSq(blockpos))).collect(Collectors.toList());
    }
  }

  protected class FindPollinationTargetGoal2 extends BeeEntity.FindPollinationTargetGoal {

    public FindPollinationTargetGoal2(){
      super();
    }

    @Override
    public void tick() {
      applyPollinationEffect();
    }
  }

  public void applyPollinationEffect(){
    if (rand.nextInt(1) == 0) {
      for (int i = 1; i <= 2; ++i) {
        BlockPos beePosDown = (new BlockPos(IronBeeEntity.this)).down(i);
        BlockState state = world.getBlockState(beePosDown);
        Block block = state.getBlock();
        if (validFillerBlock(block)) {
          world.playEvent(2005, beePosDown, 0);
          world.setBlockState(beePosDown, getOre().getDefaultState());
          addCropCounter();
        }
      }
    }
  }

  public boolean validFillerBlock(Block block){
    return block.isIn(Tags.Blocks.STONE);
  }

  public class FindBeehiveGoal2 extends FindBeehiveGoal {

    public FindBeehiveGoal2(){
      super();
    }

    @Override
    public boolean canBeeStart() {
      return hivePos != null && !detachHome() && canEnterHive() && !this.isCloseEnough(hivePos)
              && isHiveValid();
    }
  }

  public class PollinateGoal2 extends BeeEntity.PollinateGoal {

    public PollinateGoal2(){
      super();
    }

    @Override
    public Optional<BlockPos> getFlower() {
      return this.findFlower(getFlowerPredicate(), 5.0D);
    }
  }

  public Predicate<BlockState> getFlowerPredicate(){
    return getType().beeProperties.flowerPredicate;
  }

  public final Block getOre(){
    return getType().beeProperties.ore;
  }

  public final Block getAllowedHive() {
    return getType().beeProperties.hive_block;
  }

  @Nonnull
  public final PointOfInterestType getHivePoi(){
    return getType().beeProperties.getHive();
  }

  @Override
  public BeeEntityType getType() {
    return (BeeEntityType) super.getType();
  }

  public final Item getHoneyComb(){
    return getType().beeProperties.getComb();
  }
}