package com.tictim.railreborn.logic;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tictim.railreborn.api.RJ;
import com.tictim.railreborn.api.RailRebornAPI;
import com.tictim.railreborn.capability.Debugable;
import com.tictim.railreborn.enums.Engines;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public abstract class LogicEngine extends Logic implements RJ{
	private long current;
	private int fuelTicks;
	public float progress = 0;
	
	{
		this.valid = true;
	}
	
	@Override
	public void update(){
		if(fuelTicks<=0){
			progress = 0;
			if(current >= capacityRJ()) return;
			fuelTicks = this.fuel();
		}
		if(fuelTicks>0){
			insertRJ(getRJPerTick(), true, false);
			progress++;
			fuelTicks--;
		}
	}
	
	protected abstract Engines getEngineType();
	
	/**
	 * consumes fuel whatever it is.
	 * @return ticks that will generate energy until next fuel stage.
	 */
	protected abstract int fuel();
	
	/**
	 * @return current RJ/t generation rate.
	 */
	protected long getRJPerTick(){
		return getEngineType().genPerTick();
	}
	
	@Override
	public long currentRJ(){
		return current;
	}
	
	@Override
	public long maxRJIn(){
		return 0;
	}
	
	@Override
	public long capacityRJ(){
		return getEngineType().capacityRJ();
	}
	
	@Override
	public long maxRJOut(){
		return getEngineType().maxRJOut();
	}
	
	@Override
	public long insertRJ(long amount, boolean internally, boolean simulate){
		long i = Math.min(capacityRJ()-current, internally ? amount : Math.min(amount, maxRJIn()));
		if(!simulate) this.current += i;
		return i;
	}
	
	@Override
	public long extractRJ(long amount, boolean internally, boolean simulate){
		long i = Math.min(current, internally ? amount : Math.min(amount, maxRJOut()));
		if(!simulate) this.current += i;
		return i;
	}
	
	@Override
	public JsonElement getDebugInfo(){
		JsonObject obj = new JsonObject();
		obj.addProperty("Current RJ", currentRJ());
		return Debugable.stateClassType(this.getClass(), obj);
	}
	
	@Override
	public boolean hasCapability(Capability<?> cap, EnumFacing facing){
		if(cap==RailRebornAPI.RJ) return true;
		else return super.hasCapability(cap, facing);
	}
	
	@Override
	@Nullable
	@SuppressWarnings("unchecked")
	public <T> T getCapability(Capability<T> cap, EnumFacing facing){
		if(cap==RailRebornAPI.RJ) return (T)this;
		else return super.getCapability(cap, facing);
	}
	
	@Override
	public NBTTagCompound serializeNBT(){
		NBTTagCompound nbt = super.serializeNBT();
		if(current>0) nbt.setLong("energy", current);
		if(fuelTicks>0) nbt.setInteger("fuelTicks", fuelTicks);
		return nbt;
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound nbt){
		super.deserializeNBT(nbt);
		current = Math.min(Math.max(nbt.getLong("energy"), 0), this.capacityRJ());
		fuelTicks = nbt.getInteger("fuelTicks");
	}
}
