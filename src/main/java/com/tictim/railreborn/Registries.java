package com.tictim.railreborn;

import com.tictim.railreborn.pipelink.attachment.provider.PipeAttachmentProvider;
import com.tictim.railreborn.pipelink.handler.PipeHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

public final class Registries{
	private Registries(){}
	
	public static final IForgeRegistry<PipeHandler> PIPE_HANDLERS = new RegistryBuilder<PipeHandler>()//
			.setIDRange(0, Short.MAX_VALUE)//
			.setType(PipeHandler.class)//
			.setName(new ResourceLocation(RailReborn.MODID, "pipe_handlers"))//
			.setDefaultKey(new ResourceLocation(RailReborn.MODID, "invalid"))//
			.addCallback(createNamespacedDefaultedWrapperCallback())//
			.create();
	
	public static final IForgeRegistry<PipeAttachmentProvider> PIPE_ATTACHMENTS = new RegistryBuilder<PipeAttachmentProvider>()//
			.setIDRange(0, 255)//
			.setType(PipeAttachmentProvider.class)//
			.setName(new ResourceLocation(RailReborn.MODID, "pipe_attachments"))//
			.setDefaultKey(new ResourceLocation(RailReborn.MODID, "invalid"))//
			.create();
	
	public static Object createNamespacedDefaultedWrapperCallback(){
		try{
			return Class.forName("net.minecraftforge.registries.NamespacedDefaultedWrapper$Factory", true, GameData.class.getClassLoader()).newInstance();
		}catch(RuntimeException e){
			throw e;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
}
