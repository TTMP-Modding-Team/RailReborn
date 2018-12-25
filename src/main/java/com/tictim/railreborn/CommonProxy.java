package com.tictim.railreborn;

import com.tictim.railreborn.tileentity.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy{
	public void registerTileEntity(){
		GameRegistry.registerTileEntity(TEMultibrick.class, new ResourceLocation(RailReborn.MODID, "multibrick_core"));
		GameRegistry.registerTileEntity(TEMultiblockPart.class, new ResourceLocation(RailReborn.MODID, "multiblock_part"));
		GameRegistry.registerTileEntity(TERainTank.class, new ResourceLocation(RailReborn.MODID, "rain_tank"));
		GameRegistry.registerTileEntity(TEEngine.class, new ResourceLocation(RailReborn.MODID, "engine"));
		GameRegistry.registerTileEntity(TEPipe.class, new ResourceLocation(RailReborn.MODID, "pipe"));
	}
}
