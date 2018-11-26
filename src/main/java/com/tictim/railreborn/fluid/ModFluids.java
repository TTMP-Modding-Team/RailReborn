package com.tictim.railreborn.fluid;

import com.tictim.railreborn.RailReborn;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public final class ModFluids{
	private ModFluids(){}
	
	public static final Fluid CREOSOTE_OIL = new Fluid(RailReborn.MODID+".creosote_oil", new ResourceLocation(RailReborn.MODID, "textures/block/fluid/creosote_oil_still"), new ResourceLocation(RailReborn.MODID, "textures/block/fluid/creosote_oil_flowing"));
	
	public static void init(){
		FluidRegistry.enableUniversalBucket();
		FluidRegistry.registerFluid(CREOSOTE_OIL);
	}
}
