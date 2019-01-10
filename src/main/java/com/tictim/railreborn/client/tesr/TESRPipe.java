package com.tictim.railreborn.client.tesr;

import com.tictim.railreborn.tileentity.TEPipe;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

public class TESRPipe extends TileEntitySpecialRenderer<TEPipe>{
	@Override
	public void render(TEPipe te, double x, double y, double z, float partialTicks, int destroyStage, float alpha){
		TEPipe.PipeVisual visual = te.visual;
		if(visual==null) return;
		
		GL11.glPushMatrix();
		GlStateManager.translate(x, y, z);
		GL11.glColor4f(1, 1, 1, alpha);
		visual.getHandler().getPipeRenderer().render(this, visual, partialTicks, destroyStage);
		GL11.glPopMatrix();
		
		/*
		if(destroyStage >= 0){
			this.bindTexture(DESTROY_STAGES[destroyStage]);
			GlStateManager.matrixMode(GL11.GL_TEXTURE);
			GlStateManager.pushMatrix();
			GlStateManager.scale(4, 4, 1);
			GlStateManager.translate(0.0625f, 0.0625f, 0.0625f);
			GlStateManager.matrixMode(GL11.GL_MODELVIEW);
		}else this.bindTexture(visual.getHandler().getPipeTexture());
		
		GL11.glPushMatrix();
		GlStateManager.disableCull();
		GlStateManager.translate(x, y, z);
		GL11.glColor4f(1, 1, 1, alpha);
		
		
		for(EnumFacing f: EnumFacing.VALUES){
			TEPipe.PipeSideVisual s = te.visual==null ? null : te.visual.getVisual(f);
			if(s!=null){
				AxisAlignedBB aabb = te.visual.getHandler().getDiameter().getSidedBB(f);
				EnumFacing opposite = f.getOpposite();
				for(EnumFacing f2: EnumFacing.VALUES){
					if(f2!=opposite&&!(s.connectedToNode&&f==f2)) drawAtBoundingBox(aabb, f2);
				}
			}else{
				AxisAlignedBB aabb = te.visual.getHandler().getDiameter().getCenterBB();
				drawAtBoundingBox(aabb, f);
			}
		}
		GlStateManager.enableCull();
		GL11.glPopMatrix();
		if(destroyStage >= 0){
			GlStateManager.matrixMode(5890);
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(5888);
		}
		*/
	}
	
	@Override
	public void bindTexture(ResourceLocation location){
		super.bindTexture(location);
	}
	
	public void bindDestroyStages(int destroyStage){
		this.bindTexture(DESTROY_STAGES[destroyStage]);
		GlStateManager.matrixMode(GL11.GL_TEXTURE);
		GlStateManager.pushMatrix();
		GlStateManager.scale(4, 4, 1);
		GlStateManager.translate(0.0625f, 0.0625f, 0.0625f);
		GlStateManager.matrixMode(GL11.GL_MODELVIEW);
	}
	
	public void resetDestroyStages(){
		GlStateManager.matrixMode(GL11.GL_TEXTURE);
		GlStateManager.popMatrix();
		GlStateManager.matrixMode(GL11.GL_MODELVIEW);
	}
}
