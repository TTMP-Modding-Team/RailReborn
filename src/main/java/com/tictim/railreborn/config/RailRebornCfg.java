package com.tictim.railreborn.config;

import com.tictim.railreborn.RailReborn;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid = RailReborn.MODID)
@Config(modid = RailReborn.MODID, name = "ttmp/railreborn", category = "")
public final class RailRebornCfg{
	private RailRebornCfg(){}
	
	@SubscribeEvent
	public static void onConfigChange(OnConfigChangedEvent event){
		if(event.getModID().equals(RailReborn.MODID)) ConfigManager.sync(RailReborn.MODID, Type.INSTANCE);
	}
	
	public static final RailRebornCfgGeneral General = new RailRebornCfgGeneral();
	
	public static final class RailRebornCfgGeneral{
		private RailRebornCfgGeneral(){}
		
		@Config.Comment(value = "Changes OreDict name of stainless steel to railRebornSteel, to avoid some balance problems with other mods.")
		@Config.RequiresMcRestart
		public static boolean changeStainlessSteelOreDict = false;
		
		@Config.Comment(value = "Changes OreDict name of chrome to railRebornChrome, to avoid some balance problems with other mods.")
		@Config.RequiresMcRestart
		public static boolean changeChromeOreDict = false;
		
		@Config.Comment(value = "Changes OreDict name of gear to railRebornGear, to avoid some balance problems with other mods.")
		@Config.RequiresMcRestart
		public static boolean changeGearOreDict = false;
	}
	
	public static String getStainlessName(){
		return RailRebornCfg.General.changeStainlessSteelOreDict ? "railRebornSteel" : "stainlessSteel";
	}
	
	public static String getChromeName(){
		return RailRebornCfg.General.changeChromeOreDict ? "railRebornChrome" : "chrome";
	}
	
	public static String getChromiumName(){
		return RailRebornCfg.General.changeChromeOreDict ? "railRebornChromium" : "chromium";
	}
}
