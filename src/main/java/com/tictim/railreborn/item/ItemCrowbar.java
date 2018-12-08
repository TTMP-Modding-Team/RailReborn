package com.tictim.railreborn.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.tictim.railreborn.api.CrowbarSimple;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.oredict.OreDictionary;

public class ItemCrowbar extends ItemBase{
	private final double dmg;
	private final String repairMaterial;
	private final EnumRarity rarity;
	private final EnumRarity rarityEnchanted;
	
	public ItemCrowbar(double dmg, int durability, String repairMaterial, EnumRarity rarity, EnumRarity rarityEnchanted){
		this.setMaxStackSize(1).setMaxDamage(durability);
		this.setTooltip("tooltip.crowbars");
		this.dmg = dmg;
		this.repairMaterial = repairMaterial;
		this.rarity = rarity;
		this.rarityEnchanted = rarityEnchanted;
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack){
		return stack.isItemEnchanted() ? rarityEnchanted : rarity;
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker){
		stack.damageItem(1, attacker);
		return true;
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase entity){
		if(!world.isRemote&&state.getBlockHardness(world, pos)!=0) stack.damageItem(2, entity);
		return true;
	}
	
	@Override
	public boolean canDestroyBlockInCreative(World world, BlockPos pos, ItemStack stack, EntityPlayer player){
		return false;
	}
	
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt){
		return new CrowbarSimple(stack);
	}
	
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair){
		return repairMaterial!=null&&OreDictionary.containsMatch(true, OreDictionary.getOres(repairMaterial), repair);
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
