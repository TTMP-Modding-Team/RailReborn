package com.tictim.railreborn.tileentity;

import com.google.gson.JsonElement;
import com.tictim.railreborn.Registries;
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
	public PipeVisual visual;
	
	public PipeHandler getHandler(){
		PipeNode node = getNode();
		return node!=null ? node.getPipeHandler() : PipeHandlers.INVALID;
	}
	
	@Override
	public void validate(){
		super.validate();
	}
	
	public void onPlacement(PipeHandler handler){
		WorldPipeLink wpl = WorldPipeLink.get(this.world);
		PipeNode thisNode = wpl.create(pos, handler);
		
		for(EnumFacing f: EnumFacing.VALUES){
			thisNode.tryConnect(f);
			thisNode.tryAppendAttachment(PipeAttachments.DEFAULT, f);
		}
	}
	
	public void onBreak(){
		PipeNode node = getNode();
		if(node!=null) node.disconnectToAll(noDrop);
	}
	
	@Nullable
	public PipeNode getNode(){
		return WorldPipeLink.get(this.world).getNode(this.pos);
	}
	
	public boolean tryConnect(EnumFacing facing){
		PipeNode node = getNode();
		if(node==null) return false;
		
		if(node.getConnectedNode(facing)==null&&node.tryConnect(facing)){
			DataUtils.updateTileEntity(this);
			return true;
		}else return false;
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
	
	public boolean updateConnection(EnumFacing facing){
		PipeNode node = getNode();
		if(node==null) return false;
		
		boolean returns = false;
		
		if(node.getConnectedNode(facing)==null) returns = tryConnect(facing);
		if(node.checkAttachment(facing)){
			DataUtils.updateTileEntity(this);
			return true;
		}else return returns;
	}
	
	public boolean onAttachment(PipeAttachmentProvider p, EnumFacing facing){
		PipeNode node = getNode();
		if(node==null) return false;
		return node.getAttachment(facing)==null&&node.tryAppendAttachment(p, facing);
	}
	
	private boolean noDrop;
	
	public void setNoDrop(boolean noDrop){
		this.noDrop = noDrop;
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt){
		this.handleUpdateTag(pkt.getNbtCompound());
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound nbt){
		this.visual = new PipeVisual(nbt.getCompoundTag("visual"));
	}
	
	@Override
	public NBTTagCompound getUpdateTag(){
		NBTTagCompound nbt = super.getUpdateTag();
		PipeNode node = getNode();
		if(node!=null){
			nbt.setTag("visual", new PipeVisual(node).serializeNBT());
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
		return new DebugJsonBuilder(this.getClass()).key("").add(getNode()).getDebugInfo();
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
	@SuppressWarnings("unchecked")
	public <T> T getCapability(Capability<T> cap, EnumFacing facing){
		if(cap==Debugable.CAP) return (T)this;
		PipeNode node = getNode();
		T t = node!=null ? node.getCapability(cap, facing) : null;
		return t!=null ? t : super.getCapability(cap, facing);
	}
	
	public static class PipeVisual{
		private PipeHandler handler;
		private final PipeSideVisual[] sides = new PipeSideVisual[6];
		
		public PipeVisual(PipeNode node){
			this.handler = node.getPipeHandler();
			for(EnumFacing f: EnumFacing.VALUES){
				PipeAttachment a = node.getAttachment(f);
				if(a!=null) sides[f.getIndex()] = new PipeSideVisual(a, node.getConnectedNode(f)!=null);
				else if(node.getConnectedNode(f)!=null) sides[f.getIndex()] = new PipeSideVisual(true);
			}
		}
		
		public PipeVisual(PipeHandler handler){
			this.handler = handler;
			this.sides[EnumFacing.EAST.getIndex()] = new PipeSideVisual(false);
			this.sides[EnumFacing.WEST.getIndex()] = new PipeSideVisual(false);
		}
		
		public PipeHandler getHandler(){
			return handler;
		}
		
		public void setHandler(PipeHandler handler){
			this.handler = handler;
		}
		
		@Nullable
		public PipeSideVisual getVisual(EnumFacing facing){
			return sides[facing.getIndex()];
		}
		
		public PipeVisual(NBTTagCompound nbt){
			this.handler = PipeHandlers.fromId(nbt.getShort("handler"));
			NBTTagList list = nbt.getTagList("list", NBTTypes.COMPOUND);
			for(int i = 0; i<list.tagCount(); i++){
				NBTTagCompound subnbt = list.getCompoundTagAt(i);
				int idx = subnbt.getByte("idx");
				if(idx >= 0&&idx<6){
					sides[idx] = new PipeSideVisual(subnbt);
				}
			}
		}
		
		public NBTTagCompound serializeNBT(){
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setShort("handler", PipeHandlers.toId(handler));
			NBTTagList list = new NBTTagList();
			for(int i = 0; i<6; i++){
				if(sides[i]!=null){
					NBTTagCompound subnbt = sides[i].serializeNBT();
					subnbt.setByte("idx", (byte)i);
					list.appendTag(subnbt);
				}
			}
			if(!list.hasNoTags()) nbt.setTag("list", list);
			return nbt;
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
			attachment = Registries.PIPE_ATTACHMENTS.getValue(new ResourceLocation(nbt.getString("attachment")));
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
