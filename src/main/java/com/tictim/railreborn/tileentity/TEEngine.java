package com.tictim.railreborn.tileentity;

import com.tictim.railreborn.api.RJ;
import com.tictim.railreborn.enums.Engines;
import com.tictim.railreborn.enums.Multibricks;
import com.tictim.railreborn.logic.Logic;
import com.tictim.railreborn.logic.LogicEngine;
import com.tictim.railreborn.util.NBTTypes;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
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

	public void fillTank(FluidStack fluidStack) {
		LogicEngine logicengine = (LogicEngine) logic;
		logicengine.fillTank(fluidStack);
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState){
		return oldState.getBlock()!=newState.getBlock();
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		if(engine!=null){
			nbt.setInteger("engine", engine.ordinal());
		}
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt){
		if(nbt.hasKey("engine", NBTTypes.INTEGER)){
			setEngine(Engines.fromMeta(nbt.getInteger("engine")));
		}
		super.readFromNBT(nbt);
	}

}
