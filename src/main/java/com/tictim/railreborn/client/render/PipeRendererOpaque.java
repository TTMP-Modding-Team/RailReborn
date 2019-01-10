package com.tictim.railreborn.client.render;

import com.tictim.railreborn.client.tesr.TESRPipe;
import com.tictim.railreborn.pipelink.handler.PipeHandler;
import com.tictim.railreborn.tileentity.TEPipe;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;

public class PipeRendererOpaque implements PipeRenderer{
	private final ResourceLocation texture;
	
	public PipeRendererOpaque(PipeHandler pipe){
		this(new ResourceLocation(pipe.getRegistryName().getResourceDomain(), "textures/block/pipe/"+pipe.getRegistryName().getResourcePath()+".png"));
	}
	
	public PipeRendererOpaque(ResourceLocation texture){
		this.texture = texture;
	}
	
	@Override
	public void render(TESRPipe tesr, TEPipe.PipeVisual visual, float partialTicks, int destroyStage){
		if(destroyStage >= 0) tesr.bindDestroyStages(destroyStage);
		else tesr.bindTexture(texture);
		draw(visual);
		if(destroyStage >= 0) tesr.resetDestroyStages();
	}
	
	protected void draw(TEPipe.PipeVisual visual){
		for(EnumFacing f: EnumFacing.VALUES){
			TEPipe.PipeSideVisual s = visual==null ? null : visual.getVisual(f);
			if(s!=null){
				AxisAlignedBB aabb = visual.getHandler().getDiameter().getSidedBB(f);
				EnumFacing opposite = f.getOpposite();
				for(EnumFacing f2: EnumFacing.VALUES){
					if(f2!=opposite&&!(s.connectedToNode&&f==f2)) PipeRenderer.drawAtBoundingBox(aabb, f2);
				}
			}else{
				AxisAlignedBB aabb = visual.getHandler().getDiameter().getCenterBB();
				PipeRenderer.drawAtBoundingBox(aabb, f);
			}
		}
	}
}
