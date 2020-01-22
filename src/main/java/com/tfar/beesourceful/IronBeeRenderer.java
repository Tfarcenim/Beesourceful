package com.tfar.beesourceful;

import com.tfar.beesourceful.util.BeeType;
import net.minecraft.client.renderer.entity.BeeRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class IronBeeRenderer extends BeeRenderer {

  private final ResourceLocation ANGRY_SKIN;
  private final ResourceLocation ANGRY_NECTAR_SKIN;
  private final ResourceLocation PASSIVE_SKIN;
  private final ResourceLocation NECTAR_SKIN;

  public IronBeeRenderer(EntityRendererManager p_i226033_1_, BeeType beeType) {
    super(p_i226033_1_);
    String s = "textures/entity/";
    ANGRY_SKIN = new ResourceLocation(BeeSourceful.MODID, s +beeType+"_bee_angry.png");
    ANGRY_NECTAR_SKIN = new ResourceLocation(BeeSourceful.MODID, s +beeType+"_bee_angry_nectar.png");
    PASSIVE_SKIN = new ResourceLocation(BeeSourceful.MODID, s +beeType+"_bee.png");
    NECTAR_SKIN = new ResourceLocation(BeeSourceful.MODID, s +beeType+"_bee_nectar.png");
  }

  @Nonnull
  public ResourceLocation getEntityTexture(BeeEntity p_110775_1_) {
    if (p_110775_1_.isAngry()) {
      return p_110775_1_.hasNectar() ? ANGRY_NECTAR_SKIN : ANGRY_SKIN;
    } else {
      return p_110775_1_.hasNectar() ? NECTAR_SKIN : PASSIVE_SKIN;
    }
  }
}
