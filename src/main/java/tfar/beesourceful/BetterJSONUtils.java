package tfar.beesourceful;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import tfar.beesourceful.inventory.ImmutableFluidStack;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fluids.FluidStack;

public class BetterJSONUtils extends JSONUtils {

  public static double getDouble(JsonElement p_151220_0_, String p_151220_1_) {
    if (p_151220_0_.isJsonPrimitive() && p_151220_0_.getAsJsonPrimitive().isNumber()) {
      return p_151220_0_.getAsDouble();
    } else {
      throw new JsonSyntaxException("Expected " + p_151220_1_ + " to be a Double, was " + JSONUtils.toString(p_151220_0_));
    }
  }

  public static double getDouble(JsonObject p_151217_0_, String p_151217_1_) {
    if (p_151217_0_.has(p_151217_1_)) {
      return getDouble(p_151217_0_.get(p_151217_1_), p_151217_1_);
    } else {
      throw new JsonSyntaxException("Missing " + p_151217_1_ + ", expected to find a Double");
    }
  }

  public static double getOrDefaultDouble(JsonObject p_151221_0_, String p_151221_1_, double defaultValue) {
    return p_151221_0_.has(p_151221_1_) ? getDouble(p_151221_0_.get(p_151221_1_), p_151221_1_) : defaultValue;
  }

  public static FluidStack getFluidStack(JsonObject jsonObject,String string) {
    JsonObject json = jsonObject.get(string).getAsJsonObject();
    Fluid fluid = getFluid(json,"fluid");
    int amount = JSONUtils.getInt(json,"amount",1000);
    return new ImmutableFluidStack(fluid,amount);
  }

  public static Fluid getFluid(JsonElement p_188172_0_, String p_188172_1_) {
    if (p_188172_0_.isJsonPrimitive()) {
      String lvt_2_1_ = p_188172_0_.getAsString();
      return Registry.FLUID.getValue(new ResourceLocation(lvt_2_1_)).orElseThrow(() -> new JsonSyntaxException("Expected " + p_188172_1_ + " to be a fluid, was unknown string '" + lvt_2_1_ + "'"));
    } else {
      throw new JsonSyntaxException("Expected " + p_188172_1_ + " to be a fluid, was " +  JSONUtils.toString(p_188172_0_));
    }
  }

  public static Fluid getFluid(JsonObject p_188180_0_, String p_188180_1_) {
    if (p_188180_0_.has(p_188180_1_)) {
      return getFluid(p_188180_0_.get(p_188180_1_), p_188180_1_);
    } else {
      throw new JsonSyntaxException("Missing " + p_188180_1_ + ", expected to find a fluid");
    }
  }

}
