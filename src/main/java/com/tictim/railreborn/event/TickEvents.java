package com.tictim.railreborn.event;

import com.tictim.railreborn.RailReborn;
import com.tictim.railreborn.pipelink.handler.PipeHandler;
import com.tictim.railreborn.pipelink.handler.PipeHandlers;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber(modid = RailReborn.MODID)
public final class TickEvents{
	private TickEvents(){}
	
	@SubscribeEvent
	public static void onTick(TickEvent.WorldTickEvent event){
		if(event.phase==TickEvent.Phase.END){
			for(PipeHandler p: PipeHandlers.REGISTRY){
				p.update(event.world);
			}
		}
	}
}
