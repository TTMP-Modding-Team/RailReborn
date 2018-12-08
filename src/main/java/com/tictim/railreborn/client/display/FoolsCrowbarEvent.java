package com.tictim.railreborn.client.display;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.tictim.railreborn.RailReborn;
import com.tictim.railreborn.item.ModItems;
import com.tictim.railreborn.network.MessageDebug;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
				display.addAll(Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(getDebugString(debug.e), new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth()*2/5));
			}
		}
	}
	
	private static String getDebugString(JsonElement data){
		if(data.isJsonObject()){
			JsonObject obj = data.getAsJsonObject();
			StringBuilder stb = new StringBuilder();
			boolean flag = true;
			for(Map.Entry<String, JsonElement> e: obj.entrySet()){
				if(flag) flag = false;
				else stb.append("\n");
				stb.append(e.getKey()).append(": ");
				String s = getDebugString(e.getValue());
				if(e.getValue().isJsonArray()||e.getValue().isJsonObject()) stb.append("\n  ");
				stb.append(s.replace("\n", "\n  "));
			}
			return stb.toString();
		}else if(data.isJsonArray()){
			JsonArray arr = data.getAsJsonArray();
			StringBuilder stb = new StringBuilder();
			for(int i = 0; i<arr.size(); i++){
				if(i>0) stb.append("\n");
				stb.append(getDebugString(arr.get(i)));
			}
			return stb.toString();
		}else if(data.isJsonPrimitive()){
			JsonPrimitive p = data.getAsJsonPrimitive();
			if(p.isNumber()){
				return TextFormatting.GOLD+p.getAsString()+TextFormatting.RESET;
			}else if(p.isBoolean()){
				return (p.getAsBoolean() ? TextFormatting.GREEN+"True" : TextFormatting.RED+"False")+TextFormatting.RESET;
			}else{
				return p.getAsString()+TextFormatting.RESET;
			}
		}else if(data.isJsonNull()){
			return TextFormatting.GRAY+"null"+TextFormatting.RESET;
		}else throw new IllegalArgumentException("what the fuck are you "+data);
	}
}
