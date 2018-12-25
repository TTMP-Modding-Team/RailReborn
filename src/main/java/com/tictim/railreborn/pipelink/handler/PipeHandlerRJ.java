package com.tictim.railreborn.pipelink.handler;

import com.tictim.railreborn.api.RailRebornAPI;
import com.tictim.railreborn.pipelink.attachment.PipeAttachment;
import com.tictim.railreborn.pipelink.attachment.PipeAttachments;
import com.tictim.railreborn.pipelink.attachment.provider.PipeAttachmentProvider;
import com.tictim.railreborn.util.DataUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PipeHandlerRJ extends PipeHandler{
	private long capacity = 1;
	private long extract;
	private long insert;
	private long loss;
	
	public PipeHandlerRJ setExtract(long extract){
		this.extract = Math.max(0, extract);
		return this;
	}
	
	public PipeHandlerRJ setInsert(long insert){
		this.insert = Math.max(0, insert);
		return this;
	}
	
	public PipeHandlerRJ setCapacity(long capacity){
		this.capacity = Math.max(0, capacity);
		return this;
	}
	
	public PipeHandlerRJ setLossPerTick(long loss){
		this.loss = Math.max(0, loss);
		return this;
	}
	
	@Override
	@Nullable
	public PipeAttachment createDefaultPipeAttachment(World world, BlockPos target, EnumFacing facing){
		TileEntity te = world.getTileEntity(target);
		if(te!=null&&te.hasCapability(RailRebornAPI.RJ, facing)) return new PipeAttachmentRJ(target, facing);
		else return null;
	}
	
	@Override
	public PipeAttachment deserializeDefaultPipeAttachment(NBTTagCompound nbt){
		return new PipeAttachmentRJ(nbt);
	}
	
	public class PipeAttachmentRJ implements PipeAttachment{
		private final BlockPos pos;
		private final EnumFacing facing;
		
		public PipeAttachmentRJ(BlockPos pos, EnumFacing facing){
			this.pos = pos;
			this.facing = facing;
		}
		
		public PipeAttachmentRJ(NBTTagCompound nbt){
			this.pos = DataUtils.toBlockPos(nbt);
			this.facing = EnumFacing.getFront(nbt.getByte("facing"));
		}
		
		@Override
		public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing){
			return false;
		}
		
		@Nullable
		@Override
		public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing){
			return null;
		}
		
		@Override
		public NBTTagCompound serializeNBT(){
			NBTTagCompound nbt = DataUtils.toNBT(pos);
			nbt.setByte("facing", (byte)facing.getIndex());
			return nbt;
		}
		
		@Override
		public PipeAttachmentProvider getProvider(){
			return PipeAttachments.DEFAULT;
		}
	}
}
