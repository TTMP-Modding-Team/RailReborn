package com.tictim.railreborn.inventory;

import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

/**
 * �ſ� ������ {@link Inventory} ���� Ŭ����. Ÿ�Ͽ�ƼƼ�� ���ñ�� ���� �ν��Ͻ��� �ٿ����� ������ ���� build()�� ���� Inventory�� �ٷ� Ƣ���.
 * @see Inventory
 * @see ItemHandlerFactory
 * @see MarkDirtyListener 
 * @see AccessValidator
 * @see FieldHandler
 * @author Tictim
 */
public interface InventoryBuilder{
	default Inventory createInventory(){
		Inventory inv = newInventoryInstance();
		inv.setStackLimit(stackLimit());
		inv.name().set(invName(), translate());
		inv.setItemHandlerFactory(itemHandlerFactory());
		inv.setMarkDirtyListener(markDirtyListener());
		inv.setAccessValidator(accessValidator());
		inv.setFieldHandler(fieldHandler());
		return inv;
	}
	
	default Inventory newInventoryInstance(){
		return new InventoryFixed(size());
	}
	
	int size();
	
	default int stackLimit(){
		return 64;
	}
	
	String invName();
	
	default boolean translate(){
		return true;
	}
	
	@Nullable
	default ItemHandlerFactory itemHandlerFactory(){
		return (this instanceof ItemHandlerFactory) ? ((ItemHandlerFactory)this) : null;
	}
	
	@Nullable
	default MarkDirtyListener markDirtyListener(){
		return (this instanceof MarkDirtyListener) ? ((MarkDirtyListener)this) : null;
	}
	
	@Nullable
	default AccessValidator accessValidator(){
		return (this instanceof AccessValidator) ? ((AccessValidator)this) : null;
	}
	
	@Nullable
	default FieldHandler fieldHandler(){
		return (this instanceof FieldHandler) ? ((FieldHandler)this) : null;
	}
	
	/**
	 * {@link IItemHandler}�� �����ϴ� ���. InventoryBuilder�� ���� Ŭ������ ������ ������ �ڵ����� ����.
	 * @author Tictim
	 */
	@FunctionalInterface
	public static interface ItemHandlerFactory{
		/**
		 * Capability�� ���� ���� �� �ִ� ������ �ڵ鷯�� �����մϴ�. null�� ���ڷ� �޾� �����Ǵ� ������ �ڵ鷯�� ���� IItemHandler�� ������ ���˴ϴ�.
		 */
		IItemHandler create(Inventory inv, @Nullable EnumFacing facing);
	}
	
	public static interface SidedItemHandlerFactory extends ItemHandlerFactory, SideIOHandler{
		@Override
		default SidedInvWrapper create(Inventory inv, @Nullable EnumFacing facing){
			return new SidedInvWrapper(new SideWrappedInv(inv, this), facing);
		}
	}
	
	/**
	 * {@link IInventory#markDirty()}�� �޴� ���. InventoryBuilder�� ���� Ŭ������ ������ ������ �ڵ����� ����.
	 * @author Tictim
	 */
	@FunctionalInterface
	public static interface MarkDirtyListener{
		/**
		 * {@link IInventory#markDirty()}�� �޾Ƽ� ȣ��˴ϴ�.
		 */
		void markDirty();
	}
	
	/**
	 * {@link IInventory#isItemValidForSlot()}�� {@link IInventory#isUsableByPlayer()}�� �޴� ���. InventoryBuilder�� ���� Ŭ������ ������ ������ �ڵ����� ����.
	 * @author Tictim
	 */
	@FunctionalInterface
	public static interface AccessValidator{
		/**
		 * {@link IInventory#isItemValidForSlot()}�� �޾Ƽ� ȣ��˴ϴ�.
		 */
		boolean isItemValidForSlot(int index, ItemStack stack);
		
		/**
		 * {@link IInventory#isUsableByPlayer()}�� �޾Ƽ� ȣ��˴ϴ�.
		 */
		default boolean isUsableByPlayer(EntityPlayer player){
			return true;
		}
	}
	
	/**
	 * {@link IInventory}�� �ʵ带 �����ϴ� ���. InventoryBuilder�� ���� Ŭ������ ������ ������ �ڵ����� ����.
	 * @author Tictim
	 */
	public static interface FieldHandler{
		int getField(int id);
		void setField(int id, int value);
		int getFieldCount();
	}
}
