package tfar.beesourceful.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.PointOfInterestType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ReplaceBlockConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tfar.beesourceful.BeeSourceful;
import tfar.beesourceful.feature.OreBeeNestFeature;
import tfar.beesourceful.util.Shortcuts;
import tfar.extratags.api.ExtraTagRegistry;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class BeeProperties {


	public final GenerationStage.Decoration generation_stage;
	public final Predicate<Biome> allowed_biomes;
	private final Supplier<PointOfInterestType> poi;
	private final Supplier<Item> comb;
	private Item combCache;
	private PointOfInterestType type;
	public OreBeeNestFeature feature;
	public BlockState replace_block;

	public Block ore;
	public String name;

	private final Supplier<Block> hive;
	public Predicate<Biome> allowedBiomes;
	public final ITag.INamedTag<Block> flowers;
	private Block hiveCache;

	public BeeProperties(GenerationStage.Decoration stage, Predicate<Biome> allowed_biomes, Supplier<Item> comb, Supplier<PointOfInterestType> poi, OreBeeNestFeature feature, BlockState replace_block, Supplier<Block> hive, Predicate<Biome> allowedBiomes, Block ore, String name, ITag.INamedTag<Block> flowers){
		this.generation_stage = stage;
		this.allowed_biomes = allowed_biomes;
		this.comb = comb;
		this.poi = poi;
		this.feature = feature;
		this.replace_block = replace_block;
		this.hive = hive;
		this.allowedBiomes = allowedBiomes;
		this.ore = ore;
		this.name = name;
		this.flowers = flowers;
	}

	public PointOfInterestType getPOI() {
		if (type == null)type = poi.get();
		return type;
	}

	public Block getHive() {
		if (hiveCache == null)hiveCache = hive.get();
		return hiveCache;
	}

	public Item getComb() {
		if (combCache == null)combCache = comb.get();
		return combCache;
	}

	public static final Map<ResourceLocation, BeeProperties> bee_registry = new HashMap<>();

	public static final Logger logger = LogManager.getLogger();

	public static Gson g = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

	public static void writeDefaultBees() {
		File[] files = getResourceFolderFiles("bees");
		Path configDir = FMLPaths.CONFIGDIR.get();
		Path path = Paths.get(configDir+"/"+ BeeSourceful.MODID);
		try {
			Files.createDirectories(path);
		} catch (IOException e) {
			e.printStackTrace();
		}

		String content = null;
		for (File file : files) {
			try {
				FileReader fileReader = new FileReader(file);
				content =  g.toJson(new JsonParser().parse(fileReader));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			String dir = "config/beesourceful/";
			String name = file.getName();
			File toWrite = new File(dir+name);
			FileWriter writer = null;
			try {
				writer = new FileWriter(toWrite);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				writer.write(content);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void loadBee(File file) {
		FileReader reader = null;
		try {
			reader = new FileReader(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		JsonObject jsonObject = g.fromJson(reader, JsonObject.class).getAsJsonObject("bee_properties");
		ResourceLocation id = new ResourceLocation(JSONUtils.getString(jsonObject,"id"));
		Supplier<BeeEntityType> entityTypeSupplier = () -> (BeeEntityType) Registry.ENTITY_TYPE.getValue(id).orElseThrow(IllegalStateException::new);
		GenerationStage.Decoration stage = GenerationStage.Decoration.UNDERGROUND_ORES;
		String biomeString = JSONUtils.getString(jsonObject,"biome");
		Supplier<Item> comb = () -> Registry.ITEM.getOrDefault(new ResourceLocation(JSONUtils.getString(jsonObject,"comb")));
		Predicate<Biome> allowed_biomes;
		if ("*".equals(biomeString)) allowed_biomes = Shortcuts.TRUE;
		else {
			ITag.INamedTag<Biome> tag = ExtraTagRegistry.biome(biomeString);
			allowed_biomes = tag::contains;
		}

		ITag.INamedTag<Block> flower_tag = BlockTags.makeWrapperTag(new ResourceLocation(JSONUtils.getString(jsonObject, "flower")).toString());
		String name = StringUtils.substring(file.getName(),0,file.getName().length() - 5);

		Block ore = Registry.BLOCK.getOrDefault(new ResourceLocation(JSONUtils.getString(jsonObject,"ore")));
		Supplier<PointOfInterestType> poi = () -> Registry.POINT_OF_INTEREST_TYPE.getValue(new ResourceLocation(id.getNamespace(),id.getPath()+"_nest")).orElseThrow(IllegalStateException::new);
		OreBeeNestFeature feature = new OreBeeNestFeature(ReplaceBlockConfig.field_236604_a_,entityTypeSupplier);
		BlockState replace_block = Blocks.STONE.getDefaultState();
		Supplier<Block> hiveSupplier = () -> Registry.BLOCK.getValue(new ResourceLocation(id.getNamespace() ,id.getPath() + "_nest"))
						.orElseThrow(() -> new IllegalStateException(id.getPath()+"_nest"));
		bee_registry.put(id,new BeeProperties(stage,allowed_biomes,comb,poi,feature,replace_block,hiveSupplier,allowed_biomes,ore,name,
						flower_tag));

		logger.info("loaded " + id + " bee");
	}

	public static void loadCustomBees() {
		writeDefaultBees();
		Path configDir = FMLPaths.CONFIGDIR.get();
		Path path = Paths.get(configDir+"/"+BeeSourceful.MODID);
		File[] files1 = path.toFile().listFiles();
		if (files1 == null) throw new IllegalStateException("No Bees Loaded!  Please check the beesourceful config");
		for (File file : files1) {
			loadBee(file);
		}
	}

	private static File[] getResourceFolderFiles(String folder) {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		URL url = loader.getResource(folder);
		String path = url.getPath();
		return new File(path).listFiles();
	}

}
