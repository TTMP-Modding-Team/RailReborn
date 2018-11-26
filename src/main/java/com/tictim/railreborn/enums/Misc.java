package com.tictim.railreborn.enums;

import com.tictim.railreborn.item.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public enum Misc{
	COKE("fuelCoke"),
	COAL_DUST("dustCoal"),
	CHARCOAL_DUST("dustCharcoal"),
	COKE_DUST("dustCoke"),
	LAPIS_DUST("dustLapis"),
	QUARTZ_DUST("dustQuartz"),
	DIAMOND_DUST("dustDiamond"),
	CRUSHED_OBSIDIAN,
	GOLD_PLATED_GEAR,
	GEAR_BUSHING;
	
	private final String[] oreDict;
	
	Misc(String... oreDict){
		this.oreDict = oreDict;
	}
	
	public String[] getOreDict(){
		return oreDict;
	}
	
	public static Misc fromMeta(int meta){
		Misc[] arr = values();
		return arr[MathHelper.clamp(meta, 0, arr.length)];
	}
	
	@Override
	public String toString(){
		return name().toLowerCase();
	}
	
	private ItemStack of;
	
	public ItemStack of(){
		if(of==null) of = new ItemStack(ModItems.MISC, 1, this.ordinal());
		return of;
	}
	
	public ItemStack of(int count){
		if(count<=0||count>64) throw new IllegalArgumentException(Integer.toString(count));
		return count==1 ? of() : new ItemStack(ModItems.MISC, count, this.ordinal());
	}
}
