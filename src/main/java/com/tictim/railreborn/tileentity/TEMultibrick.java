package com.tictim.railreborn.tileentity;

import javax.annotation.Nullable;
import com.tictim.railreborn.block.BlockMultibrickCore;
import com.tictim.railreborn.enums.Multibricks;
import com.tictim.railreborn.logic.Logic;
import com.tictim.railreborn.multiblock.Blueprint.TestResult;
import com.tictim.railreborn.util.NBTTypes;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

public class TEMultibrick extends TileEntity implements ITickable{
	private @Nullable Logic<TileEntity> logic;
	private @Nullable Multibricks multibrick;
	
	private int tick;
	private boolean valid;
	
	public TileEntity setMultibrick(Multibricks value){
		this.multibrick = value;
		if(logic!=null) this.logic.invalidate(this, null);
		this.logic = value.createLogic();
		return this;
	}
	
	public void onBreak(){
		if(logic!=null) this.logic.invalidate(this, null);
	}
	
	public boolean isLogicValid(){
		return logic!=null&&valid;
	}
	
	@Override
	public void update(){
		if(!this.world.isRemote&&logic!=null){
			if((++tick)>=10){
				tick = 0;
				TestResult r = multibrick.getBlueprint().test(world, pos, world.getBlockState(pos).getValue(BlockHorizontal.FACING));
				if(valid!=r.isValid()){
					if(valid = r.isValid()){
						logic.validate(this, r);
						logic.invalidate(this, r);
					}
				}
			}
			if(valid) logic.update();
		}
	}
	
	@Override
	public boolean hasCapability(Capability<?> cap, EnumFacing facing){
		return logic!=null&&logic.hasCapability(cap, facing)||super.hasCapability(cap, facing);
	}
	
	@Override
	public @Nullable <T> T getCapability(Capability<T> cap, EnumFacing facing){
		T t = logic!=null ? logic.getCapability(cap, facing) : null;
		return t!=null ? t : super.getCapability(cap, facing);
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState){
		return oldState.getBlock()!=newState.getBlock()||oldState.getValue(BlockMultibrickCore.PROPERTY)!=newState.getValue(BlockMultibrickCore.PROPERTY);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		if(multibrick!=null){
			nbt.setInteger("multibrick", multibrick.ordinal());
			NBTTagCompound subnbt = logic.serializeNBT();
			if(!subnbt.hasNoTags()) nbt.setTag("logic", subnbt);
		}
		if(!valid) nbt.setBoolean("invalid", true);
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		if(nbt.hasKey("multibrick", NBTTypes.INTEGER)){
			setMultibrick(Multibricks.fromMeta(nbt.getInteger("multibrick")));
			logic.deserializeNBT(nbt.getCompoundTag("logic"));
		}
		this.valid = !nbt.getBoolean("invalid");
	}
}
