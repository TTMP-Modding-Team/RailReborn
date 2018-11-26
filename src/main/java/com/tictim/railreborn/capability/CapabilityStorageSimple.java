package com.tictim.railreborn.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;

public class CapabilityStorageSimple<NBT extends NBTBase, CAP extends INBTSerializable<NBT>> implements Capability.IStorage<CAP>{
	@Override
	public NBTBase writeNBT(Capability<CAP> capability, CAP instance, EnumFacing side){
		return instance.serializeNBT();
	}
	
	@Override
	public void readNBT(Capability<CAP> capability, CAP instance, EnumFacing side, NBTBase nbt){
		instance.deserializeNBT((NBT)nbt);
	}
}
