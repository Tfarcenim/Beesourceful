package tfar.beesourceful.entity;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;

import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

public class BeeEntityType extends EntityType<IronBeeEntity> {

	public final BeeProperties beeProperties;

	public BeeEntityType(IFactory<IronBeeEntity> p_i51559_1_, EntityClassification p_i51559_2_, boolean serializable, boolean summonable, boolean immuneToFire, boolean spawnableFarFromPlayer, EntitySize size, BeeProperties beeProperties) {
		super(p_i51559_1_, p_i51559_2_, serializable, summonable, immuneToFire, spawnableFarFromPlayer, size);
		this.beeProperties = beeProperties;
	}

	public BeeEntityType(IFactory<IronBeeEntity> p_i51559_1_, EntityClassification p_i51559_2_, boolean p_i51559_3_, boolean p_i51559_4_, boolean p_i51559_5_, boolean p_i51559_6_, EntitySize p_i51559_7_, Predicate velocityUpdateSupplier, ToIntFunction trackingRangeSupplier, ToIntFunction updateIntervalSupplier, BiFunction customClientFactory, BeeProperties beeProperties) {
		super(p_i51559_1_, p_i51559_2_, p_i51559_3_, p_i51559_4_, p_i51559_5_, p_i51559_6_, p_i51559_7_, velocityUpdateSupplier, trackingRangeSupplier, updateIntervalSupplier, customClientFactory);
		this.beeProperties = beeProperties;
	}

	public BeeEntityType(boolean immuneToFire, BeeProperties beeProperties) {
		this(IronBeeEntity::new, EntityClassification.CREATURE, true, true, immuneToFire, true, EntitySize.flexible(0.7f,0.6f),beeProperties);
	}
}
