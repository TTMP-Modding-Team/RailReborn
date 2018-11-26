package com.tictim.railreborn.api;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class CrowbarSimple implements Crowbar, ICapabilityProvider{
	private final ItemStack stack;
	
	public CrowbarSimple(ItemStack stack){
		this.stack = stack;
	}
	
	@Override
	public boolean canWork(){
		return true;
	}
	
	@Override
	public void applyDamage(EntityLivingBase target){
		stack.damageItem(1, target);
	}
	
	@Override
	public boolean hasCapability(Capability<?> cap, EnumFacing facing){
		return cap!=null&&cap==RailRebornAPI.CROWBAR;
	}
	
	@Override
	public <T> T getCapability(Capability<T> cap, EnumFacing facing){
		return cap!=null&&cap==RailRebornAPI.CROWBAR ? (T)this : null;
	}
}
