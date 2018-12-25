package com.tictim.railreborn.util;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;

public final class ArmorMaterials{
	private ArmorMaterials(){}
	
	public static final ItemArmor.ArmorMaterial FOOLS_SUITE = EnumHelper.addArmorMaterial("FOOLS_SUITE", "fools_suite", 100, new int[]{1, 2, 2, 1}, 5, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0);
}
