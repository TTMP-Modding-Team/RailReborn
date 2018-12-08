package com.tictim.railreborn.item;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemFoolsCrowbar extends ItemCrowbar{
	public ItemFoolsCrowbar(){
		super(99999999, 0, null, EnumRarity.EPIC, EnumRarity.EPIC);
		this.setTooltip("tooltip.foolscrowbar");
	}
	
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack){
		return true;
	}
}
