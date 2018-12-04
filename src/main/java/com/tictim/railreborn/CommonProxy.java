package com.tictim.railreborn;

import com.tictim.railreborn.tileentity.TEMultibrick;
import com.tictim.railreborn.tileentity.TEMultibrickPart;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy{
	public void registerModels(){}
	public void init(){}
	
	public void registerTileEntity(){
		GameRegistry.registerTileEntity(TEMultibrick.class, new ResourceLocation(RailReborn.MODID, "multibrick_core"));
		GameRegistry.registerTileEntity(TEMultibrickPart.class, new ResourceLocation(RailReborn.MODID, "multibrick_part"));
	}
}
