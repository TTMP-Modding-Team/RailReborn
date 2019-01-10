package com.tictim.railreborn.client.teisr;

import com.tictim.railreborn.pipelink.handler.PipeHandlers;
import com.tictim.railreborn.tileentity.TEPipe;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;

public class TEISRPipe extends TileEntityItemStackRenderer{
	private final TEPipe pipe = new TEPipe();
	
	{
		this.pipe.visual = new TEPipe.PipeVisual(PipeHandlers.INVALID);
	}
	
	@Override
	public void renderByItem(ItemStack stack, float partialTicks){
		this.pipe.visual.setHandler(PipeHandlers.fromItem(stack));
		TileEntityRendererDispatcher.instance.render(pipe, 0, 0, 0, 0, partialTicks);
	}
}
