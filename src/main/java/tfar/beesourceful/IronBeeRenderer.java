package tfar.beesourceful;

import net.minecraft.client.renderer.entity.BeeRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.util.ResourceLocation;
import tfar.beesourceful.entity.BeeEntityType;

import javax.annotation.Nonnull;

public class IronBeeRenderer extends BeeRenderer {

  private final ResourceLocation ANGRY_SKIN;
  private final ResourceLocation ANGRY_NECTAR_SKIN;
  private final ResourceLocation PASSIVE_SKIN;
  private final ResourceLocation NECTAR_SKIN;

  public IronBeeRenderer(EntityRendererManager p_i226033_1_, BeeEntityType beeType) {
    super(p_i226033_1_);
    String s = "textures/entity/";
    ANGRY_SKIN = new ResourceLocation(BeeSourceful.MODID, s +beeType.getRegistryName().getPath()+"_angry.png");
    ANGRY_NECTAR_SKIN = new ResourceLocation(BeeSourceful.MODID, s +beeType.getRegistryName().getPath()+"_angry_nectar.png");
    PASSIVE_SKIN = new ResourceLocation(BeeSourceful.MODID, s +beeType.getRegistryName().getPath()+".png");
    NECTAR_SKIN = new ResourceLocation(BeeSourceful.MODID, s +beeType.getRegistryName().getPath()+"_nectar.png");
  }

  @Nonnull
  public ResourceLocation getEntityTexture(BeeEntity beeEntity) {
    if (beeEntity.func_233678_J__()) {
      return beeEntity.hasNectar() ? ANGRY_NECTAR_SKIN : ANGRY_SKIN;
    } else {
      return beeEntity.hasNectar() ? NECTAR_SKIN : PASSIVE_SKIN;
    }
  }
}
