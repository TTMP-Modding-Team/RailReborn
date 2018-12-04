package com.tictim.railreborn.enums;

import org.apache.commons.lang3.StringUtils;
import com.tictim.railreborn.config.RailRebornCfg;

public enum Shape{
	INGOT,
	NUGGET,
	DUST,
	PLATE,
	GEAR;
	
	@Override
	public String toString(){
		return name().toLowerCase();
	}
	
	public String oreName(){
		switch(this){
			case GEAR:
				return RailRebornCfg.General.changeGearOreDict ? "railRebornGear" : "gear";
			default:
				return this.toString();
		}
	}
	
	public String oreName(String ore){
		return oreName(oreName(), ore);
	}
	
	public static String oreName(String prefix, String ore){
		return prefix.isEmpty() ? prefix : prefix+StringUtils.capitalize(ore);
	}
}
