package com.tictim.railreborn.util;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public final class DataUtils{
	private DataUtils(){}
	
	public static NBTTagCompound toNBT(BlockPos pos){
		NBTTagCompound nbt = new NBTTagCompound();
		if(BlockPos.fromLong(pos.toLong()).equals(pos)) nbt.setLong("xyz", pos.toLong());
		else{
			nbt.setInteger("x", pos.getX());
			nbt.setInteger("y", pos.getY());
			nbt.setInteger("z", pos.getZ());
		}
		return nbt;
	}
	
	public static BlockPos toBlockPos(NBTTagCompound nbt){
		return nbt.hasKey("xyz", NBTTypes.LONG) ? BlockPos.fromLong(nbt.getLong("xyz")) : new BlockPos(nbt.getInteger("x"), nbt.getInteger("y"), nbt.getInteger("z"));
	}
	
	public static void writeBlockPos(ByteBuf buf, BlockPos pos){
		buf.writeInt(pos.getX()).writeByte(pos.getY()).writeInt(pos.getZ());
	}
	
	public static BlockPos readBlockPos(ByteBuf buf){
		return new BlockPos(buf.readInt(), buf.readUnsignedByte(), buf.readInt());
	}
	
	public static void updateTileEntity(TileEntity te){
		IBlockState state = te.getWorld().getBlockState(te.getPos());
		te.getWorld().notifyBlockUpdate(te.getPos(), state, state, 0);
	}
}
