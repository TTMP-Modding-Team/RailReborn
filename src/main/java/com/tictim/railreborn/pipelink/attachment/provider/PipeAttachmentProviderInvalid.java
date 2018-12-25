package com.tictim.railreborn.pipelink.attachment.provider;

import com.google.gson.JsonElement;
import com.tictim.railreborn.pipelink.attachment.PipeAttachment;
import com.tictim.railreborn.pipelink.handler.PipeHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class PipeAttachmentProviderInvalid extends PipeAttachmentProvider implements PipeAttachment{
	@Nullable
	@Override
	public PipeAttachment createPipeAttachment(PipeHandler handler, World world, BlockPos target, EnumFacing facing){
		return this;
	}
	
	@Override
	public PipeAttachment deserializePipeAttachment(PipeHandler handler, NBTTagCompound nbt){
		return this;
	}
	
	@Override
	public PipeAttachmentProvider getProvider(){
		return this;
	}
	
	@Override
	public NBTTagCompound serializeNBT(){
		return new NBTTagCompound();
	}
}
