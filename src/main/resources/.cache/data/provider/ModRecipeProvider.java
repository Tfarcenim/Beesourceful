package tfar.beesourceful.data.provider;

import com.google.common.collect.Lists;
import tfar.beesourceful.BeeSourceful;
import tfar.beesourceful.data.CentrifugeRecipeBuilder;
import tfar.beesourceful.data.ShapedRecipeBuilderNoCriteria;
import tfar.beesourceful.data.ShapelessRecipeBuilderNoCriteria;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
  public ModRecipeProvider(DataGenerator p_i48262_1_) {
    super(p_i48262_1_);
  }

  @Override
  protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilderNoCriteria.shapedRecipe(BeeSourceful.Objectholders.iron_beehive)
            .key('a', Tags.Items.INGOTS_IRON)
            .key('b', Blocks.field_226906_mb_)
            .patternLine("aaa").patternLine("aba").patternLine("aaa")
            .build(consumer);
    ShapedRecipeBuilderNoCriteria.shapedRecipe(BeeSourceful.Objectholders.centrifuge)
            .key('a', Tags.Items.INGOTS_IRON)
            .key('b', Blocks.GRINDSTONE)
            .patternLine("aaa").patternLine("aba").patternLine("aaa")
            .build(consumer);

    BeeSourceful.RegistryEvents.items.stream().filter(item -> item.getRegistryName().getPath().endsWith("honeycomb"))
            .forEach(item -> {
              Item honeycomb_block = ForgeRegistries.ITEMS.getValue(new ResourceLocation(BeeSourceful.MODID, item.getRegistryName().getPath() + "_block"));
                      ShapedRecipeBuilderNoCriteria
                              .shapedRecipe(honeycomb_block)
                              .key('a', item)
                              .patternLine("aaa").patternLine("aaa").patternLine("aaa")
                              .build(consumer,honeycomb_block.getRegistryName());

                      ShapelessRecipeBuilderNoCriteria
                              .shapelessRecipe(item,9)
                              .addIngredient(honeycomb_block)
                              .build(consumer,item.getRegistryName());

                    }
            );

    centrifugeRecipes(consumer);

  }

  public void centrifugeRecipes(Consumer<IFinishedRecipe> consumer) {
    CentrifugeRecipeBuilder.centrifugeRecipe(
            Ingredient.fromTag(new ItemTags.Wrapper(new ResourceLocation("forge", "honeycombs/iron"))),
            Lists.newArrayList(
                    Pair.of(new ItemStack(Items.IRON_INGOT), 1d),
                    Pair.of(new ItemStack(BeeSourceful.Objectholders.beeswax), 1d)),new FluidStack(BeeSourceful.honey.get(),200), 200)
            .build(consumer, centWrapper("iron_honeycomb"));

    CentrifugeRecipeBuilder.centrifugeRecipe(
            Ingredient.fromTag(new ItemTags.Wrapper(new ResourceLocation("forge", "honeycombs/gold"))),
            Lists.newArrayList(
                    Pair.of(new ItemStack(Items.GOLD_INGOT), 1d),
                    Pair.of(new ItemStack(BeeSourceful.Objectholders.beeswax), 1d)),new FluidStack(BeeSourceful.honey.get(),200),  200)
            .build(consumer, centWrapper("gold_honeycomb"));

    CentrifugeRecipeBuilder.centrifugeRecipe(
            Ingredient.fromTag(new ItemTags.Wrapper(new ResourceLocation("forge", "honeycombs/diamond"))),
            Lists.newArrayList(
                    Pair.of(new ItemStack(Items.DIAMOND), 1d),
                    Pair.of(new ItemStack(BeeSourceful.Objectholders.beeswax), 1d)),new FluidStack(BeeSourceful.honey.get(),200),  200)
            .build(consumer, centWrapper("diamond_honeycomb"));

    CentrifugeRecipeBuilder.centrifugeRecipe(
            Ingredient.fromTag(new ItemTags.Wrapper(new ResourceLocation("forge", "honeycombs/lapis"))),
            Lists.newArrayList(
                    Pair.of(new ItemStack(Items.LAPIS_LAZULI, 6), 1d),
                    Pair.of(new ItemStack(BeeSourceful.Objectholders.beeswax), 1d)),new FluidStack(BeeSourceful.honey.get(),200),  200)
            .build(consumer, centWrapper("lapis_honeycomb"));

    CentrifugeRecipeBuilder.centrifugeRecipe(
            Ingredient.fromTag(new ItemTags.Wrapper(new ResourceLocation("forge", "honeycombs/redstone"))),
            Lists.newArrayList(
                    Pair.of(new ItemStack(Items.REDSTONE, 4), 1d),
                    Pair.of(new ItemStack(BeeSourceful.Objectholders.beeswax), 1d)),new FluidStack(BeeSourceful.honey.get(),200),  200)
            .build(consumer, centWrapper("redstone_honeycomb"));

    CentrifugeRecipeBuilder.centrifugeRecipe(
            Ingredient.fromTag(new ItemTags.Wrapper(new ResourceLocation("forge", "honeycombs/emerald"))),
            Lists.newArrayList(
                    Pair.of(new ItemStack(Items.EMERALD), 1d),
                    Pair.of(new ItemStack(BeeSourceful.Objectholders.beeswax), 1d)),new FluidStack(BeeSourceful.honey.get(),200),  200)
            .build(consumer, centWrapper("emerald_honeycomb"));

    CentrifugeRecipeBuilder.centrifugeRecipe(
            Ingredient.fromTag(new ItemTags.Wrapper(new ResourceLocation("forge", "honeycombs/quartz"))),
            Lists.newArrayList(
                    Pair.of(new ItemStack(Items.QUARTZ), 1d),
                    Pair.of(new ItemStack(BeeSourceful.Objectholders.beeswax), 1d)),new FluidStack(BeeSourceful.honey.get(),200),  200)
            .build(consumer, centWrapper("quartz_honeycomb"));

    CentrifugeRecipeBuilder.centrifugeRecipe(
            Ingredient.fromTag(new ItemTags.Wrapper(new ResourceLocation("forge", "honeycombs/ender"))),
            Lists.newArrayList(
                    Pair.of(new ItemStack(Items.ENDER_PEARL), 1d),
                    Pair.of(new ItemStack(BeeSourceful.Objectholders.beeswax), 1d)), new FluidStack(BeeSourceful.honey.get(),200), 200)
            .build(consumer, centWrapper("ender_honeycomb"));
  }

  public ResourceLocation centWrapper(String item){
    return new ResourceLocation(BeeSourceful.MODID,item + "_centrifuge");
  }
}
