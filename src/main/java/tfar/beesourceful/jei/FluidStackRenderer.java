package tfar.beesourceful.jei;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;

public class FluidStackRenderer implements IIngredientRenderer<FluidStack> {
	private static final int TEX_WIDTH = 16;
	private static final int TEX_HEIGHT = 16;
	private static final int MIN_FLUID_HEIGHT = 1;
	private final int capacityMb;
	private final FluidStackRenderer.TooltipMode tooltipMode;
	private final int width;
	private final int height;
	@Nullable
	private final IDrawable overlay;

	public FluidStackRenderer() {
		this(1000, FluidStackRenderer.TooltipMode.ITEM_LIST, 16, 16, null);
	}

	public FluidStackRenderer(int capacityMb, boolean showCapacity, int width, int height, @Nullable IDrawable overlay) {
		this(capacityMb, showCapacity ? FluidStackRenderer.TooltipMode.SHOW_AMOUNT_AND_CAPACITY : FluidStackRenderer.TooltipMode.SHOW_AMOUNT, width, height, overlay);
	}

	public FluidStackRenderer(int capacityMb, FluidStackRenderer.TooltipMode tooltipMode, int width, int height, @Nullable IDrawable overlay) {
		this.capacityMb = capacityMb;
		this.tooltipMode = tooltipMode;
		this.width = width;
		this.height = height;
		this.overlay = overlay;
	}

	public void render(MatrixStack matrixStack, int xPosition, int yPosition, @Nullable FluidStack fluidStack) {
		RenderSystem.enableBlend();
		RenderSystem.enableAlphaTest();
		this.drawFluid(matrixStack, xPosition, yPosition, fluidStack);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		if (this.overlay != null) {
			matrixStack.push();
			matrixStack.translate(0.0D, 0.0D, 200.0D);
			this.overlay.draw(matrixStack, xPosition, yPosition);
			matrixStack.pop();
		}

		RenderSystem.disableAlphaTest();
		RenderSystem.disableBlend();
	}

	private void drawFluid(MatrixStack matrixStack, int xPosition, int yPosition, @Nullable FluidStack fluidStack) {
		if (fluidStack != null) {
			Fluid fluid = fluidStack.getFluid();
			if (fluid != null) {
				TextureAtlasSprite fluidStillSprite = getStillFluidSprite(fluidStack);
				FluidAttributes attributes = fluid.getAttributes();
				int fluidColor = attributes.getColor(fluidStack);
				int amount = fluidStack.getAmount();
				int scaledAmount = amount * this.height / this.capacityMb;
				if (amount > 0 && scaledAmount < 1) {
					scaledAmount = 1;
				}

				if (scaledAmount > this.height) {
					scaledAmount = this.height;
				}

				this.drawTiledSprite(matrixStack, xPosition, yPosition, this.width, this.height, fluidColor, scaledAmount, fluidStillSprite);
			}
		}
	}

	private void drawTiledSprite(MatrixStack matrixStack, int xPosition, int yPosition, int tiledWidth, int tiledHeight, int color, int scaledAmount, TextureAtlasSprite sprite) {
		Minecraft minecraft = Minecraft.getInstance();
		minecraft.getTextureManager().bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
		Matrix4f matrix = matrixStack.getLast().getMatrix();
		setGLColorFromInt(color);
		int xTileCount = tiledWidth / 16;
		int xRemainder = tiledWidth - xTileCount * 16;
		int yTileCount = scaledAmount / 16;
		int yRemainder = scaledAmount - yTileCount * 16;
		int yStart = yPosition + tiledHeight;

		for(int xTile = 0; xTile <= xTileCount; ++xTile) {
			for(int yTile = 0; yTile <= yTileCount; ++yTile) {
				int width = xTile == xTileCount ? xRemainder : 16;
				int height = yTile == yTileCount ? yRemainder : 16;
				int x = xPosition + xTile * 16;
				int y = yStart - (yTile + 1) * 16;
				if (width > 0 && height > 0) {
					int maskTop = 16 - height;
					int maskRight = 16 - width;
					drawTextureWithMasking(matrix, (float)x, (float)y, sprite, maskTop, maskRight, 100.0F);
				}
			}
		}

	}

	private static TextureAtlasSprite getStillFluidSprite(FluidStack fluidStack) {
		Minecraft minecraft = Minecraft.getInstance();
		Fluid fluid = fluidStack.getFluid();
		FluidAttributes attributes = fluid.getAttributes();
		ResourceLocation fluidStill = attributes.getStillTexture(fluidStack);
		return (TextureAtlasSprite)minecraft.getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(fluidStill);
	}

	private static void setGLColorFromInt(int color) {
		float red = (float)(color >> 16 & 255) / 255.0F;
		float green = (float)(color >> 8 & 255) / 255.0F;
		float blue = (float)(color & 255) / 255.0F;
		float alpha = (float)(color >> 24 & 255) / 255.0F;
		RenderSystem.color4f(red, green, blue, alpha);
	}

	private static void drawTextureWithMasking(Matrix4f matrix, float xCoord, float yCoord, TextureAtlasSprite textureSprite, int maskTop, int maskRight, float zLevel) {
		float uMin = textureSprite.getMinU();
		float uMax = textureSprite.getMaxU();
		float vMin = textureSprite.getMinV();
		float vMax = textureSprite.getMaxV();
		uMax -= (float)maskRight / 16.0F * (uMax - uMin);
		vMax -= (float)maskTop / 16.0F * (vMax - vMin);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferBuilder.pos(matrix, xCoord, yCoord + 16.0F, zLevel).tex(uMin, vMax).endVertex();
		bufferBuilder.pos(matrix, xCoord + 16.0F - (float)maskRight, yCoord + 16.0F, zLevel).tex(uMax, vMax).endVertex();
		bufferBuilder.pos(matrix, xCoord + 16.0F - (float)maskRight, yCoord + (float)maskTop, zLevel).tex(uMax, vMin).endVertex();
		bufferBuilder.pos(matrix, xCoord, yCoord + (float)maskTop, zLevel).tex(uMin, vMin).endVertex();
		tessellator.draw();
	}

	public List<ITextComponent> getTooltip(FluidStack fluidStack, ITooltipFlag tooltipFlag) {
		List<ITextComponent> tooltip = new ArrayList();
		Fluid fluidType = fluidStack.getFluid();
		if (fluidType != null) {
			ITextComponent displayName = fluidStack.getDisplayName();
			int amount = fluidStack.getAmount();
			String amountString;
			if (this.tooltipMode == TooltipMode.SHOW_AMOUNT_AND_CAPACITY) {
				amountString = I18n.format("jei.tooltip.liquid.amount.with.capacity", amount, this.capacityMb);
				//tooltip.add(TextFormatting.GRAY + amountString);
			} else if (this.tooltipMode == TooltipMode.SHOW_AMOUNT) {
				amountString = I18n.format("jei.tooltip.liquid.amount", amount);
				//tooltip.add(TextFormatting.GRAY + amountString);
			}

		}
		return tooltip;
	}

	enum TooltipMode {
		SHOW_AMOUNT,
		SHOW_AMOUNT_AND_CAPACITY,
		ITEM_LIST;

		TooltipMode() {
		}
	}
}
