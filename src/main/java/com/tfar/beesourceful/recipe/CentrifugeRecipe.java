package com.tfar.beesourceful.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tfar.beesourceful.BeeSourceful;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class CentrifugeRecipe implements IRecipe<IInventory> {

  public final ResourceLocation id;
  public final Ingredient ingredient;
  public final List<ItemStack> outputs;
  public final int time;

  public CentrifugeRecipe(ResourceLocation id,
                          Ingredient ingredient, List<ItemStack> outputs, int time) {
    this.id = id;
    this.ingredient = ingredient;
    this.outputs = outputs;
    this.time = time;
  }

  @Override
  public boolean matches(IInventory inventory, World world) {
    return ingredient.test(inventory.getStackInSlot(0));
  }

  @Override
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
  @Override
  @Nonnull
  public ItemStack getRecipeOutput() {
    return getCraftingResults().get(0);
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
    return CentrifugeRecipeType.CENTRIFUGE;
  }

  public List<ItemStack> getCraftingResults() {
    return outputs;
  }

  public static class Serializer<T extends CentrifugeRecipe> extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<T> {
    final IRecipeFactory<T> factory;

    public Serializer(Serializer.IRecipeFactory<T> p_i50146_1_) {
      this.factory = p_i50146_1_;
    }

    public T read(ResourceLocation id, JsonObject json) {
      Ingredient ingredient;
      if (JSONUtils.isJsonArray(json, "ingredient")) {
        ingredient = Ingredient.deserialize(JSONUtils.getJsonArray(json, "ingredient"));
      } else {
        ingredient = Ingredient.deserialize(JSONUtils.getJsonObject(json, "ingredient"));
      }

      JsonArray jsonArray = JSONUtils.getJsonArray(json, "results");
      List<ItemStack> outputs = new ArrayList<>();
      jsonArray.forEach(jsonElement -> {
        String s = jsonElement.getAsString();
        ItemStack stack = new ItemStack(Registry.ITEM.getOrDefault(new ResourceLocation(s)));
        outputs.add(stack);
      });

      int time = JSONUtils.getInt(json,"time");

      return this.factory.create(id, ingredient, outputs,time);
    }

    public T read(ResourceLocation id, PacketBuffer buffer) {
      Ingredient ingredient = Ingredient.read(buffer);
      List<ItemStack> outputs = new ArrayList<>();
      IntStream.range(0,buffer.readInt()).forEach(i -> outputs.add(buffer.readItemStack()));
      int time = buffer.readInt();
      return this.factory.create(id, ingredient, outputs,time);
    }

    public void write(PacketBuffer buffer, T recipe) {
      recipe.ingredient.write(buffer);
      buffer.writeInt(recipe.outputs.size());
      recipe.outputs.forEach(buffer::writeItemStack);
      buffer.writeInt(recipe.time);
    }

    public interface IRecipeFactory<T extends CentrifugeRecipe> {
      T create(ResourceLocation id, Ingredient input, List<ItemStack> stacks,int time);
    }
  }
}
