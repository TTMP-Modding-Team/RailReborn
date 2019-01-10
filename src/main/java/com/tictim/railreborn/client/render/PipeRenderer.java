package com.tictim.railreborn.client.render;

import com.tictim.railreborn.client.tesr.TESRPipe;
import com.tictim.railreborn.tileentity.TEPipe;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
@FunctionalInterface
public interface PipeRenderer{
	void render(TESRPipe tesr, TEPipe.PipeVisual visual, float partialTicks, int destroyStage);
	
	static void drawAtBoundingBox(AxisAlignedBB aabb, EnumFacing facing){
		Tessellator t = Tessellator.getInstance();
		BufferBuilder b = t.getBuffer();
		b.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_TEX_NORMAL);
		switch(facing){
			case DOWN:{
				double maxU = -aabb.minX+1, minU = -aabb.maxX+1;
				b.pos(aabb.minX, aabb.minY, aabb.minZ).tex(maxU, aabb.minZ).normal(0, -1, 0).endVertex();
				b.pos(aabb.maxX, aabb.minY, aabb.minZ).tex(minU, aabb.minZ).normal(0, -1, 0).endVertex();
				b.pos(aabb.maxX, aabb.minY, aabb.maxZ).tex(minU, aabb.maxZ).normal(0, -1, 0).endVertex();
				b.pos(aabb.minX, aabb.minY, aabb.maxZ).tex(maxU, aabb.maxZ).normal(0, -1, 0).endVertex();
				break;
			}
			case UP:{
				b.pos(aabb.minX, aabb.maxY, aabb.minZ).tex(aabb.minX, aabb.minZ).normal(0, 1, 0).endVertex();
				b.pos(aabb.minX, aabb.maxY, aabb.maxZ).tex(aabb.minX, aabb.maxZ).normal(0, 1, 0).endVertex();
				b.pos(aabb.maxX, aabb.maxY, aabb.maxZ).tex(aabb.maxX, aabb.maxZ).normal(0, 1, 0).endVertex();
				b.pos(aabb.maxX, aabb.maxY, aabb.minZ).tex(aabb.maxX, aabb.minZ).normal(0, 1, 0).endVertex();
				break;
			}
			case NORTH:{
				double maxU = -aabb.minX+1, minU = -aabb.maxX+1;
				double maxV = -aabb.minY+1, minV = -aabb.maxY+1;
				b.pos(aabb.minX, aabb.minY, aabb.minZ).tex(maxU, maxV).normal(0, 0, -1).endVertex();
				b.pos(aabb.minX, aabb.maxY, aabb.minZ).tex(maxU, minV).normal(0, 0, -1).endVertex();
				b.pos(aabb.maxX, aabb.maxY, aabb.minZ).tex(minU, minV).normal(0, 0, -1).endVertex();
				b.pos(aabb.maxX, aabb.minY, aabb.minZ).tex(minU, maxV).normal(0, 0, -1).endVertex();
				break;
			}
			case SOUTH:{
				double maxV = -aabb.minY+1, minV = -aabb.maxY+1;
				b.pos(aabb.minX, aabb.minY, aabb.maxZ).tex(aabb.minX, maxV).normal(0, 0, 1).endVertex();
				b.pos(aabb.maxX, aabb.minY, aabb.maxZ).tex(aabb.maxX, maxV).normal(0, 0, 1).endVertex();
				b.pos(aabb.maxX, aabb.maxY, aabb.maxZ).tex(aabb.maxX, minV).normal(0, 0, 1).endVertex();
				b.pos(aabb.minX, aabb.maxY, aabb.maxZ).tex(aabb.minX, minV).normal(0, 0, 1).endVertex();
				break;
			}
			case WEST:{
				double maxV = -aabb.minY+1, minV = -aabb.maxY+1;
				b.pos(aabb.minX, aabb.minY, aabb.minZ).tex(aabb.minZ, maxV).normal(-1, 0, 0).endVertex();
				b.pos(aabb.minX, aabb.minY, aabb.maxZ).tex(aabb.maxZ, maxV).normal(-1, 0, 0).endVertex();
				b.pos(aabb.minX, aabb.maxY, aabb.maxZ).tex(aabb.maxZ, minV).normal(-1, 0, 0).endVertex();
				b.pos(aabb.minX, aabb.maxY, aabb.minZ).tex(aabb.minZ, minV).normal(-1, 0, 0).endVertex();
				break;
			}
			case EAST:{
				double maxU = -aabb.minZ+1, minU = -aabb.maxZ+1;
				double maxV = -aabb.minY+1, minV = -aabb.maxY+1;
				b.pos(aabb.maxX, aabb.minY, aabb.minZ).tex(maxU, maxV).normal(1, 0, 0).endVertex();
				b.pos(aabb.maxX, aabb.maxY, aabb.minZ).tex(maxU, minV).normal(1, 0, 0).endVertex();
				b.pos(aabb.maxX, aabb.maxY, aabb.maxZ).tex(minU, minV).normal(1, 0, 0).endVertex();
				b.pos(aabb.maxX, aabb.minY, aabb.maxZ).tex(minU, maxV).normal(1, 0, 0).endVertex();
				break;
			}
			default:
				throw new IllegalArgumentException("facing = "+facing);
		}
		t.draw();
	}
}
