package com.tictim.railreborn.fluid;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;

public class FluidWrapper implements IFluidHandler{
	private final IFluidHandler tank;
	private boolean doFill, doDrain;
	
	public FluidWrapper(IFluidHandler tank){
		this(tank, true, true);
	}
	
	public FluidWrapper(IFluidHandler tank, boolean doFill, boolean doDrain){
		this.tank = tank;
		this.doFill = doFill;
		this.doDrain = doDrain;
	}
	
	public FluidWrapper setFill(boolean doFill){
		this.doFill = doFill;
		return this;
	}
	
	public FluidWrapper setDrain(boolean doDrain){
		this.doDrain = doDrain;
		return this;
	}
	
	@Override
	public IFluidTankProperties[] getTankProperties(){
		return tank.getTankProperties();
	}
	
	@Override
	public int fill(FluidStack resource, boolean doFill){
		return doFill ? tank.fill(resource, doFill) : 0;
	}
	
	@Nullable
	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain){
		return doDrain ? tank.drain(resource, doDrain) : null;
	}
	
	@Nullable
	@Override
	public FluidStack drain(int maxDrain, boolean doDrain){
		return doDrain ? tank.drain(maxDrain, doDrain) : null;
	}
}
