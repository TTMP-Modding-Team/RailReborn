package com.tictim.railreborn.pipelink;

import com.google.gson.JsonElement;
import com.tictim.railreborn.RailReborn;
import com.tictim.railreborn.capability.Debugable;
import com.tictim.railreborn.multiblock.Blueprint;
import com.tictim.railreborn.pipelink.attachment.PipeAttachment;
import com.tictim.railreborn.pipelink.attachment.PipeAttachments;
import com.tictim.railreborn.pipelink.attachment.provider.PipeAttachmentProvider;
import com.tictim.railreborn.tileentity.TEPipe;
import com.tictim.railreborn.util.DataUtils;
import com.tictim.railreborn.util.DebugJsonBuilder;
import com.tictim.railreborn.util.NBTTypes;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PipeNode implements ICapabilityProvider, Debugable{
	private final PipeLink link;
	private final BlockPos pos;
	private final PipeNode[] nodes = new PipeNode[6];
	private final PipeAttachment[] attachments = new PipeAttachment[6];
	
	public PipeNode(PipeLink link, BlockPos pos){
		this.link = link;
		this.pos = pos;
	}
	
	public PipeNode(PipeLink link, NBTTagCompound nbt){
		this.link = link;
		this.pos = DataUtils.toBlockPos(nbt);
	}
	
	@SuppressWarnings("ConstantConditions")
	public void restoreConnection(NBTTagCompound nbt){
		if(nbt.hasKey("nodes", NBTTypes.LIST)){
			NBTTagList list = nbt.getTagList("nodes", NBTTypes.COMPOUND);
			for(int i = 0; i<list.tagCount(); i++){
				NBTTagCompound subnbt = list.getCompoundTagAt(i);
				int idx = subnbt.getInteger("index");
				if(idx >= 0&&idx<6){
					nodes[idx] = link.getNode(DataUtils.toBlockPos(subnbt));
					if(subnbt.hasKey("attachment", NBTTypes.COMPOUND)){
						NBTTagCompound anbt = subnbt.getCompoundTag("attachment");
						PipeAttachmentProvider p = PipeAttachments.REGISTRY.getValue(new ResourceLocation(anbt.getString("provider")));
						attachments[idx] = p.deserializePipeAttachment(link.getPipeHandler(), anbt);
					}
				}else RailReborn.LOGGER.error("Invalid Index {} on PipeNode at {}!", idx, Blueprint.posToStr(pos));
			}
		}
	}
	
	public PipeLink getLink(){
		return this.link;
	}
	
	public BlockPos getPos(){
		return this.pos;
	}
	
	public PipeNode create(EnumFacing facing, BlockPos pos){
		PipeNode n = this.getConnectedNode(facing);
		if(n!=null) return n;
		PipeNode node = new PipeNode(link, pos);
		link.add(node);
		add(node, facing);
		node.add(this, facing.getOpposite());
		this.updateNode();
		return node;
	}
	
	public void add(PipeNode node, EnumFacing facing){
		nodes[facing.getIndex()] = node;
	}
	
	public void disconnect(EnumFacing facing){
		nodes[facing.getIndex()] = null;
	}
	
	public void removeAttachment(EnumFacing facing){
		attachments[facing.getIndex()] = null;
	}
	
	public void tryConnect(PipeNode node, EnumFacing facing){
		add(node, facing);
		node.add(this, facing.getOpposite());
		node.updateNode();
		if(!this.getLink().isSameLink(node.getLink())) this.getLink().merge(node.getLink());
		else this.getLink().markDirty();
	}
	
	public boolean tryAppendAttachment(PipeAttachmentProvider provider, EnumFacing facing){
		PipeAttachment a = PipeAttachments.DEFAULT.createPipeAttachment(this.link.getPipeHandler(), this.link.getWorld(), this.pos.offset(facing), facing);
		if(a!=null){
			this.attachments[facing.getIndex()] = a;
			if(provider.blocksConnection()) disconnect(facing);
			link.markDirty();
			return true;
		}else return false;
	}
	
	public void updateNode(){
		TileEntity te = this.link.getWorld().getTileEntity(this.pos);
		if(te instanceof TEPipe) DataUtils.updateTileEntity(te);
	}
	
	public void disconnectToAll(World w){
		for(int i = 0; i<6; i++){
			if(nodes[i]!=null){
				nodes[i].disconnect(EnumFacing.VALUES[i].getOpposite());
				nodes[i].updateNode();
			}
			if(attachments[i]!=null){
				ItemStack s = attachments[i].getProvider().stackOf();
				if(!s.isEmpty()) InventoryHelper.spawnItemStack(w, pos.getX(), pos.getY(), pos.getZ(), s.copy());
			}
		}
		link.remove(pos);
	}
	
	@Nullable
	public PipeNode getConnectedNode(EnumFacing facing){
		PipeNode n = nodes[facing.getIndex()];
		if(n==null) return null;
		PipeAttachment a =  getAttachment(facing);
		return a!=null&&a.getProvider().blocksConnection() ? null : n;
	}
	
	@Nullable
	public PipeAttachment getAttachment(EnumFacing facing){
		return attachments[facing.getIndex()];
	}
	
	public boolean isConnected(EnumFacing f){
		return getConnectedNode(f)!=null||getAttachment(f)!=null;
	}
	
	public boolean isLoaded(){
		return this.link.getWorld().isBlockLoaded(this.pos);
	}
	
	@Override
	public int hashCode(){
		return pos.hashCode();
	}
	
	@Override
	public JsonElement getDebugInfo(){
		DebugJsonBuilder b = new DebugJsonBuilder(this.getClass()).add(pos);
		b.key("").moveFloor();
		for(EnumFacing facing: EnumFacing.VALUES){
			PipeNode n = getConnectedNode(facing);
			PipeAttachment a = getAttachment(facing);
			if(n!=null||a!=null){
				b.key(StringUtils.capitalize(facing.getName())).moveFloor();
				if(n!=null) b.add("Connected", DebugJsonBuilder.DebugFormat.TRUE);
				if(a!=null) b.add(a);
				b.prevFloor();
			}
		}
		return b.getDebugInfo();
	}
	
	@Override
	public String toString(){
		return Debugable.toDebugString(getDebugInfo());
	}
	
	@Override
	public boolean hasCapability(@Nonnull Capability<?> cap, @Nullable EnumFacing facing){
		if(facing!=null){
			PipeAttachment attachment = getAttachment(facing);
			if(attachment!=null) return attachment.hasCapability(cap, facing);
		}
		return false;
	}
	
	@Nullable
	@Override
	public <T> T getCapability(@Nonnull Capability<T> cap, @Nullable EnumFacing facing){
		if(facing!=null){
			PipeAttachment attachment = getAttachment(facing);
			if(attachment!=null) return attachment.getCapability(cap, facing);
		}
		return null;
	}
	
	@SuppressWarnings("ConstantConditions")
	public NBTTagCompound serializeNBT(){
		NBTTagCompound nbt = DataUtils.toNBT(pos);
		NBTTagList list = new NBTTagList();
		for(int i = 0; i<6; i++){
			NBTTagCompound subnbt = null;
			if(nodes[i]!=null){
				subnbt = DataUtils.toNBT(nodes[i].getPos());
			}
			if(attachments[i]!=null){
				NBTTagCompound anbt = attachments[i].serializeNBT();
				anbt.setString("provider", attachments[i].getProvider().getRegistryName().toString());
				if(subnbt==null) subnbt = new NBTTagCompound();
				subnbt.setTag("attachment", anbt);
			}
			if(subnbt!=null&&!subnbt.hasNoTags()){
				subnbt.setByte("index", (byte)i);
				list.appendTag(subnbt);
			}
		}
		if(!list.hasNoTags()) nbt.setTag("nodes", list);
		return nbt;
	}
}
