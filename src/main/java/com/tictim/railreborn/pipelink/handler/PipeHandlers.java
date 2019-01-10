package com.tictim.railreborn.pipelink.handler;

import com.tictim.railreborn.RailReborn;
import com.tictim.railreborn.Registries;
import com.tictim.railreborn.enums.Diameter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = RailReborn.MODID)
public final class PipeHandlers{
	private PipeHandlers(){}
	
	private static final RegistryNamespaced<ResourceLocation, PipeHandler> REGISTRY_NAMESPACED = GameData.getWrapper(PipeHandler.class);
	
	public static final PipeHandler INVALID = new PipeHandlerInvalid().setRegistryName("invalid");
	public static final PipeHandler STUFFED = new PipeHandlerInvalid().setRegistryName("stuffed");
	public static final PipeHandler D10 = new PipeHandlerObject().setRegistryName("d10").setDiameter(Diameter.D10);
	public static final PipeHandler D20 = new PipeHandlerObject().setRegistryName("d20");
	public static final PipeHandler D40 = new PipeHandlerObject().setRegistryName("d40").setDiameter(Diameter.D40);
	public static final PipeHandler D60 = new PipeHandlerObject().setRegistryName("d60").setDiameter(Diameter.D60);
	public static final PipeHandler D80 = new PipeHandlerObject().setRegistryName("d80").setDiameter(Diameter.D80);
	public static final PipeHandler D100 = new PipeHandlerObject().setRegistryName("d100").setDiameter(Diameter.D100);
	
	@SubscribeEvent
	public static void register(RegistryEvent.Register<PipeHandler> event){
		IForgeRegistry<PipeHandler> registry = event.getRegistry();
		registry.register(INVALID);
		registry.register(STUFFED);
		registry.register(D10);
		registry.register(D20);
		registry.register(D40);
		registry.register(D60);
		registry.register(D80);
		registry.register(D100);
	}
	
	public static PipeHandler fromId(short id){
		return REGISTRY_NAMESPACED.getObjectById(id);
	}
	
	public static short toId(PipeHandler handler){
		return (short)REGISTRY_NAMESPACED.getIDForObject(handler);
	}
	
	public static PipeHandler fromItem(ItemStack stack){
		NBTTagCompound nbt = stack.getTagCompound();
		if(nbt!=null){
			return Registries.PIPE_HANDLERS.getValue(new ResourceLocation(nbt.getString("pipe")));
		}else return INVALID;
	}
}
