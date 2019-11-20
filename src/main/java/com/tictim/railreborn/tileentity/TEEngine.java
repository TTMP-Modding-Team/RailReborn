package com.tictim.railreborn.tileentity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tictim.railreborn.api.RailRebornAPI;
import com.tictim.railreborn.block.BlockEngine;
import com.tictim.railreborn.enums.Engines;
import com.tictim.railreborn.logic.Logic;
import com.tictim.railreborn.logic.LogicEngine;
import com.tictim.railreborn.util.NBTTypes;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.animation.AnimationTESR;
import net.minecraftforge.common.animation.Event;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class TEEngine extends TELogic implements ITickable{
	@Nullable
	private Engines engine;

	public int progress = 0;
	public int generateTick = 0;

	private boolean isMoving = false;
	
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
	
	public Container getContainer(EntityPlayer player, Engines assure){
		if(engine!=assure) throw new IllegalStateException("Wrong Multibrick");
		return logic.getContainer(this, player);
	}
	
	@SideOnly(Side.CLIENT)
	public GuiContainer getGui(EntityPlayer player, Engines assure){
		if(engine!=assure) throw new IllegalStateException("Wrong Multibrick");
		return logic.getGui(this, player);
	}
	
	@Override
	public void update(){
		if(!this.world.isRemote&&logic!=null) {
			this.logic.update();
			LogicEngine elogic = (LogicEngine) this.logic;
			this.isMoving = elogic.isGenerating();
			if(this.isMoving) this.generateTick++;
			else if (!this.isMoving && this.generateTick>0) {
				this.generateTick--;
			}
			setProgress();
		}

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
	
	@Override
	protected boolean hasLogicCapability(Capability<?> cap, EnumFacing facing){
		return facing==null||facing==(cap==RailRebornAPI.RJ ? blockFacing() : blockFacing().getOpposite()) ? logic.hasCapability(cap, facing) : false;
	}
	
	@Override
	@Nullable
	protected <T> T getLogicCapability(Capability<T> cap, EnumFacing facing){
		return facing==null||facing==(cap==RailRebornAPI.RJ ? blockFacing() : blockFacing().getOpposite()) ? logic.getCapability(cap, facing) : null;
	}
	
	private EnumFacing blockFacing(){
		IBlockState state = this.world.getBlockState(pos);
		switch(state.getValue(BlockEngine.STATE)){
			case FLOOR:
				return EnumFacing.UP;
			case WALL:
				return state.getValue(BlockHorizontal.FACING);
			default: // case CEILING:
				return EnumFacing.DOWN;
		}
	}
	/*private boolean waspeak=false;
	public float getGenerateProgress() {
		LogicEngine l = (LogicEngine) this.logic;
		if(this.progress >= 5/16.0f) waspeak = true;
		if(this.progress <= 0) waspeak = false;
		if(!waspeak) this.progress = this.progress + 0.01f;
		if(waspeak) this.progress = this.progress - 0.01f;
		return this.progress;
	}*/

	public boolean isMoving() {
		return this.isMoving;
	}

	public int getProgress() {
		return this.progress;
	}

	public void setProgress() {
		int i = this.generateTick;
		if(i==0) this.progress = 0;
		else if(0< i && i < 1200) this.progress = 1;
		else if(1200 <= i && i < 1800) this.progress = 2;
		else if(1800<= i && i < 2400) this.progress = 3;
		else if(2400 < i) this.progress = 4;
		else {
			this.generateTick = 2400;
			this.progress = 0;
		}
	}

	@Override
	public boolean hasFastRenderer() {
		return true;
	}
}
