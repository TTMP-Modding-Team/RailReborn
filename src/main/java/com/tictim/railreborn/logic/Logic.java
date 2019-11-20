package com.tictim.railreborn.logic;

import com.tictim.railreborn.capability.Debugable;
import com.tictim.railreborn.multiblock.Blueprint.TestResult;
import com.tictim.railreborn.recipe.Crafting;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public abstract class Logic implements ITickable, ICapabilitySerializable<NBTTagCompound>, Debugable{
	protected boolean valid;
	
	public void validate(TileEntity te, @Nullable TestResult multiblockTest){
		if(!this.valid){
			this.valid = true;
			this.onValidate(te, multiblockTest);
		}
	}
	/**
	 * Breaking block, invalidating multiblock structure, etc.
	 */
	public void invalidate(TileEntity te, @Nullable TestResult multiblockTest){
		if(this.valid){
			this.valid = false;
			this.onInvalidate(te, multiblockTest);
		}
	}
	
	public boolean isValid(){
		return this.valid;
	}
	
	protected void onValidate(TileEntity te, @Nullable TestResult multiblockTest){}
	protected void onInvalidate(TileEntity te, @Nullable TestResult multiblockTest){}
	
	@Nullable
	public Container getContainer(TileEntity te, EntityPlayer player){ return null; }
	@Nullable
	@SideOnly(Side.CLIENT)
	public GuiContainer getGui(TileEntity te, EntityPlayer player){ return null; }
	
	public abstract ITextComponent getDisplayName();
	
	@Nullable
	public Crafting getCrafting(int idx){ return null; }
	
	@Override
	public NBTTagCompound serializeNBT(){
		NBTTagCompound nbt = new NBTTagCompound();
		if(valid) nbt.setBoolean("valid", true);
		return nbt;
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound nbt){
		valid = nbt.getBoolean("valid");
	}
	
	@Override
	public boolean hasCapability(Capability<?> cap, EnumFacing facing){
		if(cap==Debugable.CAP) return true;
		else return false;
	}
	
	@Nullable
	@Override
	public <T> T getCapability(Capability<T> cap, EnumFacing facing){
		if(cap==Debugable.CAP) return (T)this;
		else return null;
	}

	public int getIntData() {return 0;	}
}
