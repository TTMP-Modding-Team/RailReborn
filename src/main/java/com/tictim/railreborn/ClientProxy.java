package com.tictim.railreborn;

public class ClientProxy extends CommonProxy{
	@Override
	public void registerTileEntity(){
		super.registerTileEntity();
		//ClientRegistry.bindTileEntitySpecialRenderer(TEMultibrick.class, new TESRMultiblockDebug<>());
	}
}
