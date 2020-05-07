package com.tfar.beesourceful.jei;

import com.google.common.collect.Lists;
import com.tfar.beesourceful.BeeSourceful;
import com.tfar.beesourceful.CentrifugeBlockEntity;
import com.tfar.beesourceful.recipe.CentrifugeRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CentrifugeRecipeCategory implements IRecipeCategory<CentrifugeRecipe> {

  private final IDrawable icon;
  private final IDrawable background;
  private final String localizedName;
  protected final IDrawableAnimated arrow;

  public CentrifugeRecipeCategory(IGuiHelper guiHelper, String translationKey) {
    this.background = guiHelper.createDrawable(new ResourceLocation(BeeSourceful.MODID, "textures/gui/jei/centrifuge.png"), 0, 0, 88, 63);
    this.icon = guiHelper.createDrawableIngredient(new ItemStack(BeeSourceful.Objectholders.centrifuge));
    this.localizedName = I18n.format(translationKey);
    this.arrow = guiHelper.drawableBuilder(new ResourceLocation(BeeSourceful.MODID, "textures/gui/jei/centrifuge.png"), 0, 63, 35, 24)
            .buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
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
    iIngredients.setInputIngredients(Lists.newArrayList(recipe.ingredient,Ingredient.fromItems(Items.GLASS_BOTTLE)));
    List<Pair<ItemStack,Double>> outputs = recipe.outputs;
    List<ItemStack> stacks = new ArrayList<>();
    stacks.add(new ItemStack(Items.field_226638_pX_));
    stacks.addAll(outputs.stream().map(Pair::getLeft).collect(Collectors.toList()));
      iIngredients.setOutputs(VanillaTypes.ITEM, stacks);
  }

  @Override
  public void setRecipe(IRecipeLayout iRecipeLayout, CentrifugeRecipe centrifugeRecipe, IIngredients iIngredients) {
    IGuiItemStackGroup guiItemStacks = iRecipeLayout.getItemStacks();
    guiItemStacks.init(CentrifugeBlockEntity.HONEYCOMB_SLOT, true, 36, 0);
    guiItemStacks.init(CentrifugeBlockEntity.BOTTLE_SLOT, true, 0, 23);
    guiItemStacks.init(CentrifugeBlockEntity.HONEY_BOTTLE, false, 71, 23);
    guiItemStacks.init(CentrifugeBlockEntity.OUTPUT1, false, 27, 45);
    guiItemStacks.init(CentrifugeBlockEntity.OUTPUT2, false, 45, 45);
    guiItemStacks.set(iIngredients);
  }

  @Override
  public void draw(CentrifugeRecipe recipe, double mouseX, double mouseY) {
    this.arrow.draw(27, 21);

    final double honeychance = .2;

    DecimalFormat decimalFormat = new DecimalFormat("##%");

    String honeychancestring = decimalFormat.format(honeychance);
    Minecraft minecraft = Minecraft.getInstance();
    FontRenderer fontRenderer = minecraft.fontRenderer;
    fontRenderer.drawString(honeychancestring, 71, 42, 0xff808080);

    String chance1String = "";
    String chance2String = "";
    if (recipe.outputs.size() > 0) {
      chance1String = decimalFormat.format(recipe.outputs.get(0).getRight());
    }
    if (recipe.outputs.size() > 1) {
      chance2String = decimalFormat.format(recipe.outputs.get(1).getRight());
    }

    int size = fontRenderer.getStringWidth(chance1String);

    fontRenderer.drawString(chance1String, 26-size, 55, 0xff808080);
    fontRenderer.drawString(chance2String, 66, 55, 0xff808080);
  }
}
