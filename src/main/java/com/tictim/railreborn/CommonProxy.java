package com.tictim.railreborn;

import com.tictim.railreborn.tileentity.TEEngine;
import com.tictim.railreborn.tileentity.TEMultiblockPart;
import com.tictim.railreborn.tileentity.TEMultibrick;
import com.tictim.railreborn.tileentity.TERainTank;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy{
	public void registerTileEntity(){
		GameRegistry.registerTileEntity(TEMultibrick.class, new ResourceLocation(RailReborn.MODID, "multibrick_core"));
		GameRegistry.registerTileEntity(TEMultiblockPart.class, new ResourceLocation(RailReborn.MODID, "multiblock_part"));
		GameRegistry.registerTileEntity(TERainTank.class, new ResourceLocation(RailReborn.MODID, "rain_tank"));
		GameRegistry.registerTileEntity(TEEngine.class, new ResourceLocation(RailReborn.MODID, "engine"));
	}
}
