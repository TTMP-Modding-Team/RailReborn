package com.tictim.railreborn.tileentity;

import javax.annotation.Nullable;
import com.tictim.railreborn.block.BlockMultibrickCore;
import com.tictim.railreborn.util.NBTTypes;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TEMultibrickPart extends TileEntity{
	private @Nullable BlockPos core;
	
	public void setCorePos(@Nullable BlockPos pos){
		this.core = pos;
	}
	
	public @Nullable BlockPos getCorePos(){
		return this.core;
	}
	
	public @Nullable TEMultibrick getCore(){
		if(this.core==null) return null;
		TileEntity te = world.getTileEntity(core);
		return te instanceof TEMultibrick ? (TEMultibrick)te : null;
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState){
		return oldState.getBlock()!=newState.getBlock()||oldState.getValue(BlockMultibrickCore.PROPERTY)!=newState.getValue(BlockMultibrickCore.PROPERTY);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		if(core!=null){
			NBTTagCompound subnbt = new NBTTagCompound();
			subnbt.setInteger("x", core.getX());
			subnbt.setInteger("y", core.getY());
			subnbt.setInteger("z", core.getZ());
			nbt.setTag("core", subnbt);
		}
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		if(nbt.hasKey("core", NBTTypes.COMPOUND)){
			NBTTagCompound subnbt = nbt.getCompoundTag("core");
			this.core = new BlockPos(subnbt.getInteger("x"), subnbt.getInteger("y"), subnbt.getInteger("z"));
		}else this.core = null;
	}
}
