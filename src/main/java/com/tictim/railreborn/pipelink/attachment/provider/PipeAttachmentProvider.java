package com.tictim.railreborn.pipelink.attachment.provider;

import com.tictim.railreborn.item.ModItems;
import com.tictim.railreborn.pipelink.attachment.PipeAttachment;
import com.tictim.railreborn.pipelink.handler.PipeHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;

public abstract class PipeAttachmentProvider extends IForgeRegistryEntry.Impl<PipeAttachmentProvider>{
	private boolean collectible = true;
	private boolean blocksConnection = true;
	
	@Nullable
	public abstract PipeAttachment createPipeAttachment(PipeHandler handler, World world, BlockPos target, EnumFacing facing);
	public abstract PipeAttachment deserializePipeAttachment(PipeHandler handler, NBTTagCompound nbt);
	
	public boolean isCollectible(){
		return collectible;
	}
	
	public PipeAttachmentProvider setCollectible(boolean value){
		this.collectible = value;
		return this;
	}
	
	public boolean blocksConnection(){
		return blocksConnection;
	}
	
	public PipeAttachmentProvider setBlocksConnection(boolean value){
		this.blocksConnection = value;
		return this;
	}
	
	private ItemStack of;
	
	public ItemStack stackOf(){
		if(!isCollectible()) return ItemStack.EMPTY;
		if(of==null){
			of = new ItemStack(ModItems.PIPE_ATTACHMENTS);
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("attachment", this.getRegistryName().toString());
			of.setTagCompound(nbt);
		}
		return of;
	}
}
