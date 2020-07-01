package tfar.beesourceful.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.entity.EntityType;
import net.minecraft.tags.Tag;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.loading.FMLPaths;
import tfar.beesourceful.BeeSourceful;
import tfar.beesourceful.BeeSourceful.Objectholders;
import tfar.beesourceful.entity.BeeEntityType;
import tfar.beesourceful.feature.OreBeeNestFeature;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.ReplaceBlockConfig;
import tfar.extratags.api.tagtypes.BiomeTags;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class BeeType {

	public static final Map<ResourceLocation, BeeType> bee_registry = new HashMap<>();

	public static Gson g = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();


	static {
		bee_registry.put(new ResourceLocation(BeeSourceful.MODID,"iron"), new BeeType(new ResourceLocation(BeeSourceful.MODID,"iron"), () -> Objectholders.Entities.iron_bee,
						GenerationStage.Decoration.UNDERGROUND_ORES,
						Shortcuts.TRUE,
						Blocks.STONE.getDefaultState(),
						new OreBeeNestFeature(ReplaceBlockConfig::deserialize, () -> Objectholders.Entities.iron_bee)));

		bee_registry.put(new ResourceLocation(BeeSourceful.MODID,"gold"), new BeeType(new ResourceLocation(BeeSourceful.MODID,"gold"), () -> Objectholders.Entities.gold_bee,
						GenerationStage.Decoration.UNDERGROUND_ORES,
						Shortcuts.TRUE, Blocks.STONE.getDefaultState(),
						new OreBeeNestFeature(ReplaceBlockConfig::deserialize, () -> Objectholders.Entities.gold_bee)));

		bee_registry.put(new ResourceLocation(BeeSourceful.MODID,"redstone"), new BeeType(new ResourceLocation(BeeSourceful.MODID,"redstone"), () -> Objectholders.Entities.redstone_bee,
						GenerationStage.Decoration.UNDERGROUND_ORES,
						Shortcuts.TRUE, Blocks.STONE.getDefaultState(),
						new OreBeeNestFeature(ReplaceBlockConfig::deserialize, () -> Objectholders.Entities.redstone_bee)));

		bee_registry.put(new ResourceLocation(BeeSourceful.MODID,"ender"), new BeeType(new ResourceLocation(BeeSourceful.MODID,"ender"), () -> Objectholders.Entities.ender_bee,
						GenerationStage.Decoration.TOP_LAYER_MODIFICATION,
						Shortcuts.TRUE, Blocks.END_STONE.getDefaultState(),
						new OreBeeNestFeature(ReplaceBlockConfig::deserialize, () -> Objectholders.Entities.ender_bee)));

		bee_registry.put(new ResourceLocation(BeeSourceful.MODID,"quartz"), new BeeType(new ResourceLocation(BeeSourceful.MODID,"quartz"), () -> Objectholders.Entities.quartz_bee,
						GenerationStage.Decoration.UNDERGROUND_ORES,
						biome -> biome.getCategory() == Biome.Category.NETHER,
						Blocks.NETHERRACK.getDefaultState(),
						new OreBeeNestFeature(ReplaceBlockConfig::deserialize, () -> Objectholders.Entities.quartz_bee)));

		bee_registry.put(new ResourceLocation(BeeSourceful.MODID,"lapis"),new BeeType(new ResourceLocation(BeeSourceful.MODID,"lapis"), () -> Objectholders.Entities.lapis_bee,
						GenerationStage.Decoration.UNDERGROUND_ORES,
						Shortcuts.TRUE, Blocks.STONE.getDefaultState(),
						new OreBeeNestFeature(ReplaceBlockConfig::deserialize, () -> Objectholders.Entities.lapis_bee)));

		bee_registry.put(new ResourceLocation(BeeSourceful.MODID,"emerald"),new BeeType(new ResourceLocation(BeeSourceful.MODID,"emerald"), () -> Objectholders.Entities.emerald_bee,
						GenerationStage.Decoration.UNDERGROUND_ORES,
						biome -> biome.getCategory() == Biome.Category.EXTREME_HILLS,
						Blocks.STONE.getDefaultState(),
						new OreBeeNestFeature(ReplaceBlockConfig::deserialize, () -> Objectholders.Entities.emerald_bee)));

		bee_registry.put(new ResourceLocation(BeeSourceful.MODID,"diamond"),new BeeType(new ResourceLocation(BeeSourceful.MODID,"diamond"), () -> Objectholders.Entities.diamond_bee,
						GenerationStage.Decoration.UNDERGROUND_ORES,
						Shortcuts.TRUE,
						Blocks.STONE.getDefaultState(),
						new OreBeeNestFeature(ReplaceBlockConfig::deserialize, () -> Objectholders.Entities.diamond_bee)));


	}

	public static void loadCustomBees() {
		createDefaultBees();
		Path configDir = FMLPaths.CONFIGDIR.get();
		Path path = Paths.get(configDir+"/"+BeeSourceful.MODID);
		try {
			Files.createDirectories(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		File[] files = path.toFile().listFiles();
		for (File file : files) {
			FileReader reader = null;
			try {
				reader = new FileReader(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			JsonObject jsonObject = g.fromJson(reader, JsonObject.class);
			ResourceLocation id = new ResourceLocation(JSONUtils.getString(jsonObject,"id"));
			Supplier<EntityType<?>> entityTypeSupplier = () -> Registry.ENTITY_TYPE.getOrDefault(id);
			GenerationStage.Decoration stage = GenerationStage.Decoration.UNDERGROUND_ORES;
			String biomeString = JSONUtils.getString(jsonObject,"biome");
			Predicate<Biome> allowed_biomes;
			if ("all".equals(biomeString)) allowed_biomes = Shortcuts.TRUE;
			else {
				Tag<Biome> tag = BiomeTags.makeWrapperTag(new ResourceLocation(biomeString));
				allowed_biomes = tag::contains;
			}
			BlockState replace = Blocks.STONE.getDefaultState();
			bee_registry.put(id,new BeeType(id,entityTypeSupplier,stage,allowed_biomes,replace,))
		}
	}

	private static File[] getResourceFolderFiles(String folder) {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		URL url = loader.getResource(folder);
		String path = url.getPath();
		return new File(path).listFiles();
	}

	public static void createDefaultBees() {
		File[] files = getResourceFolderFiles("bees");
		for (File file : files){
			System.out.println(file.getName());
		}
	}

	public final ResourceLocation id;
	private final Supplier<BeeEntityType> beeSupplier;
	private BeeEntityType bee;
	public final GenerationStage.Decoration generation_stage;
	public final Predicate<Biome> allowed_biomes;
	public final BlockState replace_block;
	public final Feature<ReplaceBlockConfig> feature;

	public BeeType(ResourceLocation id, Supplier<BeeEntityType> beeSupplier,
								 GenerationStage.Decoration generation_stage,
								 Predicate<Biome> allowed_biomes,
								 BlockState replace_block,
								 Feature<ReplaceBlockConfig> feature) {
		this.id = id;
		this.beeSupplier = beeSupplier;
		this.generation_stage = generation_stage;
		this.allowed_biomes = allowed_biomes;
		this.replace_block = replace_block;
		this.feature = feature;
	}

	public BeeEntityType getBee() {
		if (bee == null)
			bee = beeSupplier.get();
		return bee;
	}
}
