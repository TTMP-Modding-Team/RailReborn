package com.tictim.railreborn.enums;

import net.minecraft.util.IStringSerializable;

public enum Metals implements IStringSerializable{
	STEEL,
	STAINLESS_STEEL,
	CHROME;
	
	@Override
	public String getName(){
		return name().toLowerCase();
	}
}
