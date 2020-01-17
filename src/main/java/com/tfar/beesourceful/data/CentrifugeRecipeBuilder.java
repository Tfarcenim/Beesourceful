package com.tfar.beesourceful.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tfar.beesourceful.BeeSourceful;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class CentrifugeRecipeBuilder {
  private final List<Pair<ItemStack,Double>> results;
  private final Ingredient input1;
  private final IRecipeSerializer<?> serializer;
  private final int time;

  public CentrifugeRecipeBuilder(IRecipeSerializer<?> p_i50787_1_, Ingredient input1,
                                 List<Pair<ItemStack, Double>> results, int time) {
    this.serializer = p_i50787_1_;
    this.results = results;
    this.input1 = input1;
    this.time = time;
  }

  public static CentrifugeRecipeBuilder centrifugeRecipe(Ingredient ingredient, List<Pair<ItemStack, Double>> results,int time) {
    return new CentrifugeRecipeBuilder(BeeSourceful.Objectholders.Recipes.centrifuge, ingredient, results, time);
  }

  public void build(Consumer<IFinishedRecipe> consumer, String id) {
      this.build(consumer, new ResourceLocation(id));
  }

  public void build(Consumer<IFinishedRecipe> consumer, ResourceLocation id) {
    consumer.accept(new CentrifugeRecipeBuilder.Result(id, this.serializer, this.input1, this.results,this.time));
  }

  public static class Result implements IFinishedRecipe {
    private final ResourceLocation id;
    private final Ingredient ingredient;
    private final List<Pair<ItemStack,Double>> results;
    private final int time;
    private final IRecipeSerializer<?> serializer;

    public Result(ResourceLocation id, IRecipeSerializer<?> serializer, Ingredient input, List<Pair<ItemStack,Double>> results,int time) {
      this.id = id;
      this.serializer = serializer;
      this.ingredient = input;
      this.results = results;
      this.time = time;
    }

    public void serialize(JsonObject json) {

      json.add("ingredient", this.ingredient.serialize());

      JsonArray jsonArray = new JsonArray();
      results.forEach(itemStackDoublePair -> {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("item", itemStackDoublePair.getLeft().getItem().getRegistryName().toString());
        if (itemStackDoublePair.getLeft().getCount() > 1)
          jsonObject.addProperty("count", itemStackDoublePair.getLeft().getCount());
        if (itemStackDoublePair.getRight() < 1)
          jsonObject.addProperty("chance", itemStackDoublePair.getLeft().getCount());
        jsonArray.add(jsonObject);
      });
      json.add("results", jsonArray);
      json.addProperty("time",time);
    }

    @Override
    public ResourceLocation getID() {
      return this.id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
      return this.serializer;
    }

    @Nullable
    @Override
    public JsonObject getAdvancementJson() {
      return null;
    }

    @Nullable
    @Override
    public ResourceLocation getAdvancementID() {
      return null;
    }
  }
}
