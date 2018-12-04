package com.tictim.railreborn.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;

public class InventoryResizable extends InventoryFixed{
	protected NonNullList<ItemStack> inv;
	
	public InventoryResizable(){
		super();
	}
	
	public InventoryResizable(int initialLength){
		super(initialLength);
	}
	
	@Override
	public NBTTagCompound serializeNBT(NBTTagCompound nbt){
		nbt.setInteger("size", this.inv.size());
		return super.serializeNBT(nbt);
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound nbt){
		this.reset(nbt.getInteger("size"));
		super.deserializeNBT(nbt);
	}
}
