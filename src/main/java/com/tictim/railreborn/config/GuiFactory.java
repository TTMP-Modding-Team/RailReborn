package com.tictim.railreborn.config;

import java.util.Set;

import com.tictim.railreborn.RailReborn;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.IModGuiFactory.RuntimeOptionCategoryElement;
import net.minecraftforge.fml.client.config.GuiConfig;

public class GuiFactory implements IModGuiFactory{
	@Override
	public void initialize(Minecraft minecraftInstance){}
	
	@Override
	public boolean hasConfigGui(){
		return true;
	}
	
	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen){
		return new GuiConfig(parentScreen, RailReborn.MODID, "RailReborn");
	}
	
	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories(){
		return null;
	}
}
