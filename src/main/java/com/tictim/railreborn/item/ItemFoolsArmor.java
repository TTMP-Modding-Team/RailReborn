package com.tictim.railreborn.item;

import com.tictim.railreborn.util.ArmorMaterials;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class ItemFoolsArmor extends ItemArmor{
	public ItemFoolsArmor(EntityEquipmentSlot slot){
		super(ArmorMaterials.FOOLS_SUITE, 0, slot);
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack){
		return EnumRarity.EPIC;
	}
	
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair){
		return OreDictionary.containsMatch(false, OreDictionary.getOres("ingotSteel"), repair);
	}
	
	@Override
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced){
		tooltip.add(I18n.format("tooltip.foolscrowbar"));
	}
}
