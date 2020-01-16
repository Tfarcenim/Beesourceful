package com.tfar.beesourceful.data.provider;

import com.tfar.beesourceful.BeeSourceful;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraftforge.common.Tags;

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
  }
}
