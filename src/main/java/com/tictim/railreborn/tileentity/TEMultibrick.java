package com.tictim.railreborn.tileentity;

import com.google.gson.JsonObject;
import com.tictim.railreborn.client.tesr.TESRMultiblockDebug.MultiblockDebugable;
import com.tictim.railreborn.enums.Multibricks;
import com.tictim.railreborn.logic.Logic;
import com.tictim.railreborn.multiblock.Blueprint.TestResult;
import com.tictim.railreborn.util.NBTTypes;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class TEMultibrick extends TELogic implements ITickable, MultiblockDebugable{
	@Nullable
	private Multibricks multibrick;
	private int tick;
	
	public Multibricks getMultibrick(){
		return this.multibrick;
	}
	
	@Nullable
	@Override
	protected Logic createNewLogic(){
		return multibrick==null ? null : multibrick.createLogic();
	}
	
	public TEMultibrick setMultibrick(Multibricks value){
		this.multibrick = value;
		this.resetLogic();
		return this;
	}
	
	public Container getContainer(EntityPlayer player, Multibricks assure){
		if(multibrick!=assure) throw new IllegalStateException("Wrong Multibrick");
		return logic.getContainer(this, player);
	}
	
	@SideOnly(Side.CLIENT)
	public GuiContainer getGui(EntityPlayer player, Multibricks assure){
		if(multibrick!=assure) throw new IllegalStateException("Wrong Multibrick");
		return logic.getGui(this, player);
	}
	
	@Override
	public void update(){
		if(!this.world.isRemote&&logic!=null){
			if((++tick) >= 10){
				tick = 0;
				TestResult r = multibrick.getBlueprint().test(world, pos);
				if(logic.isValid()!=r.isValid()){
					if(r.isValid()) logic.validate(this, r);
					else logic.invalidate(this, r);
				}
			}
			if(logic.isValid()) logic.update();
		}
	}
	
	@Override
	@Nullable
	public TestResult getMultiblockTest(){
		return multibrick!=null ? multibrick.getBlueprint().test(world, pos) : null;
	}
	
	@Override
	protected void addDebugInfo(JsonObject obj){
		obj.addProperty("Multibrick Type", multibrick.getName());
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState){
		return oldState!=newState;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		if(multibrick!=null){
			nbt.setInteger("multibrick", multibrick.ordinal());
		}
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt){
		if(nbt.hasKey("multibrick", NBTTypes.INTEGER)){
			setMultibrick(Multibricks.fromMeta(nbt.getInteger("multibrick")));
		}
		super.readFromNBT(nbt);
	}
}
