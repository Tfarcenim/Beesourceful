package com.tfar.beesourceful;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;

public class CentrifugeScreen extends ContainerScreen<CentrifugeContainer> {
  public CentrifugeScreen(CentrifugeContainer p_i51105_1_, PlayerInventory p_i51105_2_, ITextComponent p_i51105_3_) {
    super(p_i51105_1_, p_i51105_2_, p_i51105_3_);
  }

  /**
   * Draws the background layer of this container (behind the items).
   *
   * @param p_146976_1_
   * @param p_146976_2_
   * @param p_146976_3_
   */
  @Override
  protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
    ResourceLocation texture = new ResourceLocation(BeeSourceful.MODID,"textures/gui/centrifuge.png");
    this.minecraft.getTextureManager().bindTexture(texture);
    int i = (this.width - this.xSize) / 2;
    int j = (this.height - this.ySize) / 2;
    this.blit(i, j, 0, 0, this.xSize, this.ySize);
    double scaledprogress = 37d * this.container.centrifugeBlockEntity.time /
            Math.max(this.container.centrifugeBlockEntity.totalTime,1d);
    this.blit(i + 62, j + 35, 176, 0, (int)scaledprogress, 24);
    drawFluid();
  }

  public void drawFluid() {
    int scaledHeight = (int) (container.centrifugeBlockEntity.fluidTank.getFluidInTank(0).getAmount() * (64d/10000));

    int xPos = guiLeft + 160;
    int yPos = guiTop + 74 - scaledHeight;
//You need to get ARGB out of the color to pass

    FluidStack fluidStack = container.centrifugeBlockEntity.fluidTank.getFluidInTank(0);
    if (!fluidStack.isEmpty()) {
      minecraft.textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
      int color = fluidStack.getFluid().getAttributes().getColor(fluidStack);
      TextureAtlasSprite textureAtlasSprite = getFluidTexture(fluidStack);
      RenderSystem.color3f((color >> 16 & 0xff) / 255f, (color >> 8 & 0xff) / 255f, (color & 0xff) / 255f);
      blit(xPos, yPos, 0, 8, scaledHeight, textureAtlasSprite);
    }
  }

  public static TextureAtlasSprite getFluidTexture(@Nonnull FluidStack fluidStack) {
    Fluid fluid = fluidStack.getFluid();
    ResourceLocation spriteLocation = fluid.getAttributes().getStillTexture(fluidStack);
    return getSprite(spriteLocation);
  }

  public static TextureAtlasSprite getSprite(ResourceLocation spriteLocation) {
    return Minecraft.getInstance().getSpriteAtlas(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(spriteLocation);
  }

  @Override
  public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
    this.renderBackground();
    super.render(p_render_1_, p_render_2_, p_render_3_);
    this.renderHoveredToolTip(p_render_1_, p_render_2_);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    int size = font.getStringWidth(title.getFormattedText());
    int start = (this.xSize - size)/2;
    this.font.drawString(this.title.getFormattedText(), start, 5, 0x404040);
  }
}
