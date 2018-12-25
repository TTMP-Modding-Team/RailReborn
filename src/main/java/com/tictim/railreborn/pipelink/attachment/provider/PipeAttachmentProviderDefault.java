package com.tictim.railreborn.pipelink.attachment.provider;

import com.tictim.railreborn.pipelink.attachment.PipeAttachment;
import com.tictim.railreborn.pipelink.handler.PipeHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class PipeAttachmentProviderDefault extends PipeAttachmentProvider{
	@Nullable
	@Override
	public PipeAttachment createPipeAttachment(PipeHandler handler, World world, BlockPos target, EnumFacing facing){
		return handler.createDefaultPipeAttachment(world, target, facing);
	}
	
	@Override
	public PipeAttachment deserializePipeAttachment(PipeHandler handler, NBTTagCompound nbt){
		return handler.deserializeDefaultPipeAttachment(nbt);
	}
}
