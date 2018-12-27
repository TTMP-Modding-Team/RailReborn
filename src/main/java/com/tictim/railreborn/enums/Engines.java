package com.tictim.railreborn.enums;

import com.tictim.railreborn.RailRebornGui;
import com.tictim.railreborn.logic.*;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.MathHelper;

public enum Engines implements IStringSerializable{
	REDSTONE_REPEATER(1000, 10, 10),
	HOBBYIST_STEAM(8000, 40, 40),
	STEAM(16000, 80, 80),
	DIESEL(24000, 120, 120);
	
	private final long capacityRJ, maxRJOut, genPerTick;
	
	Engines(long capacityRJ, long maxRJOut, long genPerTick){
		this.capacityRJ = capacityRJ;
		this.maxRJOut = maxRJOut;
		this.genPerTick = genPerTick;
	}
	
	public long capacityRJ(){
		return capacityRJ;
	}
	
	public long maxRJOut(){
		return maxRJOut;
	}
	
	public long genPerTick(){
		return genPerTick;
	}
	
	@Override
	public String getName(){
		return name().toLowerCase();
	}
	
	public Logic createLogic(){
		switch(this){
			case HOBBYIST_STEAM:
				return new LogicEngineHobbyistSteam();
			case STEAM:
				return new LogicEngineSteam();
			case DIESEL:
				return new LogicEngineDiesel();
			default: // case REDSTONE_REPEATER:
				return new LogicEngineRedstone();
		}
	}
	
	public RailRebornGui getGui(){
		switch(this){
			case HOBBYIST_STEAM:
				return RailRebornGui.ENGINE_HOBBYIST_STEAM;
			case STEAM:
				return RailRebornGui.ENGINE_STEAM;
			case DIESEL:
				return RailRebornGui.ENGINE_DIESEL;
			default: // case REDSTONE_REPEATER:
				return null;
		}
	}
	
	public static Engines fromMeta(int meta){
		Engines[] arr = values();
		return arr[MathHelper.clamp(meta, 0, arr.length)];
	}
}