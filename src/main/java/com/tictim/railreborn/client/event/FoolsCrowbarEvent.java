package com.tictim.railreborn.client.event;

import com.tictim.railreborn.RailReborn;
import com.tictim.railreborn.capability.Debugable;
import com.tictim.railreborn.item.ModItems;
import com.tictim.railreborn.network.MessageDebug;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = RailReborn.MODID, value = Side.CLIENT)
@SideOnly(Side.CLIENT)
public final class FoolsCrowbarEvent{
	private FoolsCrowbarEvent(){}
	
	private static final byte MAX_TICK = 10;
	
	private static final List<String> display = new ArrayList<>();
	@Nullable
	private static BlockPos pos;
	private static byte tick = 0;
	
	@SubscribeEvent
	public static void clientTick(TickEvent.ClientTickEvent event){
		RayTraceResult ray = Minecraft.getMinecraft().objectMouseOver;
		if(ray!=null&&ray.typeOfHit==RayTraceResult.Type.BLOCK){
			BlockPos pointing = ray.getBlockPos();
			EntityPlayer p = Minecraft.getMinecraft().player;
			if(p!=null){
				if(p.getHeldItemMainhand().getItem()==ModItems.CROWBAR_FOOL||p.getHeldItemOffhand().getItem()==ModItems.CROWBAR_FOOL){
					if(!Objects.equals(pointing, pos)){
						pos = pointing;
						display.clear();
						tick = 0;
					}
					tick();
					return;
				}
			}
		}
		if(pos!=null){
			pos = null;
			display.clear();
		}
	}
	
	@SubscribeEvent
	public static void render(RenderGameOverlayEvent.Post event){
		if(event.getType()==RenderGameOverlayEvent.ElementType.ALL){
			FontRenderer font = Minecraft.getMinecraft().fontRenderer;
			int x = event.getResolution().getScaledWidth()*3/5;
			synchronized(display){
				int y = event.getResolution().getScaledHeight()/2-display.size()*font.FONT_HEIGHT/2;
				
				for(int i = 0; i<display.size(); i++){
					String s = display.get(i);
					font.drawString(s, x, y+i*font.FONT_HEIGHT, 0xFFFFFF);
				}
			}
		}
	}
	
	private static void tick(){
		if(++tick >= MAX_TICK) tick = 0;
		if(tick==0){
			RailReborn.NET.sendToServer(new MessageDebug(pos));
		}
	}
	
	public static void updateDebugEvent(MessageDebug debug){
		if(pos==null||!pos.equals(debug.pos)) return;
		synchronized(display){
			display.clear();
			if(debug.e!=null){
				display.addAll(Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(Debugable.toDebugString(debug.e), new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth()*2/5));
			}
		}
	}
}
