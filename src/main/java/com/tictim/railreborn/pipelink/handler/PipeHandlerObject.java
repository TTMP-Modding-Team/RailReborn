package com.tictim.railreborn.pipelink.handler;

import com.tictim.railreborn.pipelink.attachment.PipeAttachment;
import com.tictim.railreborn.pipelink.attachment.PipeAttachments;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class PipeHandlerObject extends PipeHandler{
	@Override
	@Nullable
	public PipeAttachment createDefaultPipeAttachment(World world, BlockPos target, EnumFacing facing){
		return null;
	}
	
	@Override
	public PipeAttachment deserializeDefaultPipeAttachment(NBTTagCompound nbt){
		return PipeAttachments.INVALID.deserializePipeAttachment(this, nbt);
	}
}
