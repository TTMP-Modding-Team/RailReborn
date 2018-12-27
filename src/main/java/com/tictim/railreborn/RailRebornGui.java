package com.tictim.railreborn;

import com.tictim.railreborn.enums.Engines;
import com.tictim.railreborn.enums.Multibricks;
import com.tictim.railreborn.tileentity.TEEngine;
import com.tictim.railreborn.tileentity.TEMultibrick;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public enum RailRebornGui{
	COKE_OVEN{
		@Override
		public Container getServerGuiElement(EntityPlayer player, World world, int x, int y, int z){
			return ((TEMultibrick)world.getTileEntity(new BlockPos(x, y, z))).getContainer(player, Multibricks.COKE_OVEN);
		}
		
		@Override
		public GuiContainer getClientGuiElement(EntityPlayer player, World world, int x, int y, int z){
			return ((TEMultibrick)world.getTileEntity(new BlockPos(x, y, z))).getGui(player, Multibricks.COKE_OVEN);
		}
	},
	BLAST_FURNACE{
		@Override
		public Container getServerGuiElement(EntityPlayer player, World world, int x, int y, int z){
			return ((TEMultibrick)world.getTileEntity(new BlockPos(x, y, z))).getContainer(player, Multibricks.BLAST_FURNACE);
		}
		
		@Override
		public GuiContainer getClientGuiElement(EntityPlayer player, World world, int x, int y, int z){
			return ((TEMultibrick)world.getTileEntity(new BlockPos(x, y, z))).getGui(player, Multibricks.BLAST_FURNACE);
		}
		
	},
	ENGINE_HOBBYIST_STEAM{
		@Override
		public Container getServerGuiElement(EntityPlayer player, World world, int x, int y, int z){
			return ((TEEngine)world.getTileEntity(new BlockPos(x, y, z))).getContainer(player, Engines.HOBBYIST_STEAM);
		}
		
		@Override
		public GuiContainer getClientGuiElement(EntityPlayer player, World world, int x, int y, int z){
			return ((TEEngine)world.getTileEntity(new BlockPos(x, y, z))).getGui(player, Engines.HOBBYIST_STEAM);
		}
	},
	ENGINE_STEAM{
		@Override
		public Container getServerGuiElement(EntityPlayer player, World world, int x, int y, int z){
			return ((TEEngine)world.getTileEntity(new BlockPos(x, y, z))).getContainer(player, Engines.STEAM);
		}
		
		@Override
		public GuiContainer getClientGuiElement(EntityPlayer player, World world, int x, int y, int z){
			return ((TEEngine)world.getTileEntity(new BlockPos(x, y, z))).getGui(player, Engines.STEAM);
		}
	},
	ENGINE_DIESEL{
		@Override
		public Container getServerGuiElement(EntityPlayer player, World world, int x, int y, int z){
			return ((TEEngine)world.getTileEntity(new BlockPos(x, y, z))).getContainer(player, Engines.DIESEL);
		}
		
		@Override
		public GuiContainer getClientGuiElement(EntityPlayer player, World world, int x, int y, int z){
			return ((TEEngine)world.getTileEntity(new BlockPos(x, y, z))).getGui(player, Engines.DIESEL);
		}
	};
	
	public abstract Container getServerGuiElement(EntityPlayer player, World world, int x, int y, int z);
	
	@SideOnly(Side.CLIENT)
	public abstract GuiContainer getClientGuiElement(EntityPlayer player, World world, int x, int y, int z);
	
	public void openGui(EntityPlayer player, World world, BlockPos pos){
		openGui(player, world, pos.getX(), pos.getY(), pos.getZ());
	}
	
	public void openGui(EntityPlayer player, World world, int x, int y, int z){
		player.openGui(RailReborn.instance, ordinal(), world, x, y, z);
	}
}
