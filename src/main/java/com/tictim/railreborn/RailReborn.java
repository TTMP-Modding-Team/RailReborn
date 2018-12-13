package com.tictim.railreborn;

import com.tictim.railreborn.api.Crowbar;
import com.tictim.railreborn.api.RJ;
import com.tictim.railreborn.capability.CapabilityStorageNothing;
import com.tictim.railreborn.capability.Debugable;
import com.tictim.railreborn.fluid.ModFluids;
import com.tictim.railreborn.item.ModItems;
import com.tictim.railreborn.multiblock.Blueprints;
import com.tictim.railreborn.network.MessageDebug;
import com.tictim.railreborn.recipe.MachineRecipes;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = RailReborn.MODID, name = RailReborn.NAME, version = RailReborn.VERSION, guiFactory = "com.tictim.railreborn.config.GuiFactory")
//@EventBusSubscriber(modid = RailReborn.MODID)
public class RailReborn{
	public static final String MODID = "railreborn";
	public static final String NAME = "Rail Reborn";
	public static final String VERSION = "0.0.0.0";
	
	private static boolean crashed;
	
	@Mod.Instance(value = MODID)
	public static RailReborn instance;
	
	@SidedProxy(modId = MODID, clientSide = "com.tictim.railreborn.ClientProxy", serverSide = "com.tictim.railreborn.CommonProxy")
	public static CommonProxy proxy;
	
	public static final SimpleNetworkWrapper NET = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
	public static final Logger LOGGER = LogManager.getLogger(NAME);
	
	{
		ModFluids.init();
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		ModItems.MISC.toString();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event){
		ModItems.registerOreDict();
		MachineRecipes.addMachineRecipes();
		Blueprints.COKE_OVEN.toString();
		
		CapabilityManager.INSTANCE.register(RJ.class, new CapabilityStorageNothing<>(), () -> null);
		CapabilityManager.INSTANCE.register(Crowbar.class, new CapabilityStorageNothing<>(), () -> null);
		CapabilityManager.INSTANCE.register(Debugable.class, new CapabilityStorageNothing<>(), () -> null);
		
		proxy.registerTileEntity();
		
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new RailRebornGuiHandler());
		
		NET.registerMessage(new MessageDebug.Client(), MessageDebug.class, 0, Side.CLIENT);
		NET.registerMessage(new MessageDebug.Server(), MessageDebug.class, 1, Side.SERVER);
	}
}
