package com.tictim.railreborn.tileentity;

import com.google.gson.JsonElement;
import com.tictim.railreborn.RailReborn;
import com.tictim.railreborn.capability.Debugable;
import com.tictim.railreborn.pipelink.PipeNode;
import com.tictim.railreborn.pipelink.WorldPipeLink;
import com.tictim.railreborn.pipelink.attachment.PipeAttachment;
import com.tictim.railreborn.pipelink.attachment.PipeAttachments;
import com.tictim.railreborn.pipelink.attachment.provider.PipeAttachmentProvider;
import com.tictim.railreborn.pipelink.handler.PipeHandler;
import com.tictim.railreborn.pipelink.handler.PipeHandlers;
import com.tictim.railreborn.util.DataUtils;
import com.tictim.railreborn.util.DebugJsonBuilder;
import com.tictim.railreborn.util.NBTTypes;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class TEPipe extends TileEntity implements Debugable{
	private PipeHandler handler = PipeHandlers.INVALID;
	public PipeVisual visual;
	
	public TEPipe setHandler(PipeHandler handler){
		this.handler = handler;
		return this;
	}
	
	public PipeHandler getHandler(){
		return this.handler;
	}
	
	@Override
	public void validate(){
		super.validate();
	}
	
	public void onPlacement(){
		PipeNode thisNode = null;
		for(EnumFacing f: EnumFacing.VALUES){
			PipeNode node2 = tryGetNode(f);
			if(node2!=null){
				if(thisNode==null){
					thisNode = node2.create(f.getOpposite(), this.pos);
				}else{
					thisNode.tryConnect(node2, f);
				}
			}
		}
		if(thisNode==null){
			thisNode = WorldPipeLink.get(this.world).assign(this.handler, this.pos);
		}
		for(EnumFacing f: EnumFacing.VALUES){
			thisNode.tryAppendAttachment(PipeAttachments.DEFAULT, f);
		}
		// TODO connect to machines etc.
	}
	
	public void onBreak(){
		PipeNode node = getNode();
		if(node!=null) node.disconnectToAll(this.world);
	}
	
	@Nullable
	public PipeNode getNode(){
		return WorldPipeLink.get(this.world).getNode(this.handler, this.pos);
	}
	
	@Nullable
	public PipeNode tryGetNode(EnumFacing facing){
		TileEntity te = this.world.getTileEntity(this.pos.offset(facing));
		if(te instanceof TEPipe){
			TEPipe pipe = (TEPipe)te;
			return pipe.handler==this.handler ? pipe.getNode() : null;
		}else return null;
	}
	
	public boolean tryConnect(EnumFacing facing){
		PipeNode node = getNode();
		if(node==null) return false;
		
		PipeNode anotherNode = node.getConnectedNode(facing);
		if(anotherNode==null){
			anotherNode = tryGetNode(facing);
			if(anotherNode!=null){
				node.tryConnect(anotherNode, facing);
				DataUtils.updateTileEntity(this);
				return true;
			}
		}
		return false;
	}
	
	public boolean tryDisconnect(EnumFacing facing){
		PipeNode node = getNode();
		if(node==null) return false;
		
		PipeNode anotherNode = node.getConnectedNode(facing);
		if(anotherNode!=null){
			node.disconnect(facing);
			anotherNode.disconnect(facing.getOpposite());
			DataUtils.updateTileEntity(this);
			anotherNode.updateNode();
			return true;
		}
		return false;
	}
	
	public boolean onAttachment(PipeAttachmentProvider p, EnumFacing facing){
		PipeNode node = getNode();
		if(node==null) return false;
		return node.getAttachment(facing)==null&&node.tryAppendAttachment(p, facing);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt){
		this.handleUpdateTag(pkt.getNbtCompound());
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound nbt){
		this.handler = PipeHandlers.REGISTRY.getValue(new ResourceLocation(nbt.getString("pipe")));
		if(this.handler==PipeHandlers.INVALID) RailReborn.LOGGER.error("TEPipe synced with invalid PipeHandler");
		if(nbt.hasKey("visual", NBTTypes.LIST)){
			this.visual = new PipeVisual(nbt.getTagList("visual", NBTTypes.COMPOUND));
		}
	}
	
	@Override
	public NBTTagCompound getUpdateTag(){
		NBTTagCompound nbt = super.getUpdateTag();
		nbt.setString("pipe", handler.getRegistryName().toString());
		PipeNode node = getNode();
		if(node!=null){
			NBTTagList list = new PipeVisual(node).serializeNBT();
			if(!list.hasNoTags()) nbt.setTag("visual", list);
		}
		return nbt;
	}
	
	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket(){
		return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox(){
		return Block.FULL_BLOCK_AABB.offset(this.pos);
	}
	
	@Override
	public JsonElement getDebugInfo(){
		DebugJsonBuilder b = new DebugJsonBuilder(this.getClass());
		b.key("Pipe Handler").add(handler.getRegistryName());
		b.key("").add(getNode());
		return b.getDebugInfo();
	}
	
	@Override
	public String toString(){
		return Debugable.toDebugString(getDebugInfo());
	}
	
	@Override
	public boolean hasCapability(Capability<?> cap, EnumFacing facing){
		if(cap==Debugable.CAP) return true;
		PipeNode node = getNode();
		return node!=null&&node.hasCapability(cap, facing)||super.hasCapability(cap, facing);
	}
	
	@Override
	@Nullable
	public <T> T getCapability(Capability<T> cap, EnumFacing facing){
		if(cap==Debugable.CAP) return (T)this;
		PipeNode node = getNode();
		T t = node!=null ? node.getCapability(cap, facing) : null;
		return t!=null ? t : super.getCapability(cap, facing);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		nbt.setString("pipe", handler.getRegistryName().toString());
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		this.handler = PipeHandlers.REGISTRY.getValue(new ResourceLocation(nbt.getString("pipe")));
		if(this.handler==PipeHandlers.INVALID) RailReborn.LOGGER.error("TEPipe loaded with invalid PipeHandler");
	}
	
	public static class PipeVisual{
		private final PipeSideVisual[] sides = new PipeSideVisual[6];
		
		public PipeVisual(PipeNode node){
			for(EnumFacing f: EnumFacing.VALUES){
				PipeAttachment a = node.getAttachment(f);
				if(a!=null) sides[f.getIndex()] = new PipeSideVisual(a, node.getConnectedNode(f)!=null);
				else if(node.getConnectedNode(f)!=null) sides[f.getIndex()] = new PipeSideVisual(true);
			}
		}
		
		public PipeVisual(){
			sides[EnumFacing.EAST.getIndex()] = new PipeSideVisual(false);
			sides[EnumFacing.WEST.getIndex()] = new PipeSideVisual(false);
		}
		
		@Nullable
		public PipeSideVisual getVisual(EnumFacing facing){
			return sides[facing.getIndex()];
		}
		
		public PipeVisual(NBTTagList list){
			for(int i = 0; i<list.tagCount(); i++){
				NBTTagCompound nbt = list.getCompoundTagAt(i);
				int idx = nbt.getByte("idx");
				if(idx >= 0&&idx<6){
					sides[idx] = new PipeSideVisual(nbt);
				}
			}
		}
		
		public NBTTagList serializeNBT(){
			NBTTagList list = new NBTTagList();
			for(int i = 0; i<6; i++){
				if(sides[i]!=null){
					NBTTagCompound nbt = sides[i].serializeNBT();
					nbt.setByte("idx", (byte)i);
					list.appendTag(nbt);
				}
			}
			return list;
		}
	}
	
	public static class PipeSideVisual{
		public final PipeAttachmentProvider attachment;
		public final boolean connectedToNode;
		public int meta;
		
		public PipeSideVisual(PipeAttachment attachment, boolean connectedToNode){
			this.attachment = attachment.getProvider();
			this.meta = attachment.getMetadata();
			this.connectedToNode = connectedToNode;
		}
		
		public PipeSideVisual(boolean connectedToNode){
			this.attachment = PipeAttachments.DEFAULT;
			this.connectedToNode = connectedToNode;
		}
		
		public PipeSideVisual(NBTTagCompound nbt){
			attachment = PipeAttachments.REGISTRY.getValue(new ResourceLocation(nbt.getString("attachment")));
			connectedToNode = nbt.getBoolean("connected");
			meta = nbt.getInteger("meta");
		}
		
		public NBTTagCompound serializeNBT(){
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("attachment", attachment.getRegistryName().toString());
			if(connectedToNode) nbt.setBoolean("connected", true);
			nbt.setInteger("meta", meta);
			return nbt;
		}
	}
}
