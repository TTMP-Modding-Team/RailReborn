package com.tictim.railreborn.enums;

import net.minecraft.util.IStringSerializable;

public enum WoodType implements IStringSerializable{
	HORIZONTAL,
	VERTICAL;
	
	@Override
	public String getName(){
		return name().toLowerCase();
	}
}
