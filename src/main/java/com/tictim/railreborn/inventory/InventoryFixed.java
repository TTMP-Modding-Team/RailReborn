package com.tictim.railreborn.inventory;

import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;

public class InventoryFixed extends Inventory{
	protected NonNullList<ItemStack> inv;
	
	public InventoryFixed(){
		this(0);
	}
	
	public InventoryFixed(int initialLength){
		reset(initialLength);
	}
	
	public void reset(int length){
		inv = NonNullList.withSize(length, ItemStack.EMPTY);
	}
	
	@Override
	public int getSlots(){
		return inv.size();
	}
	
	@Override
	public int getSizeInventory(){
		return inv.size();
	}
	
	@Override
	public boolean isEmpty(){
		return inv.isEmpty();
	}
	
	@Override
	public ItemStack getStackInSlot(int index){
		return inv.get(index);
	}
	
	@Override
	public ItemStack decrStackSize(int index, int count){
		ItemStack stack = ItemStackHelper.getAndSplit(inv, index, count);
		if(!stack.isEmpty()) this.markDirty();
		return stack;
	}
	
	@Override
	public ItemStack removeStackFromSlot(int index){
		return ItemStackHelper.getAndRemove(this.inv, index);
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack){
		this.inv.set(index, stack);
		if(!stack.isEmpty()&&stack.getCount()>this.getInventoryStackLimit())
			stack.setCount(this.getInventoryStackLimit());
		this.markDirty();
	}
	
	@Override
	public void clear(){
		this.inv.clear();
	}
	
	@Override
	public NBTTagCompound serializeNBT(NBTTagCompound nbt){
		
		//if(mutable) nbt.setBoolean("mutable", this.mutable);
		//else
		NBTTagList list = new NBTTagList();
		for(int i = 0; i<this.inv.size(); i++){
			ItemStack s = this.inv.get(i);
			if(!s.isEmpty()){
				NBTTagCompound subnbt = s.getTagCompound();
				subnbt.setInteger("_idx", i);
				list.appendTag(subnbt);
			}
		}
		if(!list.hasNoTags()) nbt.setTag("inventory", list);
		return super.serializeNBT(nbt);
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound nbt){
		/*
		if(this.mutable = nbt.getBoolean("mutable")){
			this.resetToMutable();
			if(nbt.hasKey("inventory", 9)){
				NBTTagList list = nbt.getTagList("inventory", 9);
				for(int i = 0; i<list.tagCount(); i++){
					NBTTagCompound subnbt = list.getCompoundTagAt(i);
					this.inv.add(new ItemStack(subnbt));
				}
			}
		}else{
		*/
		reset(nbt);
		int size = nbt.getInteger("size");
		this.reset(size);
		if(nbt.hasKey("inventory", 9)){
			NBTTagList list = nbt.getTagList("inventory", 9);
			for(int i = 0; i<list.tagCount(); i++){
				NBTTagCompound subnbt = list.getCompoundTagAt(i);
				int idx = subnbt.getInteger("_idx");
				if(idx>=0&&idx<size){
					this.inv.set(idx, new ItemStack(subnbt));
				}
			}
		}
		super.deserializeNBT(nbt);
	}
	
	protected void reset(NBTTagCompound nbt) {
		this.clear();
	}
}
