package com.tictim.railreborn.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.tictim.railreborn.api.CrowbarSimple;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.oredict.OreDictionary;

public class ItemCrowbar extends ItemBase{
	private final double dmg;
	private final String repairMaterial;
	
	public ItemCrowbar(double dmg, int durability, String repairMaterial){
		this.setMaxStackSize(1).setMaxDamage(durability);
		this.setTooltip("tooltip.crowbars");
		this.dmg = dmg;
		this.repairMaterial = repairMaterial;
	}
	
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt){
		return new CrowbarSimple(stack);
	}
	
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair){
		return OreDictionary.containsMatch(true, OreDictionary.getOres(repairMaterial), repair);
	}
	
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack){
		Multimap<String, AttributeModifier> multimap = HashMultimap.create();
		if(slot==EntityEquipmentSlot.MAINHAND){
			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", dmg, 0));
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4, 0));
		}
		return multimap;
	}
}
