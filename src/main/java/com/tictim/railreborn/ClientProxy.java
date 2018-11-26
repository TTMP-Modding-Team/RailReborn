package com.tictim.railreborn;

import com.tictim.railreborn.item.ModItems;

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
}
