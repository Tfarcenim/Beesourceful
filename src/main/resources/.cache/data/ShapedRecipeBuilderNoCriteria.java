package tfar.beesourceful.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class ShapedRecipeBuilderNoCriteria {
    private final Item result;
    private final int count;
    private final List<String> pattern = Lists.newArrayList();
    private final Map<Character, Ingredient> key = Maps.newLinkedHashMap();
    private String group;

    public ShapedRecipeBuilderNoCriteria(IItemProvider p_i48261_1_, int p_i48261_2_) {
      this.result = p_i48261_1_.asItem();
      this.count = p_i48261_2_;
    }

    public static ShapedRecipeBuilderNoCriteria shapedRecipe(IItemProvider p_200470_0_) {
      return shapedRecipe(p_200470_0_, 1);
    }

    public static ShapedRecipeBuilderNoCriteria shapedRecipe(IItemProvider p_200468_0_, int p_200468_1_) {
      return new ShapedRecipeBuilderNoCriteria(p_200468_0_, p_200468_1_);
    }

    public ShapedRecipeBuilderNoCriteria key(Character p_200469_1_, Tag<Item> p_200469_2_) {
      return this.key(p_200469_1_, Ingredient.fromTag(p_200469_2_));
    }

    public ShapedRecipeBuilderNoCriteria key(Character p_200462_1_, IItemProvider p_200462_2_) {
      return this.key(p_200462_1_, Ingredient.fromItems(p_200462_2_));
    }

    public ShapedRecipeBuilderNoCriteria key(Character p_200471_1_, Ingredient p_200471_2_) {
      if (this.key.containsKey(p_200471_1_)) {
        throw new IllegalArgumentException("Symbol '" + p_200471_1_ + "' is already defined!");
      } else if (p_200471_1_ == ' ') {
        throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
      } else {
        this.key.put(p_200471_1_, p_200471_2_);
        return this;
      }
    }

    public ShapedRecipeBuilderNoCriteria patternLine(String p_200472_1_) {
      if (!this.pattern.isEmpty() && p_200472_1_.length() != ((String)this.pattern.get(0)).length()) {
        throw new IllegalArgumentException("Pattern must be the same width on every line!");
      } else {
        this.pattern.add(p_200472_1_);
        return this;
      }
    }

    public ShapedRecipeBuilderNoCriteria setGroup(String p_200473_1_) {
      this.group = p_200473_1_;
      return this;
    }

    public void build(Consumer<IFinishedRecipe> p_200464_1_) {
      this.build(p_200464_1_, Registry.ITEM.getKey(this.result));
    }

    public void build(Consumer<IFinishedRecipe> p_200466_1_, String p_200466_2_) {
      ResourceLocation lvt_3_1_ = Registry.ITEM.getKey(this.result);
      if ((new ResourceLocation(p_200466_2_)).equals(lvt_3_1_)) {
        throw new IllegalStateException("Shaped Recipe " + p_200466_2_ + " should remove its 'save' argument");
      } else {
        this.build(p_200466_1_, new ResourceLocation(p_200466_2_));
      }
    }

    public void build(Consumer<IFinishedRecipe> p_200467_1_, ResourceLocation p_200467_2_) {
      this.validate(p_200467_2_);
      p_200467_1_.accept(new ShapedRecipeBuilderNoCriteria.Result(p_200467_2_, this.result, this.count, this.group == null ? "" : this.group, this.pattern, this.key));
    }

    private void validate(ResourceLocation p_200463_1_) {
      if (this.pattern.isEmpty()) {
        throw new IllegalStateException("No pattern is defined for shaped recipe " + p_200463_1_ + "!");
      } else {
        Set<Character> lvt_2_1_ = Sets.newHashSet(this.key.keySet());
        lvt_2_1_.remove(' ');
        Iterator var3 = this.pattern.iterator();

        while(var3.hasNext()) {
          String lvt_4_1_ = (String)var3.next();

          for(int lvt_5_1_ = 0; lvt_5_1_ < lvt_4_1_.length(); ++lvt_5_1_) {
            char lvt_6_1_ = lvt_4_1_.charAt(lvt_5_1_);
            if (!this.key.containsKey(lvt_6_1_) && lvt_6_1_ != ' ') {
              throw new IllegalStateException("Pattern in recipe " + p_200463_1_ + " uses undefined symbol '" + lvt_6_1_ + "'");
            }

            lvt_2_1_.remove(lvt_6_1_);
          }
        }

        if (!lvt_2_1_.isEmpty()) {
          throw new IllegalStateException("Ingredients are defined but not used in pattern for recipe " + p_200463_1_);
        } else if (this.pattern.size() == 1 && ((String)this.pattern.get(0)).length() == 1) {
          throw new IllegalStateException("Shaped recipe " + p_200463_1_ + " only takes in a single item - should it be a shapeless recipe instead?");
        }
      }
    }

    public class Result implements IFinishedRecipe {
      private final ResourceLocation id;
      private final Item result;
      private final int count;
      private final String group;
      private final List<String> pattern;
      private final Map<Character, Ingredient> key;

      public Result(ResourceLocation p_i48271_2_, Item p_i48271_3_, int p_i48271_4_, String p_i48271_5_, List<String> p_i48271_6_, Map<Character, Ingredient> p_i48271_7_) {
        this.id = p_i48271_2_;
        this.result = p_i48271_3_;
        this.count = p_i48271_4_;
        this.group = p_i48271_5_;
        this.pattern = p_i48271_6_;
        this.key = p_i48271_7_;
      }

      public void serialize(JsonObject p_218610_1_) {
        if (!this.group.isEmpty()) {
          p_218610_1_.addProperty("group", this.group);
        }

        JsonArray lvt_2_1_ = new JsonArray();
        Iterator var3 = this.pattern.iterator();

        while(var3.hasNext()) {
          String lvt_4_1_ = (String)var3.next();
          lvt_2_1_.add(lvt_4_1_);
        }

        p_218610_1_.add("pattern", lvt_2_1_);
        JsonObject lvt_3_1_ = new JsonObject();
        Iterator var7 = this.key.entrySet().iterator();

        while(var7.hasNext()) {
          Map.Entry<Character, Ingredient> lvt_5_1_ = (Map.Entry)var7.next();
          lvt_3_1_.add(String.valueOf(lvt_5_1_.getKey()), ((Ingredient)lvt_5_1_.getValue()).serialize());
        }

        p_218610_1_.add("key", lvt_3_1_);
        JsonObject lvt_4_2_ = new JsonObject();
        lvt_4_2_.addProperty("item", Registry.ITEM.getKey(this.result).toString());
        if (this.count > 1) {
          lvt_4_2_.addProperty("count", this.count);
        }

        p_218610_1_.add("result", lvt_4_2_);
      }

      public IRecipeSerializer<?> getSerializer() {
        return IRecipeSerializer.CRAFTING_SHAPED;
      }

      public ResourceLocation getID() {
        return this.id;
      }

      @Nullable
      public JsonObject getAdvancementJson() {
        return null;
      }

      @Nullable
      public ResourceLocation getAdvancementID() {
        return null;
      }
    }
  }