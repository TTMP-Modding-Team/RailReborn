package com.tictim.railreborn.fluid;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TypedFluidTank extends FluidTank{
	private final Fluid fluidType;
	
	public TypedFluidTank(Fluid fluid, int capacity){
		super(null, capacity);
		this.fluidType = fluid;
		Validate.notNull(fluidType);
	}
	
	public TypedFluidTank(@Nonnull FluidStack fluid, int capacity){
		super(fluid, capacity);
		this.fluidType = fluid.getFluid();
		Validate.notNull(fluidType);
	}
	
	public TypedFluidTank(Fluid fluid, int amount, int capacity){
		this(new FluidStack(fluid, amount), capacity);
	}
	
	@Override
	public boolean canDrainFluidType(@Nullable FluidStack fluid){
		return super.canDrainFluidType(fluid)&&fluid.getFluid()==fluidType;
	}
}
