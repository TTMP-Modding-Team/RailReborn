package com.tictim.railreborn.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotInv extends Slot{
	private boolean lockInput, lockOutput;
	
	public SlotInv(IInventory inventory, int index, int xPosition, int yPosition){
		super(inventory, index, xPosition, yPosition);
	}
	
	public SlotInv lockInput(){
		this.lockInput = true;
		return this;
	}
	
	public SlotInv lockOutput(){
		this.lockOutput = true;
		return this;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack){
		return !lockInput&&this.inventory.isItemValidForSlot(this.getSlotIndex(), stack);
	}
	
	@Override
	public boolean canTakeStack(EntityPlayer playerIn){
		return !lockOutput;
	}
}
