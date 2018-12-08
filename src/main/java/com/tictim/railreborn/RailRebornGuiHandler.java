package com.tictim.railreborn;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RailRebornGuiHandler implements IGuiHandler{
	@Override
	public Container getServerGuiElement(int i, EntityPlayer player, World world, int x, int y, int z){
		return get(i).getServerGuiElement(player, world, x, y, z);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public GuiContainer getClientGuiElement(int i, EntityPlayer player, World world, int x, int y, int z){
		return get(i).getClientGuiElement(player, world, x, y, z);
	}
	
	private static RailRebornGui get(int idx){
		return RailRebornGui.values()[idx];
	}
}
