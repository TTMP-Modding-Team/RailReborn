package com.tictim.railreborn.api;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public final class RailRebornAPI{
	private RailRebornAPI(){}
	
	@CapabilityInject(RJ.class)
	public static final Capability<RJ> RJ = null;
	
	@CapabilityInject(Crowbar.class)
	public static final Capability<Crowbar> CROWBAR = null;
	
	public static boolean isCrowbar(ItemStack stack){
		return CROWBAR!=null&&stack.hasCapability(CROWBAR, null);
	}
}
