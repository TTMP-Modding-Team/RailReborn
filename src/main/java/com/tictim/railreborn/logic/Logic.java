package com.tictim.railreborn.logic;

import com.tictim.railreborn.capability.Debugable;
import com.tictim.railreborn.inventory.ContainerBase;
import com.tictim.railreborn.inventory.Inventory;
import com.tictim.railreborn.multiblock.Blueprint.TestResult;
import com.tictim.railreborn.recipe.Crafting;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public abstract class Logic<TE extends TileEntity> implements ITickable, ICapabilitySerializable<NBTTagCompound>, Debugable{
	protected boolean valid;
	
	public void validate(TE te, @Nullable TestResult multiblockTest){
		this.valid = true;
		this.onValidate(te, multiblockTest);
	}
	/**
	 * Breaking block, invalidating multiblock structure, etc.
	 */
	public void invalidate(TE te, @Nullable TestResult multiblockTest){
		this.valid = false;
		this.onInvalidate(te, multiblockTest);
	}
	
	protected abstract void onValidate(TE te, @Nullable TestResult multiblockTest);
	protected abstract void onInvalidate(TE te, @Nullable TestResult multiblockTest);
	
	public abstract ContainerBase<? extends TE> getContainer(TE te, EntityPlayer player);
	@SideOnly(Side.CLIENT)
	public abstract GuiContainer getGui(TE te, EntityPlayer player);
	
	public abstract Inventory getInventory();
	
	@Nullable
	public abstract Crafting getCrafting(int idx);
	
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
	
	@Override
	@Nullable
	public <T> T getCapability(Capability<T> cap, EnumFacing facing){
		if(cap==Debugable.CAP) return (T)this;
		else return null;
	}
}
