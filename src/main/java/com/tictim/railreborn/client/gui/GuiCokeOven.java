package com.tictim.railreborn.client.gui;

import com.tictim.railreborn.RailReborn;
import com.tictim.railreborn.inventory.ContainerCokeOven;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

public class GuiCokeOven extends GuiContainer{
	private static final ResourceLocation TEXTURE = new ResourceLocation(RailReborn.MODID, "textures/gui/coke_oven.png");
	private final ContainerCokeOven container;
	
	public GuiCokeOven(ContainerCokeOven container){
		super(container);
		this.container = container;
		this.ySize = 168;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks){
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}
	
	@Override
	protected void renderHoveredToolTip(int mouseX, int mouseY){
		super.renderHoveredToolTip(mouseX, mouseY);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		String s = container.getTE().getDisplayName().getFormattedText();
		this.fontRenderer.drawString(s, this.xSize/2-this.fontRenderer.getStringWidth(s)/2, -11, 0xFFFFFF);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float v, int i, int i1){
		this.mc.getTextureManager().bindTexture(TEXTURE);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		double p = container.getTE().getProgress();
		if(p>0) this.drawTexturedModalRect(this.guiLeft+53, this.guiTop+27, this.xSize, 0, (int)(26*p), 13);
	}
}