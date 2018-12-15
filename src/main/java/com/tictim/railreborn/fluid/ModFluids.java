package com.tictim.railreborn.fluid;

import com.tictim.railreborn.RailReborn;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.EnumRarity;
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
	
	public static final Fluid CREOSOTE_OIL = new Fluid(RailReborn.MODID+".creosote_oil", new ResourceLocation(RailReborn.MODID, "block/fluid/creosote_oil_still"), new ResourceLocation(RailReborn.MODID, "block/fluid/creosote_oil_flowing"));
	public static final Fluid OIL = new Fluid(RailReborn.MODID+".oil", new ResourceLocation(RailReborn.MODID, "block/fluid/oil_still"), new ResourceLocation(RailReborn.MODID, "block/fluid/oil_flowing")).setViscosity(3000);
	public static final Fluid DIESEL = new Fluid(RailReborn.MODID+".diesel", new ResourceLocation(RailReborn.MODID, "block/fluid/diesel_still"), new ResourceLocation(RailReborn.MODID, "block/fluid/diesel_flowing")).setViscosity(800).setRarity(EnumRarity.RARE);
	public static final Fluid STEAM = new Fluid(RailReborn.MODID+".steam", new ResourceLocation(RailReborn.MODID, "block/fluid/steam_still"), new ResourceLocation(RailReborn.MODID, "block/fluid/steam_flowing")).setDensity(-1).setTemperature(373).setGaseous(true);
	
	public static void init(){
		FluidRegistry.enableUniversalBucket();
		FluidRegistry.addBucketForFluid(CREOSOTE_OIL);
		FluidRegistry.addBucketForFluid(OIL);
		FluidRegistry.addBucketForFluid(DIESEL);
		FluidRegistry.addBucketForFluid(STEAM);
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void registerModels(ModelRegistryEvent event){
		fluidBlockstate = new ResourceLocation(RailReborn.MODID, "fluid");
		setFluidModelMapper(CREOSOTE_OIL);
		setFluidModelMapper(OIL);
		setFluidModelMapper(DIESEL);
		setFluidModelMapper(STEAM);
		fluidBlockstate = null;
	}
	
	@SideOnly(Side.CLIENT)
	private static ResourceLocation fluidBlockstate;
	
	@SideOnly(Side.CLIENT)
	private static void setFluidModelMapper(Fluid fluid){
		ModelResourceLocation loc = new ModelResourceLocation(fluidBlockstate, fluid.getName());
		ModelLoader.setCustomStateMapper(fluid.getBlock(), new StateMapperBase(){
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state){
				return loc;
			}
		});
	}
}
