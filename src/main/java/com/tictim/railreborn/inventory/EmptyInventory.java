package com.tictim.railreborn.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

public class EmptyInventory implements IInventory{
	private final Inventory.Name name;
	@Nullable
	private final InventoryBuilder.MarkDirtyListener dirtyListener;
	@Nullable
	private final InventoryBuilder.AccessValidator validator;
	@Nullable
	private final InventoryBuilder.FieldHandler field;
	
	public EmptyInventory(Inventory.Name name){
		this(name, null, null, null);
	}
	
	public EmptyInventory(Inventory.Name name, @Nullable InventoryBuilder.MarkDirtyListener m, @Nullable InventoryBuilder.AccessValidator a, @Nullable InventoryBuilder.FieldHandler f){
		this.name = name;
		this.dirtyListener = m;
		this.validator = a;
		this.field = f;
	}
	
	@Override
	public int getSizeInventory(){
		return 0;
	}
	
	@Override
	public boolean isEmpty(){
		return true;
	}
	
	@Override
	public ItemStack getStackInSlot(int index){
		return ItemStack.EMPTY;
	}
	
	@Override
	public ItemStack decrStackSize(int index, int count){
		return ItemStack.EMPTY;
	}
	
	@Override
	public ItemStack removeStackFromSlot(int index){
		return ItemStack.EMPTY;
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack){}
	
	@Override
	public int getInventoryStackLimit(){
		return 64;
	}
	
	@Override
	public void markDirty(){
		if(dirtyListener!=null) dirtyListener.markDirty();
	}
	
	@Override
	public boolean isUsableByPlayer(EntityPlayer player){
		return this.validator==null||validator.isUsableByPlayer(player);
	}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack){
		return false;
	}
	
	@Override
	public void openInventory(EntityPlayer player){}
	
	@Override
	public void closeInventory(EntityPlayer player){}
	
	@Override
	public int getField(int id){
		return field==null ? 0 : field.getField(id);
	}
	
	@Override
	public void setField(int id, int value){
		if(field!=null) field.setField(id, value);
	}
	
	@Override
	public int getFieldCount(){
		return field==null ? 0 : field.getFieldCount();
	}
	
	@Override
	public void clear(){}
	
	@Override
	public String getName(){
		return name.getName();
	}
	
	@Override
	public boolean hasCustomName(){
		return name.hasCustomName();
	}
	
	@Override
	public ITextComponent getDisplayName(){
		return name.getDisplayName();
	}
}
