package com.tfar.beesourceful.recipe;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class CentrifugeRecipeType {

  public static final IRecipeType<CentrifugeRecipe> CENTRIFUGE = register("beesourceful:centrifuge");

  public static <T extends IRecipe<?>> IRecipeType<T> register(final String name) {
    return Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(name), new IRecipeType<T>() {
      public String toString() {
        return name;
      }
    });
  }
}
