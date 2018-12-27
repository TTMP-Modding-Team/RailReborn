package com.tictim.railreborn.inventory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tictim.railreborn.capability.Debugable;
import com.tictim.railreborn.inventory.InventoryBuilder.AccessValidator;
import com.tictim.railreborn.inventory.InventoryBuilder.FieldHandler;
import com.tictim.railreborn.inventory.InventoryBuilder.ItemHandlerFactory;
import com.tictim.railreborn.inventory.InventoryBuilder.MarkDirtyListener;
import com.tictim.railreborn.util.NBTTypes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IWorldNameable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.Map;

/**
 * {@link InventoryBasic}보다 낫다
 * @author Tictim
 * @see InventoryBuilder
 */
public abstract class Inventory implements IInventory, IItemHandlerModifiable, INBTSerializable<NBTTagCompound>, Debugable{
	protected IItemHandler delegate = new InvWrapper(this);
	protected final Name name = new Name();
	protected int stackLimit = 64;
	
	/*
	public void resetToMutable(){
		inv = NonNullList.from(ItemStack.EMPTY);
		mutable = true;
	}
	*/
	public void setStackLimit(int value){
		stackLimit = MathHelper.clamp(value, 1, 64);
	}
	
	@Nullable
	private ItemHandlerFactory ihfactory;
	@Nullable
	private IItemHandler nullSideHandler;
	@Nullable
	private Map<EnumFacing, IItemHandler> cache;
	
	public void setItemHandlerFactory(@Nullable ItemHandlerFactory ihfactory){
		if(this.ihfactory!=ihfactory){
			this.ihfactory = ihfactory;
			//delegate = ihfactory==null ? new InvWrapper(this) : ihfactory.create(this, null);
			cache = null;
		}
	}
	
	public IItemHandler create(@Nullable EnumFacing facing){
		if(ihfactory==null) return delegate;
		
		if(facing==null){
			if(nullSideHandler==null) nullSideHandler = ihfactory.create(this, null);
			return nullSideHandler;
		}else if(cache!=null){
			if(cache.containsKey(facing)) return cache.get(facing);
		}else cache = new EnumMap<>(EnumFacing.class);
		IItemHandler i = ihfactory.create(this, facing);
		cache.put(facing, i);
		return i;
	}
	
	public Name name(){
		return name;
	}
	
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
	
	@Override
	public void setStackInSlot(int slot, ItemStack stack){
		this.setInventorySlotContents(slot, stack);
	}
	
	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate){
		return delegate.insertItem(slot, stack, simulate);
	}
	
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate){
		return delegate.extractItem(slot, amount, simulate);
	}
	
	@Override
	public int getSlots(){
		return this.getSizeInventory();
	}
	
	@Override
	public int getSlotLimit(int slot){
		return this.stackLimit;
	}
	
	@Override
	public int getInventoryStackLimit(){
		return this.stackLimit;
	}
	
	@Nullable
	private MarkDirtyListener dirtyListener;
	
	public void setMarkDirtyListener(@Nullable MarkDirtyListener dirtyListener){
		this.dirtyListener = dirtyListener;
	}
	
	@Override
	public void markDirty(){
		if(dirtyListener!=null) dirtyListener.markDirty();
	}
	
	@Nullable
	private AccessValidator validator;
	
	public void setAccessValidator(@Nullable AccessValidator validator){
		this.validator = validator;
	}
	
	@Override
	public boolean isUsableByPlayer(EntityPlayer player){
		return this.validator==null||validator.isUsableByPlayer(player);
	}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack){
		return this.validator==null||validator.isItemValidForSlot(index, stack);
	}
	
	@Override
	public void openInventory(EntityPlayer player){}
	
	@Override
	public void closeInventory(EntityPlayer player){}
	
	@Nullable
	private FieldHandler field;
	
	public void setFieldHandler(@Nullable FieldHandler field){
		this.field = field;
	}
	
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
	public JsonElement getDebugInfo(){
		JsonObject obj = new JsonObject();
		obj.addProperty("Size", this.getSizeInventory());
		obj.add("Items", Debugable.debugItemStacks(this));
		return Debugable.stateClassType(this.getClass(), obj);
	}
	
	@Override
	public NBTTagCompound serializeNBT(){
		return serializeNBT(new NBTTagCompound());
	}
	
	public NBTTagCompound serializeNBT(NBTTagCompound nbt){
		{
			NBTTagCompound name = this.name.serializeNBT();
			if(!name.hasNoTags()) nbt.setTag("name", name);
		}
		if(stackLimit<64){
			nbt.setByte("stackLimit", (byte)stackLimit);
		}
		return nbt;
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound nbt){
		this.name.deserializeNBT(nbt.getCompoundTag("name"));
		setStackLimit(nbt.hasKey("stackLimit", NBTTypes.BYTE) ? nbt.getByte("stackLimit") : 64);
	}
	
	public static final class Name implements IWorldNameable, INBTSerializable<NBTTagCompound>{
		private final String initialText;
		private final boolean initialTranslate;
		private String text;
		private boolean translate;
		private boolean disableSave;
		
		public Name(){
			this("", false);
		}
		
		public Name(String text){
			this(text, false);
		}
		
		public Name(String text, boolean translate){
			Validate.notNull(text);
			set(text, translate);
			this.initialText = text;
			this.initialTranslate = translate;
		}
		
		public void set(Name name){
			set(name.text, name.translate);
		}
		
		public void set(String text){
			set(text, false);
		}
		
		public void set(String text, boolean translate){
			Validate.notNull(text);
			this.text = text;
			this.translate = translate;
		}
		
		public void setEnableSave(boolean enableSave){
			this.disableSave = !enableSave;
		}
		
		public void enableSave(){
			this.disableSave = false;
		}
		
		public void disableSave(){
			this.disableSave = true;
		}
		
		@Override
		public String getName(){
			return text;
		}
		
		@Override
		public boolean hasCustomName(){
			return !translate;
		}
		
		@Override
		public ITextComponent getDisplayName(){
			return translate ? new TextComponentTranslation(text) : new TextComponentString(text);
		}
		
		@Override
		public NBTTagCompound serializeNBT(){
			NBTTagCompound nbt = new NBTTagCompound();
			if(!disableSave&&(!initialText.equals(text)||initialTranslate!=translate)){
				nbt.setString("text", text);
				if(translate) nbt.setBoolean("translate", true);
			}
			return nbt;
		}
		
		@Override
		public void deserializeNBT(NBTTagCompound nbt){
			if(nbt.hasKey("text", NBTTypes.STRING)){
				text = nbt.getString("text");
				translate = nbt.getBoolean("translate");
			}
		}
	}
}
