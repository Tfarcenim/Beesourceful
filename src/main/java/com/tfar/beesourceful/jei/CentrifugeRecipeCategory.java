package com.tfar.beesourceful.jei;

import com.google.common.collect.Lists;
import com.tfar.beesourceful.BeeSourceful;
import com.tfar.beesourceful.blockentity.CentrifugeBlockEntity;
import com.tfar.beesourceful.recipe.CentrifugeRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

public class CentrifugeRecipeCategory implements IRecipeCategory<CentrifugeRecipe> {

  private final IDrawable icon;
  private final IDrawable background;
  private final String localizedName;
  protected final IDrawableAnimated arrow;
  protected final FluidStackRenderer fluidStackRenderer;

  public CentrifugeRecipeCategory(IGuiHelper guiHelper, String translationKey) {
    this.background = guiHelper.createDrawable(new ResourceLocation(BeeSourceful.MODID, "textures/gui/jei/centrifuge.png"), 0, 0, 118, 68);
    this.icon = guiHelper.createDrawableIngredient(new ItemStack(BeeSourceful.Objectholders.centrifuge));
    this.localizedName = I18n.format(translationKey);
    this.arrow = guiHelper.drawableBuilder(new ResourceLocation(BeeSourceful.MODID, "textures/gui/jei/centrifuge.png"), 0, 61, 35, 25)
            .buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
    this.fluidStackRenderer = new FluidStackRenderer(10000,true,8,64,null);

  }

  @Override
  public ResourceLocation getUid() {
    return new ResourceLocation(BeeSourceful.MODID, "centrifuge");
  }

  @Override
  public Class<? extends CentrifugeRecipe> getRecipeClass() {
    return CentrifugeRecipe.class;
  }

  @Override
  public String getTitle() {
    return localizedName;
  }

  @Override
  public IDrawable getBackground() {
    return background;
  }

  @Override
  public IDrawable getIcon() {
    return icon;
  }

  @Override
  public void setIngredients(CentrifugeRecipe recipe, IIngredients iIngredients) {
    iIngredients.setInputIngredients(Lists.newArrayList(recipe.ingredient));
    List<Pair<ItemStack,Double>> outputs = recipe.outputs;
    List<ItemStack> stacks = outputs.stream().map(Pair::getLeft).collect(Collectors.toList());
      iIngredients.setOutputs(VanillaTypes.ITEM, stacks);
      iIngredients.setOutputs(VanillaTypes.FLUID,Lists.newArrayList(recipe.fluid));
  }

  @Override
  public void setRecipe(IRecipeLayout iRecipeLayout, CentrifugeRecipe centrifugeRecipe, IIngredients iIngredients) {
    IGuiItemStackGroup guiItemStacks = iRecipeLayout.getItemStacks();
    guiItemStacks.init(CentrifugeBlockEntity.HONEYCOMB_SLOT, true, 0, 28);
    guiItemStacks.init(CentrifugeBlockEntity.OUTPUT0, false, 64, 19);
    guiItemStacks.init(CentrifugeBlockEntity.OUTPUT1, false, 82, 19);
    guiItemStacks.init(CentrifugeBlockEntity.OUTPUT2, false, 64, 38);
    guiItemStacks.init(CentrifugeBlockEntity.OUTPUT3, false, 82, 38);
    IGuiFluidStackGroup guiFluidStacks = iRecipeLayout.getFluidStacks();
    guiFluidStacks.init(0,false,fluidStackRenderer,109,1,8,64,0,0);
    guiItemStacks.set(iIngredients);
    guiFluidStacks.set(iIngredients);
  }

  @Override
  public void draw(CentrifugeRecipe recipe, double mouseX, double mouseY) {
    this.arrow.draw(28, 21);
    DecimalFormat decimalFormat = new DecimalFormat("##%");

    Minecraft minecraft = Minecraft.getInstance();
    FontRenderer fontRenderer = minecraft.fontRenderer;

    List<Pair<ItemStack,Double>> outputs = recipe.outputs;
    int size = outputs.size();
    if (size > 0) {
      final double chance1 = recipe.outputs.get(0).getRight();
      if (chance1 < 1) {
        String chance1String = decimalFormat.format(chance1);
        fontRenderer.drawString(chance1String, 62, 10, 0xff808080);
      }
    }
    if (size > 1) {
      final double chance1 = recipe.outputs.get(1).getRight();
      if (chance1 < 1) {
        String chance1String = decimalFormat.format(chance1);
        fontRenderer.drawString(chance1String, 82, 10, 0xff808080);
      }
    }
    if (size > 2) {
      final double chance1 = recipe.outputs.get(2).getRight();
      if (chance1 < 1) {
        String chance1String = decimalFormat.format(chance1);
        fontRenderer.drawString(chance1String, 62, 50, 0xff808080);
      }
    }
    if (size > 3) {
      final double chance1 = recipe.outputs.get(3).getRight();
      if (chance1 < 1) {
        String chance1String = decimalFormat.format(chance1);
        fontRenderer.drawString(chance1String, 82, 50, 0xff808080);
      }
    }
  }
}
