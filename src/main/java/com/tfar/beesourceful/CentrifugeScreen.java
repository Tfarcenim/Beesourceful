package com.tfar.beesourceful;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

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
    this.blit(i + 70, j + 35, 176, 0, (int)scaledprogress, 24);
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
