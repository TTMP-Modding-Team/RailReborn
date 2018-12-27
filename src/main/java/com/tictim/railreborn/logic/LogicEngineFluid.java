package com.tictim.railreborn.logic;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tictim.railreborn.capability.Debugable;
import com.tictim.railreborn.util.NBTTypes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nullable;

public abstract class LogicEngineFluid extends LogicEngine{
	protected final FluidTank tank = createFluidTank();
	
	{
		tank.setCanDrain(false);
	}
	
	protected abstract FluidTank createFluidTank();
	
	@Override
	protected int fuel(){
		if(tank.getFluidAmount() >= 500){
			tank.drainInternal(500, true);
			return fuelTime();
		}else return 0;
	}
	
	protected abstract int fuelTime();
	
	@Override
	public NBTTagCompound serializeNBT(){
		NBTTagCompound nbt = super.serializeNBT();
		NBTTagCompound subnbt = tank.writeToNBT(new NBTTagCompound());
		if(!subnbt.hasNoTags()) nbt.setTag("fluidTank", subnbt);
		return nbt;
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound nbt){
		super.deserializeNBT(nbt);
		if(nbt.hasKey("fluidTank", NBTTypes.COMPOUND)) tank.readFromNBT(nbt.getCompoundTag("fluidTank"));
	}
	
	@Override
	public JsonElement getDebugInfo(){
		JsonObject obj = new JsonObject();
		obj.add("Fluid Tank", Debugable.debugFluidTank(this.tank));
		obj.addProperty("Current RJ", currentRJ());
		return Debugable.stateClassType(this.getClass(), obj);
	}
	
	@Override
	public boolean hasCapability(Capability<?> cap, EnumFacing facing){
		if(cap==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) return true;
		else return super.hasCapability(cap, facing);
	}
	
	@Override
	@Nullable
	@SuppressWarnings("unchecked")
	public <T> T getCapability(Capability<T> cap, EnumFacing facing){
		if(cap==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY){
			return (T)this.tank;
		}else return super.getCapability(cap, facing);
	}
}
