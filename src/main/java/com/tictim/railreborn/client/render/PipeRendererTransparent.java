package com.tictim.railreborn.client.render;

import com.tictim.railreborn.pipelink.handler.PipeHandler;
import com.tictim.railreborn.tileentity.TEPipe;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class PipeRendererTransparent extends PipeRendererOpaque{
	public PipeRendererTransparent(PipeHandler pipe){
		super(pipe);
	}
	
	public PipeRendererTransparent(ResourceLocation texture){
		super(texture);
	}
	
	@Override
	protected void draw(TEPipe.PipeVisual visual){
		GlStateManager.disableCull();
		super.draw(visual);
	}
}
