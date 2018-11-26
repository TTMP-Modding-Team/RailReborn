package com.tictim.railreborn.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public interface SideIOHandler{
	default boolean canInsertItem(IInventory inv, int index, ItemStack stack, EnumFacing direction){
		return inv.isItemValidForSlot(index, stack);
	}
	
	default boolean canExtractItem(IInventory inv, int index, ItemStack stack, EnumFacing direction){
		return true;
	}
}