package tfar.beesourceful.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.tags.Tag;
import net.minecraft.village.PointOfInterestType;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class BeeProperties {

	private final Supplier<PointOfInterestType> hive;
	private PointOfInterestType hiveCache;
	public final Block hive_block;
	public final Block ore;
	private final Supplier<Item> comb;
	private Item combCache;
	public final Tag<Block> flowers;
	public final Predicate<BlockState> flowerPredicate;

	public BeeProperties(Supplier<PointOfInterestType> hive, Block hive_block, Block ore, Supplier<Item> comb, Tag<Block> flowers){
		this.hive = hive;
		this.hive_block = hive_block;
		this.ore = ore;
		this.comb = comb;
		this.flowers = flowers;
		flowerPredicate = state -> state.isIn(flowers);
	}

	public PointOfInterestType getHive() {
		if (hiveCache == null)hiveCache = hive.get();
		return hiveCache;
	}

	public Item getComb() {
		if (combCache == null)combCache = comb.get();
		return combCache;
	}
}
