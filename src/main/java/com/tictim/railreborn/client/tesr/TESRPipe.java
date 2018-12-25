package com.tictim.railreborn.client.tesr;

import com.tictim.railreborn.pipelink.handler.PipeHandler;
import com.tictim.railreborn.tileentity.TEPipe;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

public class TESRPipe extends TileEntitySpecialRenderer<TEPipe>{
	@Override
	public void render(TEPipe te, double x, double y, double z, float partialTicks, int destroyStage, float alpha){
		PipeHandler h = te.getHandler();
		if(h==null) return;
		
		if(destroyStage >= 0){
			this.bindTexture(DESTROY_STAGES[destroyStage]);
			GlStateManager.matrixMode(5890);
			GlStateManager.pushMatrix();
			GlStateManager.scale(4, 4, 1);
			GlStateManager.translate(0.0625f, 0.0625f, 0.0625f);
			GlStateManager.matrixMode(5888);
		}else this.bindTexture(h.getPipeTexture());
		
		GL11.glPushMatrix();
		GlStateManager.translate(x, y, z);
		GL11.glColor4d(1, 1, 1, 1);
		for(EnumFacing f: EnumFacing.VALUES){
			TEPipe.PipeSideVisual s = te.visual==null ? null : te.visual.getVisual(f);
			if(s!=null){
				AxisAlignedBB aabb = h.getDiameter().getSidedBB(f);
				EnumFacing opposite = f.getOpposite();
				for(EnumFacing f2: EnumFacing.VALUES){
					if(f2!=opposite&&!(s.connectedToNode&&f==f2)) drawAtBoundingBox(aabb, f2);
				}
			}else{
				AxisAlignedBB aabb = h.getDiameter().getCenterBB();
				drawAtBoundingBox(aabb, f);
			}
		}
		GL11.glPopMatrix();
		if(destroyStage >= 0){
			GlStateManager.matrixMode(5890);
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(5888);
		}
	}
	
	public static void drawAtBoundingBox(AxisAlignedBB aabb, EnumFacing facing){
		Tessellator t = Tessellator.getInstance();
		BufferBuilder b = t.getBuffer();
		b.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_TEX_NORMAL);
		switch(facing){
			case DOWN:
				b.pos(aabb.minX, aabb.minY, aabb.minZ).tex(aabb.maxX, aabb.minZ).normal(0, -1, 0).endVertex();
				b.pos(aabb.maxX, aabb.minY, aabb.minZ).tex(aabb.minX, aabb.minZ).normal(0, -1, 0).endVertex();
				b.pos(aabb.maxX, aabb.minY, aabb.maxZ).tex(aabb.minX, aabb.maxZ).normal(0, -1, 0).endVertex();
				b.pos(aabb.minX, aabb.minY, aabb.maxZ).tex(aabb.maxX, aabb.maxZ).normal(0, -1, 0).endVertex();
				break;
			case UP:
				b.pos(aabb.minX, aabb.maxY, aabb.minZ).tex(aabb.minX, aabb.minZ).normal(0, 1, 0).endVertex();
				b.pos(aabb.minX, aabb.maxY, aabb.maxZ).tex(aabb.minX, aabb.maxZ).normal(0, 1, 0).endVertex();
				b.pos(aabb.maxX, aabb.maxY, aabb.maxZ).tex(aabb.maxX, aabb.maxZ).normal(0, 1, 0).endVertex();
				b.pos(aabb.maxX, aabb.maxY, aabb.minZ).tex(aabb.maxX, aabb.minZ).normal(0, 1, 0).endVertex();
				break;
			case NORTH:
				b.pos(aabb.minX, aabb.minY, aabb.minZ).tex(aabb.maxX, aabb.maxY).normal(0, 0, -1).endVertex();
				b.pos(aabb.minX, aabb.maxY, aabb.minZ).tex(aabb.maxX, aabb.minY).normal(0, 0, -1).endVertex();
				b.pos(aabb.maxX, aabb.maxY, aabb.minZ).tex(aabb.minX, aabb.minY).normal(0, 0, -1).endVertex();
				b.pos(aabb.maxX, aabb.minY, aabb.minZ).tex(aabb.minX, aabb.maxY).normal(0, 0, -1).endVertex();
				break;
			case SOUTH:
				b.pos(aabb.minX, aabb.minY, aabb.maxZ).tex(aabb.minX, aabb.maxY).normal(0, 0, 1).endVertex();
				b.pos(aabb.maxX, aabb.minY, aabb.maxZ).tex(aabb.maxX, aabb.maxY).normal(0, 0, 1).endVertex();
				b.pos(aabb.maxX, aabb.maxY, aabb.maxZ).tex(aabb.maxX, aabb.minY).normal(0, 0, 1).endVertex();
				b.pos(aabb.minX, aabb.maxY, aabb.maxZ).tex(aabb.minX, aabb.minY).normal(0, 0, 1).endVertex();
				break;
			case WEST:
				b.pos(aabb.minX, aabb.minY, aabb.minZ).tex(aabb.minZ, aabb.maxY).normal(-1, 0, 0).endVertex();
				b.pos(aabb.minX, aabb.minY, aabb.maxZ).tex(aabb.maxZ, aabb.maxY).normal(-1, 0, 0).endVertex();
				b.pos(aabb.minX, aabb.maxY, aabb.maxZ).tex(aabb.maxZ, aabb.minY).normal(-1, 0, 0).endVertex();
				b.pos(aabb.minX, aabb.maxY, aabb.minZ).tex(aabb.minZ, aabb.minY).normal(-1, 0, 0).endVertex();
				break;
			case EAST:
				b.pos(aabb.maxX, aabb.minY, aabb.minZ).tex(aabb.maxZ, aabb.maxY).normal(1, 0, 0).endVertex();
				b.pos(aabb.maxX, aabb.maxY, aabb.minZ).tex(aabb.maxZ, aabb.minY).normal(1, 0, 0).endVertex();
				b.pos(aabb.maxX, aabb.maxY, aabb.maxZ).tex(aabb.minZ, aabb.minY).normal(1, 0, 0).endVertex();
				b.pos(aabb.maxX, aabb.minY, aabb.maxZ).tex(aabb.minZ, aabb.maxY).normal(1, 0, 0).endVertex();
				break;
			default:
				throw new IllegalArgumentException("facing = "+facing);
		}
		t.draw();
	}
}
