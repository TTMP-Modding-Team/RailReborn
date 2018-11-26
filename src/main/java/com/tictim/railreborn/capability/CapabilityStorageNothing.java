package com.tictim.railreborn.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class CapabilityStorageNothing implements Capability.IStorage{
	@Override
	public NBTBase writeNBT(Capability capability, Object instance, EnumFacing side){
		return null;
	}

	@Override
	public void readNBT(Capability capability, Object instance, EnumFacing side, NBTBase nbt){}
}
