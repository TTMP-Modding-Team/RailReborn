package com.tictim.railreborn.api;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public class RJImpl implements RJ, INBTSerializable<NBTTagCompound>{
	private long capacity, current, in, out;
	private boolean infinite;
	
	public void setCapacityRJ(long capacity){
		this.capacity = Math.max(0, capacity);
	}
	
	public void setCurrentRJ(long current){
		this.current = Math.max(0, current);
	}
	
	public void setMaxRJIn(long in){
		this.in = Math.max(0, in);
	}
	
	public void setMaxRJOut(long out){
		this.out = Math.max(0, out);
	}
	
	public void setInfinite(boolean infinite){
		this.infinite = infinite;
	}
	
	public boolean isInfinite(){
		return this.infinite;
	}
	
	@Override
	public long capacityRJ(){
		return capacity;
	}
	
	@Override
	public long currentRJ(){
		return current;
	}
	
	@Override
	public long maxRJIn(){
		return in;
	}
	
	@Override
	public long maxRJOut(){
		return out;
	}
	
	@Override
	public long insertRJ(long amount, boolean internally, boolean simulate){
		if(infinite) return 0;
		else{
			long i = Math.min(capacityRJ()-currentRJ(), internally ? amount : Math.min(amount, maxRJIn()));
			if(!simulate) this.current += i;
			return i;
		}
	}
	
	@Override
	public long extractRJ(long amount, boolean internally, boolean simulate){
		if(infinite) return Long.MAX_VALUE;
		else{
			long i = Math.min(currentRJ(), internally ? amount : Math.min(amount, maxRJOut()));
			if(!simulate) this.current += i;
			return i;
		}
	}
	
	@Override
	public NBTTagCompound serializeNBT(){
		NBTTagCompound nbt = new NBTTagCompound();
		if(this.capacity>0) nbt.setLong("max", this.capacity);
		if(this.current>0) nbt.setLong("current", this.current);
		if(this.in>0) nbt.setLong("in", this.in);
		if(this.out>0) nbt.setLong("out", this.out);
		return nbt;
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound nbt){
		this.capacity = nbt.getLong("max");
		this.current = nbt.getLong("current");
		this.in = nbt.getLong("in");
		this.out = nbt.getLong("out");
	}
}
