package com.tictim.railreborn.pipelink.handler;

import com.tictim.railreborn.RailReborn;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

@Mod.EventBusSubscriber(modid = RailReborn.MODID)
public final class PipeHandlers{
	private PipeHandlers(){}
	
	public static final IForgeRegistry<PipeHandler> REGISTRY = new RegistryBuilder<PipeHandler>()//
			.setIDRange(1, 255)//
			.setType(PipeHandler.class)//
			.setName(new ResourceLocation(RailReborn.MODID, "pipe_handlers"))//
			.setDefaultKey(new ResourceLocation(RailReborn.MODID, "invalid"))//
			.create();
	
	public static final PipeHandler INVALID = new PipeHandlerInvalid().setRegistryName("invalid");
	public static final PipeHandler STUFFED = new PipeHandlerInvalid().setRegistryName("stuffed");
	
	@SubscribeEvent
	public static void register(RegistryEvent.Register<PipeHandler> event){
		IForgeRegistry<PipeHandler> registry = event.getRegistry();
		registry.register(INVALID);
		registry.register(STUFFED);
	}
}
