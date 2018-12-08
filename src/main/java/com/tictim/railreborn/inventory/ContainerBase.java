package com.tictim.railreborn.inventory;

import com.google.common.base.MoreObjects;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class ContainerBase<TE extends TileEntity> extends Container{
	protected final TE te;
	protected final IInventory inv;
	protected final EntityPlayer player;
	
	private final List<TransferActionWrapper> actions = new LinkedList<>();
	private final int[] fields;
	
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
		this.fields = new int[inv.getFieldCount()];
		for(int i = 0; i<fields.length; ++i){
			fields[i] = inv.getField(i);
		}
	}
	
	protected void addPlayerInventory(int xOff, int yOff, boolean addTransferAction){
		addPlayerInventory(xOff, yOff, yOff+(3*18)+4, addTransferAction);
	}
	
	protected void addPlayerInventory(int xOff, int yOff, int quickslotYOff, boolean addTransferAction){
		for(int y = 0; y<3; y++){
			for(int x = 0; x<9; x++){
				this.addSlotToContainer(new Slot(player.inventory, x+y*9+9, xOff+x*18, yOff+y*18));
			}
		}
		for(int x = 0; x<9; x++){
			this.addSlotToContainer(new Slot(player.inventory, x, xOff+x*18, quickslotYOff));
		}
		
		if(addTransferAction) addPlayerInvTransferAction();
	}
	
	protected void addPlayerInvTransferAction(){
		int len = this.inventorySlots.size();
		
		addTransferAction(0, len-36, 36, (slot, idx, start, end) -> this.mergeItemStack(slot.getStack(), 0, start, false));
		addTransferAction(-1, len-36, 27, (slot, idx, start, end) -> this.mergeItemStack(slot.getStack(), end, end+9, false));
		addTransferAction(-2, len-9, 9, (slot, idx, start, end) -> this.mergeItemStack(slot.getStack(), start-27, start, false));
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
		this.actions.add(new TransferActionWrapper(priority, slotStart, slots, action));
		this.actions.sort(Comparator.naturalOrder());
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
		return te.getWorld().getTileEntity(te.getPos())==te&&player.getDistanceSq(te.getPos().getX()+.5, te.getPos().getY()+.5, te.getPos().getZ()+.5)<=64&&inv.isUsableByPlayer(player);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index){
		Slot slot = this.inventorySlots.get(index);
		if(slot!=null&&slot.getHasStack()&&slot.canTakeStack(player)){
			ItemStack copy = slot.getStack().copy();
			for(TransferActionWrapper w: actions){
				if(w.transfer(slot, index)){
					ItemStack after = slot.getStack();
					if(after.isEmpty()) slot.putStack(ItemStack.EMPTY);
					else slot.onSlotChanged();
					
					if(after.getCount()==copy.getCount()) return ItemStack.EMPTY;
					slot.onTake(player, after);
				}
			}
			return ItemStack.EMPTY;
		}else return ItemStack.EMPTY;
	}
	
	public void addListener(IContainerListener listener){
		super.addListener(listener);
		listener.sendAllWindowProperties(this, this.inv);
	}
	
	public void detectAndSendChanges(){
		super.detectAndSendChanges();
		
		for(int i = 0; i<this.listeners.size(); i++){
			IContainerListener l = this.listeners.get(i);
			for(int j = 0; j<this.fields.length; j++){
				int val = this.inv.getField(j);
				if(val!=this.fields[j]){
					this.fields[j] = val;
					l.sendWindowProperty(this, j, val);
				}
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data){
		this.inv.setField(id, data);
	}
	
	@FunctionalInterface
	public static interface TransferAction{
		boolean transfer(Slot slot, int index, int regionStart, int regionEnd);
	}
	
	private static class TransferActionWrapper implements Comparable<TransferActionWrapper>{
		private final int priority, slotStart, slots;
		private final TransferAction action;
		
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
		
		@Override
		public int compareTo(TransferActionWrapper o){
			return o.priority-this.priority;
		}
		
		@Override
		public String toString(){
			return MoreObjects.toStringHelper(this).add("priority", priority).add("slotStart", slotStart).add("slots", slots).toString();
		}
	}
}
