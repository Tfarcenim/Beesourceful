package com.tfar.beesourceful.jei;

import com.tfar.beesourceful.BeeSourceful;
import com.tfar.beesourceful.recipe.CentrifugeRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;

@JeiPlugin
public class JeiCompat implements IModPlugin {

  private static final ResourceLocation pluginid = new ResourceLocation(BeeSourceful.MODID,BeeSourceful.MODID);

  @Override
  public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
    registration.addRecipeCatalyst(new ItemStack(BeeSourceful.Objectholders.centrifuge), new ResourceLocation(BeeSourceful.MODID,"centrifuge"));
  }

  @Override
  public void registerCategories(IRecipeCategoryRegistration registration) {
    IJeiHelpers jeiHelpers = registration.getJeiHelpers();
    IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
    registration.addRecipeCategories(
            new CentrifugeRecipeCategory(guiHelper,"gui.beesourceful.category.centrifuge"));
  }

  @Override
  public void registerRecipes(IRecipeRegistration registration) {
    RecipeManager recipeManager = Minecraft.getInstance().world.getRecipeManager();
    Collection<CentrifugeRecipe> recipes = getRecipes(recipeManager, CentrifugeRecipe.CENTRIFUGE);
    registration.addRecipes(recipes,new ResourceLocation(BeeSourceful.MODID,"centrifuge"));
  }

  private static <C extends IInventory, T extends IRecipe<C>> Collection<T> getRecipes(RecipeManager recipeManager, IRecipeType<T> recipeType) {
    Map<ResourceLocation, IRecipe<C>> recipesMap = recipeManager.getRecipes(recipeType);
    return (Collection<T>) recipesMap.values();
  }

  @Nonnull
  @Override
  public ResourceLocation getPluginUid() {
    return pluginid;
  }
}
