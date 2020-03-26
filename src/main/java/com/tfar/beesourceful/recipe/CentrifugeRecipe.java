package com.tfar.beesourceful.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tfar.beesourceful.BeeSourceful;
import com.tfar.beesourceful.BetterJSONUtils;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class CentrifugeRecipe implements IRecipe<IInventory> {

  public static final IRecipeType<CentrifugeRecipe> CENTRIFUGE = IRecipeType.register("beesourceful:centrifuge");

  public final ResourceLocation id;
  public final Ingredient ingredient;
  public final List<Pair<ItemStack,Double>> outputs;
  public final int time;

  public CentrifugeRecipe(ResourceLocation id,
                          Ingredient ingredient, List<Pair<ItemStack,Double>> outputs, int time) {
    this.id = id;
    this.ingredient = ingredient;
    this.outputs = outputs;
    this.time = time;
  }

  @Override
  public boolean matches(IInventory inventory, World world) {
    return ingredient.test(inventory.getStackInSlot(0));
  }
  /** use CentrifugeRecipe#getCraftingResults instead*/
  @Override
  @Nonnull
  @Deprecated
  public ItemStack getCraftingResult(IInventory p_77572_1_) {
    return ItemStack.EMPTY;
  }

  /**
   * Used to determine if this recipe can fit in a grid of the given width/height
   *
   * @param p_194133_1_
   * @param p_194133_2_
   */
  @Override
  public boolean canFit(int p_194133_1_, int p_194133_2_) {
    return true;
  }

  /**
   * Get the result of this recipe, usually for display purposes (e.g. recipe book). If your recipe has more than one
   * possible result (e.g. it's dynamic and depends on its inputs), then return an empty stack.
   */
  @Deprecated
  @Override
  @Nonnull
  public ItemStack getRecipeOutput() {
    return ItemStack.EMPTY;
  }

  @Override
  public ResourceLocation getId() {
    return id;
  }

  @Override
  public IRecipeSerializer<?> getSerializer() {
    return BeeSourceful.Objectholders.Recipes.centrifuge;
  }

  @Override
  public IRecipeType<?> getType() {
    return CENTRIFUGE;
  }

  public List<Pair<ItemStack,Double>> getCraftingResults() {
    return outputs;
  }

  public static class Serializer<T extends CentrifugeRecipe> extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<T> {
    final IRecipeFactory<T> factory;

    public Serializer(Serializer.IRecipeFactory<T> p_i50146_1_) {
      this.factory = p_i50146_1_;
    }

    @Override
    public T read(ResourceLocation id, JsonObject json) {
      Ingredient ingredient;
      if (JSONUtils.isJsonArray(json, "ingredient")) {
        ingredient = Ingredient.deserialize(JSONUtils.getJsonArray(json, "ingredient"));
      } else {
        ingredient = Ingredient.deserialize(JSONUtils.getJsonObject(json, "ingredient"));
      }

      JsonArray jsonArray = JSONUtils.getJsonArray(json, "results");
      List<Pair<ItemStack,Double>> outputs = new ArrayList<>();
      jsonArray.forEach(jsonElement -> {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String registryname = JSONUtils.getString(jsonObject,"item");
        int count = JSONUtils.getInt(jsonObject,"count",1);
        double chance = BetterJSONUtils.getDouble(jsonObject,"chance",1);
        ItemStack stack = new ItemStack(Registry.ITEM.getOrDefault(new ResourceLocation(registryname)),count);
        outputs.add(Pair.of(stack,chance));
      });

      int time = JSONUtils.getInt(json,"time");

      return this.factory.create(id, ingredient, outputs,time);
    }

    public T read(ResourceLocation id, PacketBuffer buffer) {
      Ingredient ingredient = Ingredient.read(buffer);
      List<Pair<ItemStack,Double>> outputs = new ArrayList<>();
      IntStream.range(0,buffer.readInt()).forEach(i -> outputs.add(Pair.of(buffer.readItemStack(),buffer.readDouble())));
      int time = buffer.readInt();
      return this.factory.create(id, ingredient, outputs,time);
    }

    public void write(PacketBuffer buffer, T recipe) {
      recipe.ingredient.write(buffer);
      buffer.writeInt(recipe.outputs.size());
      recipe.outputs.forEach(itemStackDoublePair -> {
        buffer.writeItemStack(itemStackDoublePair.getLeft());
        buffer.writeDouble(itemStackDoublePair.getRight());
      });
      buffer.writeInt(recipe.time);
    }

    public interface IRecipeFactory<T extends CentrifugeRecipe> {
      T create(ResourceLocation id, Ingredient input, List<Pair<ItemStack,Double>> stacks,int time);
    }
  }
}
