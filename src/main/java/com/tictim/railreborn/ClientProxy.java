package com.tictim.railreborn;

import com.tictim.railreborn.client.tesr.TESRPipe;
import com.tictim.railreborn.tileentity.TEPipe;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy{
	@Override
	public void registerTileEntity(){
		super.registerTileEntity();
		ClientRegistry.bindTileEntitySpecialRenderer(TEPipe.class, new TESRPipe());
		//ClientRegistry.bindTileEntitySpecialRenderer(TEMultibrick.class, new TESRMultiblockDebug<>());
	}
}
