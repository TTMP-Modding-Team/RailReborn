package com.tictim.railreborn.pipelink.attachment;

import com.google.gson.JsonElement;
import com.tictim.railreborn.capability.Debugable;
import com.tictim.railreborn.pipelink.PipeNode;
import com.tictim.railreborn.pipelink.attachment.provider.PipeAttachmentProvider;
import com.tictim.railreborn.util.DebugJsonBuilder;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface PipeAttachment extends ICapabilityProvider, Debugable{
	NBTTagCompound serializeNBT();
	PipeAttachmentProvider getProvider();
	
	default void update(PipeNode node, EnumFacing facing){}
	default boolean isDismantled(PipeNode node, EnumFacing facing){
		return this.getProvider().getConnectionRequirement().isStateValid(node.isConnected(facing));
	}
	
	/**
	 * For rendering
	 */
	default int getMetadata(){ return 0; }
	
	@Override
	default boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing){
		return false;
	}
	
	@Nullable
	@Override
	default <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing){
		return null;
	}
	
	@Override
	default JsonElement getDebugInfo(){
		return new DebugJsonBuilder(this.getClass()).getDebugInfo();
	}
}
