package com.tictim.railreborn.client.event;

import com.tictim.railreborn.RailReborn;
import com.tictim.railreborn.util.Microblock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = RailReborn.MODID, value = Side.CLIENT)
@SideOnly(Side.CLIENT)
public final class MicroblockEvent{
	private MicroblockEvent(){}
	
	@SubscribeEvent
	public static void highlight(DrawBlockHighlightEvent event){
		if(event.getTarget().typeOfHit==RayTraceResult.Type.BLOCK){
			World world = Minecraft.getMinecraft().world;
			BlockPos pos = event.getTarget().getBlockPos();
			IBlockState state = world.getBlockState(pos);
			if(state.getBlock() instanceof Microblock){
				Microblock m = (Microblock)state.getBlock();
				AxisAlignedBB aabb = Microblock.getCollidedAABB(m.getBoundingBoxes(world, pos, state), event.getTarget().hitVec.subtract(pos.getX(), pos.getY(), pos.getZ()));
				if(aabb!=null){
					GlStateManager.enableBlend();
					GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
					GlStateManager.glLineWidth(2.0F);
					GlStateManager.disableTexture2D();
					GlStateManager.depthMask(false);
					EntityPlayer player = event.getPlayer();
					double x = player.lastTickPosX+(player.posX-player.lastTickPosX)*event.getPartialTicks();
					double y = player.lastTickPosY+(player.posY-player.lastTickPosY)*event.getPartialTicks();
					double z = player.lastTickPosZ+(player.posZ-player.lastTickPosZ)*event.getPartialTicks();
					RenderGlobal.drawSelectionBoundingBox(aabb.grow(0.002).offset(pos.getX()-x, pos.getY()-y, pos.getZ()-z), 0, 0, 0, .4f);
					GlStateManager.depthMask(true);
					GlStateManager.enableTexture2D();
					GlStateManager.disableBlend();
				}
				event.setCanceled(true);
			}
		}
	}
}
