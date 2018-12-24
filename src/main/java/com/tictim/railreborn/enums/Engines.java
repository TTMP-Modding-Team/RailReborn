package com.tictim.railreborn.enums;

import com.tictim.railreborn.RailRebornGui;
import com.tictim.railreborn.client.gui.GuiSteamEngine;
import com.tictim.railreborn.logic.*;
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
		switch(this) {
			case DIESEL:
				return new LogicDiselEngine();
			case STEAM:
				return new LogicSteamEngine();
			case HOBBYIST_STEAM:
				return new LogicHobbyistEngine();
			default:
				return new LogicRedstoneEngine();
		}
	}

	public RailRebornGui getGui(){
		switch(this) {
			case DIESEL:
				return RailRebornGui.DISEL_ENGINE;
			case STEAM:
				return RailRebornGui.STEAM_ENGINE;
			case HOBBYIST_STEAM:
				return RailRebornGui.HOBBYIST_ENGINE;
			default:
				return null;
		}
	}

	public static Engines fromMeta(int meta){
		Engines[] arr = values();
		return arr[MathHelper.clamp(meta, 0, arr.length)];
	}
}