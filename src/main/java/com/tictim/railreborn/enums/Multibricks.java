package com.tictim.railreborn.enums;

import com.tictim.railreborn.logic.Logic;
import com.tictim.railreborn.logic.LogicBlastFurnace;
import com.tictim.railreborn.logic.LogicCokeOven;
import com.tictim.railreborn.multiblock.Blueprint;
import com.tictim.railreborn.multiblock.Blueprints;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.MathHelper;

public enum Multibricks implements IStringSerializable{
	COKE_OVEN,
	BLAST_FURNACE;
	
	@Override
	public String getName(){
		return name().toLowerCase();
	}
	
	public Logic createLogic(){
		switch(this){
			case BLAST_FURNACE:
				return new LogicBlastFurnace();
			default: // case COKE_OVEN:
				return new LogicCokeOven();
		}
	}
	
	public Blueprint getBlueprint(){
		switch(this){
			case BLAST_FURNACE:
				return Blueprints.BLAST_FURNACE;
			default: // case COKE_OVEN:
				return Blueprints.COKE_OVEN;
		}
	}
	
	public static Multibricks fromMeta(int meta){
		Multibricks[] arr = values();
		return arr[MathHelper.clamp(meta, 0, arr.length)];
	}
}
