package com.tictim.railreborn.event;

import com.tictim.railreborn.RailReborn;
import com.tictim.railreborn.Registries;
import com.tictim.railreborn.pipelink.handler.PipeHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber(modid = RailReborn.MODID)
public final class TickEvents{
	private TickEvents(){}
	
	@SubscribeEvent
	public static void onTick(TickEvent.WorldTickEvent event){
		if(event.phase==TickEvent.Phase.END){
			for(PipeHandler p: Registries.PIPE_HANDLERS){
				p.update(event.world);
			}
		}
	}
}
