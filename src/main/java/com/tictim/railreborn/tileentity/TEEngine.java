package com.tictim.railreborn.tileentity;

import com.tictim.railreborn.api.RJ;
import com.tictim.railreborn.enums.Engines;
import com.tictim.railreborn.enums.Multibricks;
import com.tictim.railreborn.logic.Logic;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class TEEngine extends TELogic implements ITickable{
	@Nullable
	private Engines engine;
	
	public Engines getEngine(){
		return this.engine;
	}
	
	public TEEngine setEngine(Engines value){
		this.engine = value;
		this.resetLogic();
		return this;
	}
	public void addFluid() {
		//for debu
	}
	@Nullable
	@Override
	protected Logic createNewLogic(){
		return this.engine==null ? null : engine.createLogic();
	}

	public Container getContainer(EntityPlayer player){
		return logic.getContainer(this, player);
	}

	@SideOnly(Side.CLIENT)
	public GuiContainer getGui(EntityPlayer player){
		return logic.getGui(this, player);
	}
	
	@Override
	public void update(){
		if(!this.world.isRemote){
			if(logic!=null){
				if(!logic.isValid()) logic.validate(this, null);
				logic.update();
			}
		}
	}

}
