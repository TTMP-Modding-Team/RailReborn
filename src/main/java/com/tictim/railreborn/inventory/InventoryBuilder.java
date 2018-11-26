package com.tictim.railreborn.inventory;

import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

/**
 * 매우 간단한 {@link Inventory} 설정 클래스. 타일엔티티든 뭐시기든 간에 인스턴스에 붙여놓고 구현한 다음 build()만 쓰면 Inventory가 바로 튀어나옴.
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
	 * {@link IItemHandler}를 생성하는 모듈. InventoryBuilder와 같은 클래스에 구현해 놓으면 자동으로 포함.
	 * @author Tictim
	 */
	@FunctionalInterface
	public static interface ItemHandlerFactory{
		/**
		 * Capability를 통해 사용될 수 있는 아이템 핸들러를 생성합니다. null을 인자로 받아 생성되는 아이템 핸들러는 내부 IItemHandler의 구현에 사용됩니다.
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
	 * {@link IInventory#markDirty()}를 받는 모듈. InventoryBuilder와 같은 클래스에 구현해 놓으면 자동으로 포함.
	 * @author Tictim
	 */
	@FunctionalInterface
	public static interface MarkDirtyListener{
		/**
		 * {@link IInventory#markDirty()}를 받아서 호출됩니다.
		 */
		void markDirty();
	}
	
	/**
	 * {@link IInventory#isItemValidForSlot()}과 {@link IInventory#isUsableByPlayer()}를 받는 모듈. InventoryBuilder와 같은 클래스에 구현해 놓으면 자동으로 포함.
	 * @author Tictim
	 */
	@FunctionalInterface
	public static interface AccessValidator{
		/**
		 * {@link IInventory#isItemValidForSlot()}를 받아서 호출됩니다.
		 */
		boolean isItemValidForSlot(int index, ItemStack stack);
		
		/**
		 * {@link IInventory#isUsableByPlayer()}를 받아서 호출됩니다.
		 */
		default boolean isUsableByPlayer(EntityPlayer player){
			return true;
		}
	}
	
	/**
	 * {@link IInventory}의 필드를 관리하는 모듈. InventoryBuilder와 같은 클래스에 구현해 놓으면 자동으로 포함.
	 * @author Tictim
	 */
	public static interface FieldHandler{
		int getField(int id);
		void setField(int id, int value);
		int getFieldCount();
	}
}
