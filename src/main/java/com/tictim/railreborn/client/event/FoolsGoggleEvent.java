package com.tictim.railreborn.client.event;

import com.tictim.railreborn.RailReborn;
import com.tictim.railreborn.item.ModItems;
import com.tictim.railreborn.network.MessagePipeData;
import com.tictim.railreborn.network.MessagePipeRequest;
import com.tictim.railreborn.pipelink.PipeNode;
import com.tictim.railreborn.pipelink.attachment.PipeAttachment;
import com.tictim.railreborn.tileentity.TEPipe;
import com.tictim.railreborn.pipelink.WorldPipeLink;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

/**
 * @deprecated
 */
//@Mod.EventBusSubscriber(modid = RailReborn.MODID, value = Side.CLIENT)
@SideOnly(Side.CLIENT)
public final class FoolsGoggleEvent{
	private FoolsGoggleEvent(){}
	
	private static WorldPipeLink wpl;
	
	@Nullable
	private static BlockPos latestPointing;
	private static short tick = 0;
	
	@SubscribeEvent
	public static void clientTick(TickEvent.ClientTickEvent event){
		EntityPlayer p = Minecraft.getMinecraft().player;
		if(p!=null&&p.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem()==ModItems.FOOLS_GOGGLE){
			if(wpl==null||(wpl.getWorld()!=p.world)){ // &&wpl.getWorld().provider.getDimension()!=p.world.provider.getDimension()
				wpl = new WorldPipeLink(p.world);
			}
			RayTraceResult ray = Minecraft.getMinecraft().objectMouseOver;
			if(ray!=null&&ray.typeOfHit==RayTraceResult.Type.BLOCK){
				BlockPos pointing = ray.getBlockPos();
				if(latestPointing==pointing){
					if(++tick>200){
						tick = 0;
						RailReborn.NET.sendToServer(new MessagePipeRequest(pointing));
					}
				}else{
					TileEntity te = p.world.getTileEntity(pointing);
					if(te instanceof TEPipe){
						latestPointing = pointing;
						tick = 0;
						RailReborn.NET.sendToServer(new MessagePipeRequest(pointing));
					}
				}
				return;
			}
		}else if(wpl!=null) wpl = null;
		latestPointing = null;
	}
	
	@SubscribeEvent
	public static void onTick(RenderWorldLastEvent event){
		WorldPipeLink wpl = FoolsGoggleEvent.wpl;
		if(wpl==null) return;
		else synchronized(wpl){
			if(wpl.map().isEmpty()) return;
		}
		GL11.glPushMatrix();
		final Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
		GL11.glTranslated(-(entity.lastTickPosX+(entity.posX-entity.lastTickPosX)*event.getPartialTicks()), -(entity.lastTickPosY+(entity.posY-entity.lastTickPosY)*event.getPartialTicks()), -(entity.lastTickPosZ+(entity.posZ-entity.lastTickPosZ)*event.getPartialTicks()));
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.disableTexture2D();
		GlStateManager.disableDepth();
		GL11.glLineWidth(4f);
		GlStateManager.color(1, 1, 0);
		synchronized(wpl){
			for(PipeNode node: wpl.map().values())
				draw(node);
		}
		GlStateManager.disableBlend();
		GlStateManager.enableTexture2D();
		GlStateManager.enableDepth();
		GL11.glPopMatrix();
	}
	
	private static void draw(PipeNode node){
		if(node.isLoaded()){
			GL11.glPushMatrix();
			GL11.glTranslated(node.getPos().getX()+.5, node.getPos().getY()+.5, node.getPos().getZ()+.5);
			//int color = node.getLink().getPipeHandler().getColor();
			//GlStateManager.color((color<<16&0xFF)/255f, (color<<8&0xFF)/255f, (color&0xFF)/255f);
			for(EnumFacing f: EnumFacing.VALUES){
				PipeAttachment a = node.getAttachment(f);
				PipeNode n = node.getConnectedNode(f);
				
				if(a!=null||n!=null){
					GL11.glPushMatrix();
					switch(f){
						case DOWN:
							GL11.glRotated(180, 1, 0, 0);
							break;
						case NORTH:
							GL11.glRotated(270, 1, 0, 0);
							break;
						case SOUTH:
							GL11.glRotated(90, 1, 0, 0);
							break;
						case WEST:
							GL11.glRotated(90, 0, 0, 1);
							break;
						case EAST:
							GL11.glRotated(270, 0, 0, 1);
							break;
						// case UP:
					}
					GL11.glBegin(GL11.GL_LINES);
					if(a!=null){
						GL11.glVertex3d(.5, .5, .5);
						GL11.glVertex3d(.5, .5, -.5);
						GL11.glVertex3d(.5, .5, -.5);
						GL11.glVertex3d(-.5, .5, -.5);
						GL11.glVertex3d(-.5, .5, -.5);
						GL11.glVertex3d(-.5, .5, .5);
						GL11.glVertex3d(-.5, .5, .5);
						GL11.glVertex3d(.5, .5, .5);
					}
					if(n!=null){
						GL11.glVertex3d(0, 0, 0);
						GL11.glVertex3d(0, .5, 0);
					}
					GL11.glEnd();
					GL11.glPopMatrix();
				}
			}
			GL11.glPopMatrix();
		}
	}
	
	public static void update(MessagePipeData message){
		WorldPipeLink wpl = FoolsGoggleEvent.wpl;
		if(wpl==null) return;
		else synchronized(wpl){
			/*
			for(PipeLink l: pipes){
				if(l.containsNode(message.pos)){
					pipes.remove(l);
					break;
				}
			}
			pipes.add(message.link);
			*/
		}
	}
}
