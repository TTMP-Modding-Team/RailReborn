package com.tictim.railreborn.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;

public class SideWrappedInv implements ISidedInventory{
	private final IInventory inv;
	private final SideIOHandler sideHandler;
	
	public SideWrappedInv(IInventory inv){
		this(inv, DefaultSideHandler.INSTANCE);
	}
	
	public SideWrappedInv(IInventory inv, SideIOHandler sideHandler){
		this.inv = inv;
		this.sideHandler = sideHandler;
	}
	
	public IInventory inventory(){
		return this.inv;
	}
	
	@Override
	public int getSizeInventory(){
		return this.inv.getSizeInventory();
	}
	
	@Override
	public boolean isEmpty(){
		return this.inv.isEmpty();
	}
	
	@Override
	public ItemStack getStackInSlot(int index){
		return this.inv.getStackInSlot(index);
	}
	
	@Override
	public ItemStack decrStackSize(int index, int count){
		return this.inv.decrStackSize(index, count);
	}
	
	@Override
	public ItemStack removeStackFromSlot(int index){
		return this.inv.removeStackFromSlot(index);
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack){
		this.inv.setInventorySlotContents(index, stack);
	}
	
	@Override
	public int getInventoryStackLimit(){
		return this.inv.getInventoryStackLimit();
	}
	
	@Override
	public void markDirty(){
		this.inv.markDirty();
	}
	
	@Override
	public boolean isUsableByPlayer(EntityPlayer player){
		return this.inv.isUsableByPlayer(player);
	}
	
	@Override
	public void openInventory(EntityPlayer player){
		this.inv.openInventory(player);
	}
	
	@Override
	public void closeInventory(EntityPlayer player){
		this.inv.closeInventory(player);
	}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack){
		return this.inv.isItemValidForSlot(index, stack);
	}
	
	@Override
	public int getField(int id){
		return this.inv.getField(id);
	}
	
	@Override
	public void setField(int id, int value){
		this.inv.setField(id, value);
	}
	
	@Override
	public int getFieldCount(){
		return this.inv.getFieldCount();
	}
	
	@Override
	public void clear(){
		this.inv.clear();
	}
	
	@Override
	public String getName(){
		return this.inv.getName();
	}
	
	@Override
	public boolean hasCustomName(){
		return this.inv.hasCustomName();
	}
	
	@Override
	public ITextComponent getDisplayName(){
		return this.inv.getDisplayName();
	}
	
	private int[] allSlots;
	
	@Override
	public int[] getSlotsForFace(EnumFacing side){
		if(allSlots==null||allSlots.length!=getSizeInventory()){
			allSlots = new int[getSizeInventory()];
			for(int i = 0; i<allSlots.length; i++)
				allSlots[i] = i;
		}
		return allSlots;
	}
	
	@Override
	public boolean canInsertItem(int index, ItemStack stack, EnumFacing direction){
		return this.sideHandler.canInsertItem(inv, index, stack, direction);
	}
	
	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction){
		return this.sideHandler.canExtractItem(inv, index, stack, direction);
	}
}
