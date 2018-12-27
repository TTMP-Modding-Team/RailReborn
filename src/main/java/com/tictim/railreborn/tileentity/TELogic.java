package com.tictim.railreborn.tileentity;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.tictim.railreborn.capability.Debugable;
import com.tictim.railreborn.logic.Logic;
import com.tictim.railreborn.util.NBTTypes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public abstract class TELogic extends TileEntity implements Debugable{
	@Nullable
	protected Logic logic;
	
	protected void resetLogic(){
		invalidateLogic();
		logic = createNewLogic();
	}
	
	public void invalidateLogic(){
		if(logic!=null) this.logic.invalidate(this, null);
	}
	
	public boolean isLogicValid(){
		return logic!=null&&logic.isValid();
	}
	
	@Nullable
	@Override
	public ITextComponent getDisplayName(){
		return logic==null ? null : logic.getDisplayName();
	}
	
	@Nullable
	protected abstract Logic createNewLogic();
	
	@Override
	public JsonElement getDebugInfo(){
		JsonObject obj = new JsonObject();
		addDebugInfo(obj);
		obj.add("", logic==null ? JsonNull.INSTANCE : logic.getDebugInfo());
		return Debugable.stateClassType(this.getClass(), obj);
	}
	
	protected void addDebugInfo(JsonObject obj){}
	
	@Override
	public boolean hasCapability(Capability<?> cap, EnumFacing facing){
		if(cap==Debugable.CAP) return true;
		return (logic!=null&&hasLogicCapability(cap, facing))||super.hasCapability(cap, facing);
	}
	
	protected boolean hasLogicCapability(Capability<?> cap, EnumFacing facing){
		return logic.hasCapability(cap, facing);
	}
	
	@Override
	@Nullable
	public <T> T getCapability(Capability<T> cap, EnumFacing facing){
		if(cap==Debugable.CAP) return (T)this;
		T t = logic!=null ? getLogicCapability(cap, facing) : null;
		return t!=null ? t : super.getCapability(cap, facing);
	}
	
	protected <T> T getLogicCapability(Capability<T> cap, EnumFacing facing){
		return logic.getCapability(cap, facing);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		if(logic!=null){
			NBTTagCompound subnbt = logic.serializeNBT();
			if(!subnbt.hasNoTags()) nbt.setTag("logic", subnbt);
		}
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		if(logic!=null&&nbt.hasKey("logic", NBTTypes.COMPOUND)){
			logic.deserializeNBT(nbt.getCompoundTag("logic"));
		}
	}
}
