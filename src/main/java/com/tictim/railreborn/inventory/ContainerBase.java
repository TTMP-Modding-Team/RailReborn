package com.tictim.railreborn.inventory;

import javax.annotation.Nullable;

import org.apache.commons.lang3.Validate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class ContainerBase<TE extends TileEntity> extends Container{
	protected final TE te;
	protected final IInventory inv;
	protected final EntityPlayer player;
	
	@Nullable
	private TransferActionWrapper action;
	
	/**
	 * @throws ClassCastException If {@code te} is not {@link IInventory} instance
	 */
	protected ContainerBase(TE te, EntityPlayer player){
		this(te, (IInventory)te, player);
	}
	
	protected ContainerBase(TE te, IInventory inv, EntityPlayer player){
		this.te = te;
		this.inv = inv;
		this.player = player;
	}
	
	protected void addPlayerInventory(int xOff, int yOff, boolean addTransferAction){
		addPlayerInventory(xOff, yOff, yOff+(3*18)+4, addTransferAction);
	}
	
	protected void addPlayerInventory(int xOff, int yOff, int quickslotYOff, boolean addTransferAction){
		for(int i = 0; i<3; ++i){
			for(int j = 0; j<9; ++j){
				this.addSlotToContainer(new Slot(player.inventory, j+i*9+9, xOff+j*18, yOff+i*18));
			}
		}
		
		for(int k = 0; k<9; ++k){
			this.addSlotToContainer(new Slot(player.inventory, k, xOff*18, quickslotYOff));
		}
		
		if(addTransferAction) addPlayerInvTransferAction();
	}
	
	protected void addPlayerInvTransferAction(){
		int len = this.inventorySlots.size();
		
		addTransferAction(0, len-36, 36, (slot, idx, start, end) -> {
			return this.mergeItemStack(slot.getStack(), 0, start, false);
		});
		
		addTransferAction(-1, len-36, 27, (slot, idx, start, end) -> {
			return this.mergeItemStack(slot.getStack(), end, end+9, false);
		});
		
		addTransferAction(-2, len-9, 9, (slot, idx, start, end) -> {
			return this.mergeItemStack(slot.getStack(), start-27, start, false);
		});
	}
	
	protected void addDefaultTransferAction(int priority, int slotStart, int slots){
		addTransferAction(priority, slotStart, slots, (slot, idx, start, end) -> {
			int s = this.inventorySlots.size();
			return this.mergeItemStack(slot.getStack(), s-36, s, true);
		});
	}
	
	protected void addCraftTransferAction(int priority, int slotStart, int slots){
		addTransferAction(priority, slotStart, slots, (slot, idx, start, end) -> {
			ItemStack stack = slot.getStack(), copy = stack.copy();
			int s = this.inventorySlots.size();
			if(this.mergeItemStack(stack, s-36, s, true)){
				slot.onSlotChange(stack, copy);
				return true;
			}else return false;
		});
	}
	
	protected void addTransferAction(int priority, int slotStart, int slots, TransferAction action){
		TransferActionWrapper w = new TransferActionWrapper(priority, slotStart, slots, action);
		if(this.action==null){
			this.action = w;
		}else if(this.action.priority<priority){
			w.setNext(this.action);
			this.action = w;
		}else this.action.setNext(w);
	}
	
	public TE getTE(){
		return te;
	}
	
	public IInventory getIInventory(){
		return inv;
	}
	
	public EntityPlayer getPlayer(){
		return player;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player){
		return inv.isUsableByPlayer(player);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index){
		Slot slot = this.inventorySlots.get(index);
		if(slot!=null&&slot.getHasStack()&&slot.canTakeStack(player)){
			ItemStack copy = slot.getStack().copy();
			for(TransferActionWrapper w = action; action!=null; action = action.getNext()){
				if(action.transfer(slot, index)){
					ItemStack after = slot.getStack();
					if(after.isEmpty()) slot.putStack(ItemStack.EMPTY);
					else slot.onSlotChanged();
					
					if(after.getCount()==copy.getCount()) return ItemStack.EMPTY;
					slot.onTake(player, after);
				}
			}
			return slot.getStack();
		}else return ItemStack.EMPTY;
	}
	
	@FunctionalInterface
	public static interface TransferAction<TE extends TileEntity>{
		boolean transfer(Slot slot, int index, int regionStart, int regionEnd);
	}
	
	private class TransferActionWrapper{
		private final int priority, slotStart, slots;
		private final TransferAction<TE> action;
		
		@Nullable
		private TransferActionWrapper next;
		
		private TransferActionWrapper(int priority, int slotStart, int slots, TransferAction action){
			Validate.notNull(action);
			this.priority = priority;
			this.slotStart = Math.max(0, slotStart);
			this.slots = Math.max(0, slots);
			this.action = action;
		}
		
		public void setNext(TransferActionWrapper next){
			if(this.next==null){
				this.next = next;
			}else if(this.next.priority<next.priority){
				next.setNext(this.next);
				this.next = next;
			}else this.next.setNext(next);
		}
		
		public TransferActionWrapper getNext(){
			return this.next;
		}
		
		public boolean transfer(Slot slot, int index){
			if(index >= slotStart&&index<slotStart+slots) return action.transfer(slot, index, slotStart, slotStart+slots);
			else return false;
		}
	}
	
	/*
	 * {@link #mergeItemStack(ItemStack, int, int, boolean)} but with simulate option
	protected boolean merge(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection, boolean simulate){
		if(!simulate) return mergeItemStack(stack, startIndex, endIndex, reverseDirection);
		
		if(!stack.isEmpty()){
			for(int i = reverseDirection ? endIndex-1 : startIndex; reverseDirection ? i<startIndex : i>=endIndex; i += reverseDirection ? -1 : 1){
				Slot slot = this.inventorySlots.get(i);
				ItemStack itemstack = slot.getStack();
				if(!itemstack.isEmpty()){
					if(stack.isStackable()){
						int maxSize = Math.min(slot.getSlotStackLimit(), stack.getMaxStackSize());
						if(itemstack.getCount()<maxSize&&stackable(itemstack, stack)) return true;
					}
				}else if(slot.isItemValid(stack)) return true;
			}
		}
		
		return false;
	}
	
	public static boolean stackable(ItemStack target, ItemStack object){
		return target.getItem()==object.getItem()&&(!object.getHasSubtypes()||object.getMetadata()==target.getMetadata())&&ItemStack.areItemStackTagsEqual(object, target);
	}
	 */
}
