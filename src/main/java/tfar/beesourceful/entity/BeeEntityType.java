package tfar.beesourceful.entity;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

public class BeeEntityType extends EntityType<IronBeeEntity> {

	public BeeProperties beeProperties;

	public BeeEntityType(IFactory<IronBeeEntity> iFactory, EntityClassification classification, boolean p_i51559_3_, boolean p_i51559_4_, boolean p_i51559_5_, boolean p_i51559_6_, ImmutableSet<Block> blockImmutableSet, EntitySize entitySize, int i1, int i2) {
		super(iFactory, classification, p_i51559_3_, p_i51559_4_, p_i51559_5_, p_i51559_6_,blockImmutableSet, entitySize,i1,i2);
	}

	public BeeEntityType(boolean immuneToFire, BeeProperties beeProperties) {
		this(IronBeeEntity::new, EntityClassification.CREATURE, true, true, immuneToFire, true, ImmutableSet.of(),EntitySize.flexible(0.7f,0.6f),5,3);
		this.beeProperties = beeProperties;
	}

}
