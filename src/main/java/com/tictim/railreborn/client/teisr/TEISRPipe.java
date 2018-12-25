package com.tictim.railreborn.client.teisr;

import com.tictim.railreborn.pipelink.handler.PipeHandler;
import com.tictim.railreborn.tileentity.TEPipe;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;

public class TEISRPipe extends TileEntityItemStackRenderer{
	private final TEPipe pipe;
	
	public TEISRPipe(PipeHandler handler){
		this.pipe = new TEPipe().setHandler(handler);
		this.pipe.visual = new TEPipe.PipeVisual();
	}
	
	@Override
	public void renderByItem(ItemStack stack, float partialTicks){
		TileEntityRendererDispatcher.instance.render(pipe, 0, 0, 0, 0, partialTicks);
	}
}
