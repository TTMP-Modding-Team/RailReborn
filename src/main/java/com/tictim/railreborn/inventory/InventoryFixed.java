package com.tictim.railreborn.inventory;

import com.tictim.railreborn.util.NBTTypes;
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
		if(!stack.isEmpty()&&stack.getCount()>this.getInventoryStackLimit()) stack.setCount(this.getInventoryStackLimit());
		this.markDirty();
	}
	
	@Override
	public void clear(){
		this.inv.clear();
	}
	
	@Override
	public NBTTagCompound serializeNBT(NBTTagCompound nbt){
		NBTTagList list = new NBTTagList();
		for(int i = 0; i<this.inv.size(); i++){
			ItemStack s = this.inv.get(i);
			if(!s.isEmpty()){
				NBTTagCompound subnbt = s.serializeNBT();
				subnbt.setInteger("_idx", i);
				list.appendTag(subnbt);
			}
		}
		if(!list.hasNoTags()) nbt.setTag("inventory", list);
		return super.serializeNBT(nbt);
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound nbt){
		if(nbt.hasKey("inventory", NBTTypes.LIST)){
			NBTTagList list = nbt.getTagList("inventory", NBTTypes.COMPOUND);
			for(int i = 0; i<list.tagCount(); i++){
				NBTTagCompound subnbt = list.getCompoundTagAt(i);
				int idx = subnbt.getInteger("_idx");
				if(idx >= 0&&idx<inv.size()){
					this.inv.set(idx, new ItemStack(subnbt));
				}
			}
		}
		super.deserializeNBT(nbt);
	}
}
