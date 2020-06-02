package com.tfar.beesourceful.util;

import com.tfar.beesourceful.BeeSourceful;
import com.tfar.beesourceful.BeeSourceful.Objectholders;
import com.tfar.beesourceful.feature.OreBeeNestFeature;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import com.tfar.beesourceful.entity.IronBeeEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.ReplaceBlockConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class BeeType {

	public static final Map<ResourceLocation, BeeType> bee_registry = new HashMap<>();

	static {
		bee_registry.put(new ResourceLocation(BeeSourceful.MODID,"iron"), new BeeType(new ResourceLocation(BeeSourceful.MODID,"iron"), () -> Objectholders.Entities.iron_bee,
						GenerationStage.Decoration.UNDERGROUND_ORES,
						Shortcuts.TRUE,
						Blocks.STONE.getDefaultState(),
						() -> Objectholders.iron_bee_nest,
						new OreBeeNestFeature(ReplaceBlockConfig::deserialize, () -> Objectholders.Entities.iron_bee)));

		bee_registry.put(new ResourceLocation(BeeSourceful.MODID,"gold"), new BeeType(new ResourceLocation(BeeSourceful.MODID,"gold"), () -> Objectholders.Entities.gold_bee,
						GenerationStage.Decoration.UNDERGROUND_ORES,
						Shortcuts.TRUE, Blocks.STONE.getDefaultState(),
						() -> Objectholders.gold_bee_nest,
						new OreBeeNestFeature(ReplaceBlockConfig::deserialize, () -> Objectholders.Entities.gold_bee)));

		bee_registry.put(new ResourceLocation(BeeSourceful.MODID,"redstone"), new BeeType(new ResourceLocation(BeeSourceful.MODID,"redstone"), () -> Objectholders.Entities.redstone_bee,
						GenerationStage.Decoration.UNDERGROUND_ORES,
						Shortcuts.TRUE, Blocks.STONE.getDefaultState(),
						() -> Objectholders.redstone_bee_nest,
						new OreBeeNestFeature(ReplaceBlockConfig::deserialize, () -> Objectholders.Entities.redstone_bee)));

		bee_registry.put(new ResourceLocation(BeeSourceful.MODID,"ender"), new BeeType(new ResourceLocation(BeeSourceful.MODID,"ender"), () -> Objectholders.Entities.ender_bee,
						GenerationStage.Decoration.TOP_LAYER_MODIFICATION,
						Shortcuts.TRUE, Blocks.END_STONE.getDefaultState(),
						() -> Objectholders.ender_bee_nest,
						new OreBeeNestFeature(ReplaceBlockConfig::deserialize, () -> Objectholders.Entities.ender_bee)));

		bee_registry.put(new ResourceLocation(BeeSourceful.MODID,"quartz"), new BeeType(new ResourceLocation(BeeSourceful.MODID,"quartz"), () -> Objectholders.Entities.quartz_bee,
						GenerationStage.Decoration.UNDERGROUND_ORES,
						biome -> biome.getCategory() == Biome.Category.NETHER,
						Blocks.NETHERRACK.getDefaultState(),
						() -> Objectholders.ender_bee_nest,
						new OreBeeNestFeature(ReplaceBlockConfig::deserialize, () -> Objectholders.Entities.quartz_bee)));

		bee_registry.put(new ResourceLocation(BeeSourceful.MODID,"lapis"),new BeeType(new ResourceLocation(BeeSourceful.MODID,"lapis"), () -> Objectholders.Entities.lapis_bee,
						GenerationStage.Decoration.UNDERGROUND_ORES,
						Shortcuts.TRUE, Blocks.STONE.getDefaultState(),
						() -> Objectholders.lapis_bee_nest,
						new OreBeeNestFeature(ReplaceBlockConfig::deserialize, () -> Objectholders.Entities.lapis_bee)));

		bee_registry.put(new ResourceLocation(BeeSourceful.MODID,"emerald"),new BeeType(new ResourceLocation(BeeSourceful.MODID,"emerald"), () -> Objectholders.Entities.emerald_bee,
						GenerationStage.Decoration.UNDERGROUND_ORES,
						biome -> biome.getCategory() == Biome.Category.EXTREME_HILLS,
						Blocks.STONE.getDefaultState(),
						() -> Objectholders.emerald_bee_nest,
						new OreBeeNestFeature(ReplaceBlockConfig::deserialize, () -> Objectholders.Entities.emerald_bee)));

		bee_registry.put(new ResourceLocation(BeeSourceful.MODID,"diamond"),new BeeType(new ResourceLocation(BeeSourceful.MODID,"diamond"), () -> Objectholders.Entities.diamond_bee,
						GenerationStage.Decoration.UNDERGROUND_ORES,
						Shortcuts.TRUE,

						Blocks.STONE.getDefaultState(),
						() -> Objectholders.diamond_bee_nest,
						new OreBeeNestFeature(ReplaceBlockConfig::deserialize, () -> Objectholders.Entities.diamond_bee)));


	}

	public final ResourceLocation id;
	public final Supplier<EntityType<? extends IronBeeEntity>> beeSupplier;
	public final GenerationStage.Decoration generation_stage;
	public final Predicate<Biome> allowed_biomes;
	public final BlockState replace_block;
	public final Supplier<Block> beehive;
	public final Feature<ReplaceBlockConfig> feature;

	public BeeType(ResourceLocation id, Supplier<EntityType<? extends IronBeeEntity>> beeSupplier,
					GenerationStage.Decoration generation_stage,
					Predicate<Biome> allowed_biomes,
					BlockState replace_block,
					Supplier<Block> beehive, Feature<ReplaceBlockConfig> feature) {
		this.id = id;
		this.beeSupplier = beeSupplier;
		this.generation_stage = generation_stage;
		this.allowed_biomes = allowed_biomes;
		this.replace_block = replace_block;
		this.beehive = beehive;
		this.feature = feature;
	}
}
