package com.tictim.railreborn.fluid;

import com.tictim.railreborn.RailReborn;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = RailReborn.MODID)
public final class ModFluids{
	private ModFluids(){}
	
	public static final Fluid CREOSOTE_OIL = new Fluid(RailReborn.MODID+".creosote_oil", new ResourceLocation(RailReborn.MODID, "textures/block/fluid/creosote_oil_still"), new ResourceLocation(RailReborn.MODID, "textures/block/fluid/creosote_oil_flowing"));
	
	public static void init(){
		FluidRegistry.enableUniversalBucket();
		FluidRegistry.addBucketForFluid(CREOSOTE_OIL);
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void registerModels(ModelRegistryEvent event){
		fluidBlockstate = new ResourceLocation(RailReborn.MODID, "fluid");
		setFluidModelMapper(CREOSOTE_OIL);
		fluidBlockstate = null;
	}
	
	@SideOnly(Side.CLIENT)
	private static ResourceLocation fluidBlockstate;
	
	@SideOnly(Side.CLIENT)
	private static void setFluidModelMapper(Fluid fluid){
		ModelResourceLocation loc = new ModelResourceLocation(fluidBlockstate, ModFluids.CREOSOTE_OIL.getName());
		ModelLoader.setCustomStateMapper(fluid.getBlock(), new StateMapperBase(){
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state){
				return loc;
			}
		});
	}
}
