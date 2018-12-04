package com.tictim.railreborn.client.tesr;

import javax.annotation.Nullable;

import com.tictim.railreborn.client.tesr.TESRMultiblockDebug.MultiblockDebugable;
import com.tictim.railreborn.multiblock.Blueprint;
import com.tictim.railreborn.multiblock.Blueprint.TestResult;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TESRMultiblockDebug<TE extends TileEntity&MultiblockDebugable> extends TileEntitySpecialRenderer<TE>{
	@Override
	public void render(TE te, double x, double y, double z, float partialTicks, int destroyStage, float alpha){
		setLightmapDisabled(true);
		TestResult test = te.getMultiblockTest();
		if(test!=null){
			String s = (test.isValid() ? TextFormatting.YELLOW : TextFormatting.GOLD).toString();
			String s2 = test.getWorld()!=null ? test.getWorld().toString() : "";
			drawNameplate(this.rendererDispatcher, s+s2, te.getPos(), 12);
			if(test.groups()>1){
				for(int i = 1; i<test.groups(); i++)
					for(BlockPos p: test.getGroup(i))
						drawNameplate(this.rendererDispatcher, s+i+":"+Blueprint.posToStr(p), p, 12);
			}else{
				for(BlockPos p: test.getGroup(0))
					drawNameplate(this.rendererDispatcher, s+"X:"+Blueprint.posToStr(p), p, 12);
			}
		}else drawNameplate(this.rendererDispatcher, TextFormatting.RED+"NO MULTIBLOCK INFO", te.getPos(), 12);
		setLightmapDisabled(false);
	}
	
	private static void drawNameplate(TileEntityRendererDispatcher d, String str, BlockPos p, int maxDistance){
		EntityRenderer.drawNameplate(d.getFontRenderer(), str, p.getX()+.5f-(float)d.staticPlayerX, p.getY()+.5f-(float)d.staticPlayerY, p.getZ()+.5f-(float)d.staticPlayerZ, 0, d.entityYaw, d.entityPitch, false, false);
	}
	
	private static String joinGroup(Iterable<BlockPos> list){
		StringBuilder stb = new StringBuilder();
		boolean flag = true;
		for(BlockPos p: list){
			if(flag) flag = false;
			else stb.append(" ");
			stb.append(Blueprint.posToStr(p));
		}
		return stb.toString();
	}
	
	public interface MultiblockDebugable{
		@SideOnly(Side.CLIENT)
		@Nullable
		TestResult getMultiblockTest();
	}
}
