package com.tictim.railreborn;

import com.tictim.railreborn.client.tesr.TESRMultiblockDebug;
import com.tictim.railreborn.item.ModItems;
import com.tictim.railreborn.tileentity.TEMultibrick;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy{
	private boolean init;
	
	@Override
	public void registerModels(){
		if(!init) ModItems.registerModels();
	}
	
	@Override
	public void init(){
		init = true;
	}
	
	@Override
	public void registerTileEntity(){
		super.registerTileEntity();
		//ClientRegistry.bindTileEntitySpecialRenderer(TEMultibrick.class, new TESRMultiblockDebug<>());
	}
}
