package com.tictim.railreborn.client.teisr;

import com.tictim.railreborn.enums.Engines;
import com.tictim.railreborn.tileentity.TEEngine;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;

public class TEISREngine extends TileEntityItemStackRenderer{
	private final TEEngine te;
	
	public TEISREngine(Engines engine){
		this.te = new TEEngine().setEngine(engine);
	}
	
	@Override
	public void renderByItem(ItemStack stack, float partialTicks){
//		TileEntityRendererDispatcher.instance.render(this.te, 0, 0, 0, 0, partialTicks);
	}
}
