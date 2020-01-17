package com.tfar.beesourceful.data.provider;

import com.google.common.collect.Lists;
import com.tfar.beesourceful.BeeSourceful;
import com.tfar.beesourceful.data.CentrifugeRecipeBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
  public ModRecipeProvider(DataGenerator p_i48262_1_) {
    super(p_i48262_1_);
  }

  @Override
  protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shapedRecipe(BeeSourceful.Objectholders.iron_beehive)
            .key('a', Tags.Items.INGOTS_IRON)
            .key('b', Blocks.field_226906_mb_)
            .patternLine("aaa").patternLine("aba").patternLine("aaa")
            .addCriterion("has_iron", this.hasItem(Tags.Items.INGOTS_IRON))
            .addCriterion("has_hive", this.hasItem(Blocks.field_226906_mb_))
            .build(consumer);
    ShapedRecipeBuilder.shapedRecipe(BeeSourceful.Objectholders.centrifuge)
            .key('a', Tags.Items.INGOTS_IRON)
            .key('b', Blocks.GRINDSTONE)
            .patternLine("aaa").patternLine("aba").patternLine("aaa")
            .addCriterion("has_iron", this.hasItem(Tags.Items.INGOTS_IRON))
            .addCriterion("has_hive", this.hasItem(Blocks.field_226906_mb_))
            .build(consumer);

    CentrifugeRecipeBuilder.centrifugeRecipe(
            Ingredient.fromTag(new ItemTags.Wrapper(new ResourceLocation("forge", "honeycombs/iron"))),
            Lists.newArrayList(
                    Pair.of(new ItemStack(Items.IRON_INGOT), 1d),
                    Pair.of(new ItemStack(BeeSourceful.Objectholders.beeswax), 1d)), 200)
            .build(consumer, BeeSourceful.Objectholders.iron_honeycomb.getRegistryName());

    CentrifugeRecipeBuilder.centrifugeRecipe(
            Ingredient.fromTag(new ItemTags.Wrapper(new ResourceLocation("forge", "honeycombs/gold"))),
            Lists.newArrayList(
                    Pair.of(new ItemStack(Items.GOLD_INGOT), 1d),
                    Pair.of(new ItemStack(BeeSourceful.Objectholders.beeswax), 1d)), 200)
            .build(consumer, BeeSourceful.Objectholders.gold_honeycomb.getRegistryName());

    CentrifugeRecipeBuilder.centrifugeRecipe(
            Ingredient.fromTag(new ItemTags.Wrapper(new ResourceLocation("forge", "honeycombs/diamond"))),
            Lists.newArrayList(
                    Pair.of(new ItemStack(Items.DIAMOND), 1d),
                    Pair.of(new ItemStack(BeeSourceful.Objectholders.beeswax), 1d)), 200)
            .build(consumer, BeeSourceful.Objectholders.diamond_honeycomb.getRegistryName());

    CentrifugeRecipeBuilder.centrifugeRecipe(
            Ingredient.fromTag(new ItemTags.Wrapper(new ResourceLocation("forge", "honeycombs/lapis"))),
            Lists.newArrayList(
                    Pair.of(new ItemStack(Items.LAPIS_LAZULI,6), 1d),
                    Pair.of(new ItemStack(BeeSourceful.Objectholders.beeswax), 1d)), 200)
            .build(consumer, BeeSourceful.Objectholders.lapis_honeycomb.getRegistryName());

    CentrifugeRecipeBuilder.centrifugeRecipe(
            Ingredient.fromTag(new ItemTags.Wrapper(new ResourceLocation("forge", "honeycombs/redstone"))),
            Lists.newArrayList(
                    Pair.of(new ItemStack(Items.REDSTONE,4), 1d),
                    Pair.of(new ItemStack(BeeSourceful.Objectholders.beeswax), 1d)), 200)
            .build(consumer, BeeSourceful.Objectholders.redstone_honeycomb.getRegistryName());

    CentrifugeRecipeBuilder.centrifugeRecipe(
            Ingredient.fromTag(new ItemTags.Wrapper(new ResourceLocation("forge", "honeycombs/emerald"))),
            Lists.newArrayList(
                    Pair.of(new ItemStack(Items.EMERALD), 1d),
                    Pair.of(new ItemStack(BeeSourceful.Objectholders.beeswax), 1d)), 200)
            .build(consumer, BeeSourceful.Objectholders.emerald_honeycomb.getRegistryName());

    CentrifugeRecipeBuilder.centrifugeRecipe(
            Ingredient.fromTag(new ItemTags.Wrapper(new ResourceLocation("forge", "honeycombs/quartz"))),
            Lists.newArrayList(
                    Pair.of(new ItemStack(Items.QUARTZ), 1d),
                    Pair.of(new ItemStack(BeeSourceful.Objectholders.beeswax), 1d)), 200)
            .build(consumer, BeeSourceful.Objectholders.quartz_honeycomb.getRegistryName());

    CentrifugeRecipeBuilder.centrifugeRecipe(
            Ingredient.fromTag(new ItemTags.Wrapper(new ResourceLocation("forge", "honeycombs/ender"))),
            Lists.newArrayList(
                    Pair.of(new ItemStack(Items.ENDER_PEARL), 1d),
                    Pair.of(new ItemStack(BeeSourceful.Objectholders.beeswax), 1d)), 200)
            .build(consumer, BeeSourceful.Objectholders.ender_honeycomb.getRegistryName());
  }
}
