package tfar.beesourceful.blockentity;

import tfar.beesourceful.BeeSourceful;
import tfar.beesourceful.block.IronBeehiveBlock;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import tfar.beesourceful.entity.IronBeeEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.BeehiveTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class IronBeehiveBlockEntity extends BeehiveTileEntity {

  public List<Item> honeycombs = new ArrayList<>();

  @Nonnull
  @Override
  public TileEntityType<?> getType() {
    return BeeSourceful.Objectholders.BlockEntities.iron_beehive;
  }

  @Override
  public void tick() {
    if (getBlockState().get(IronBeehiveBlock.TICK)) {
      super.tick();
    }
  }

  public void tryEnterHive(Entity bee, boolean hasNectar, int ticksInHive) {
    if (!isFullOfBees()) {
      bee.removePassengers();
      CompoundNBT nbt = new CompoundNBT();
      bee.writeUnlessPassenger(nbt);
      this.bees.add(new BeehiveTileEntity.Bee(nbt, ticksInHive, hasNectar ? 2400 : 600));
      if (this.world != null) {
        if (bee instanceof IronBeeEntity) {
          IronBeeEntity bee1 = (IronBeeEntity)bee;
          if (bee1.hasFlower() && (!this.hasFlowerPos() || this.world.rand.nextBoolean())) {
            this.flowerPos = bee1.getFlowerPos();
          }
        }
        BlockPos pos = this.getPos();
        this.world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_BEEHIVE_ENTER, SoundCategory.BLOCKS, 1.0F, 1.0F);
      }
      if (bee.getType() == BeeSourceful.Objectholders.Entities.ender_bee){
        /*this.world.addParticle(ParticleTypes.PORTAL, bee.getParticleX(0.5D),
                bee.getRandomBodyY() - 0.25D, bee.getParticleZ(0.5D),
                (world.rand.nextDouble() - 0.5D) * 2.0D, -world.rand.nextDouble(),
                (world.rand.nextDouble() - 0.5D) * 2.0D);*/
      }
      bee.remove();
    }
  }

  public boolean shouldStayInHive(BlockState state, State beehiveState){
    return (this.world.isNightTime() || this.world.isRaining()) && beehiveState != BeehiveTileEntity.State.EMERGENCY;
  }

  @Override
  public boolean isFullOfBees() {
    return bees.size() >= 5;
  }

  public Item getResourceHoneyComb(){
    Item honeycomb = honeycombs.get(honeycombs.size()-1);
    honeycombs.remove(honeycomb);
    return honeycomb;
  }

  public boolean hasCombs(){
    return honeycombs.size() > 0;
  }

  public boolean isAllowedBee(IronBeeEntity bee){
    Block hive = getBlockState().getBlock();
    return hive == BeeSourceful.Objectholders.iron_beehive || bee.getAllowedHive() == getBlockState().getBlock();
  }

  @Override
  public void read(BlockState state,CompoundNBT nbt) {
    super.read(state,nbt);
    if (nbt.contains("Honeycombs")){
      CompoundNBT combs = (CompoundNBT) nbt.get("Honeycombs");
      int i = 0;
      while (combs.contains(String.valueOf(i))){
        honeycombs.add(ForgeRegistries.ITEMS.getValue(new ResourceLocation(combs.getString(String.valueOf(i)))));
        i++;
      }
      ForgeRegistries.ITEMS.getValue(new ResourceLocation(nbt.getString("Honeycomb")));
    }
  }

  @Nonnull
  @Override
  public CompoundNBT write(CompoundNBT nbt) {
    super.write(nbt);
    if (!honeycombs.isEmpty()){
      CompoundNBT combs = new CompoundNBT();
      for (int i = 0; i < honeycombs.size();i++){
        combs.putString(String.valueOf(i),honeycombs.get(i).getRegistryName().toString());
      }
      nbt.put("Honeycombs",combs);
    }
    return nbt;
  }
}
