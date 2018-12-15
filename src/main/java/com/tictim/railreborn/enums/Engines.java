package com.tictim.railreborn.enums;

import com.tictim.railreborn.RailRebornGui;
import com.tictim.railreborn.logic.Logic;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.MathHelper;

public enum Engines implements IStringSerializable{
	REDSTONE_REPEATER,
	HOBBYIST_STEAM,
	STEAM,
	DIESEL;
	
	@Override
	public String getName(){
		return name().toLowerCase();
	}
	
	public Logic createLogic(){
		return null;
	}
	
	public RailRebornGui getGui(){
		return null;
	}
	
	public static Engines fromMeta(int meta){
		Engines[] arr = values();
		return arr[MathHelper.clamp(meta, 0, arr.length)];
	}
}