package com.tictim.railreborn.pipelink;

import com.google.gson.JsonElement;
import com.tictim.railreborn.RailReborn;
import com.tictim.railreborn.Registries;
import com.tictim.railreborn.capability.Debugable;
import com.tictim.railreborn.multiblock.Blueprint;
import com.tictim.railreborn.pipelink.attachment.PipeAttachment;
import com.tictim.railreborn.pipelink.attachment.provider.PipeAttachmentProvider;
import com.tictim.railreborn.pipelink.handler.PipeHandler;
import com.tictim.railreborn.pipelink.handler.PipeHandlers;
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
	private final WorldPipeLink wpl;
	private final BlockPos pos;
	private final PipeHandler handler;
	private final PipeNode[] nodes = new PipeNode[6];
	private final PipeAttachment[] attachments = new PipeAttachment[6];
	
	public PipeNode(WorldPipeLink wpl, BlockPos pos, PipeHandler handler){
		this.wpl = wpl;
		this.pos = pos;
		this.handler = handler;
	}
	
	public PipeNode(WorldPipeLink wpl, NBTTagCompound nbt){
		this.wpl = wpl;
		this.pos = DataUtils.toBlockPos(nbt);
		this.handler = PipeHandlers.fromId(nbt.getShort("handler"));
	}
	
	@SuppressWarnings("ConstantConditions")
	public void restoreConnection(NBTTagCompound nbt){
		if(nbt.hasKey("nodes", NBTTypes.LIST)){
			NBTTagList list = nbt.getTagList("nodes", NBTTypes.COMPOUND);
			for(int i = 0; i<list.tagCount(); i++){
				NBTTagCompound subnbt = list.getCompoundTagAt(i);
				int idx = subnbt.getInteger("index");
				if(idx >= 0&&idx<6){
					if(subnbt.getBoolean("connected")){
						nodes[idx] = wpl.getNode(this.pos.offset(EnumFacing.getFront(idx)));
					}
					if(subnbt.hasKey("attachment", NBTTypes.COMPOUND)){
						NBTTagCompound anbt = subnbt.getCompoundTag("attachment");
						PipeAttachmentProvider p = Registries.PIPE_ATTACHMENTS.getValue(new ResourceLocation(anbt.getString("provider")));
						attachments[idx] = p.deserializePipeAttachment(handler, anbt);
					}
				}else RailReborn.LOGGER.error("Invalid Index {} on PipeNode at {}!", idx, Blueprint.posToStr(pos));
			}
		}
	}
	
	public BlockPos getPos(){
		return this.pos;
	}
	
	public PipeHandler getPipeHandler(){
		return this.handler;
	}
	
	public boolean connect(PipeNode node, EnumFacing facing, boolean test){
		if(node.handler==this.handler&&this.nodes[facing.getIndex()]==null){
			PipeAttachment a = this.getAttachment(facing);
			if(a==null||a.getProvider().getConnectionRequirement().isConnectedStateValid()){
				if(!test){
					nodes[facing.getIndex()] = node;
					checkAttachment(facing);
				}
				return true;
			}
		}
		return false;
	}
	
	public void disconnect(EnumFacing facing){
		nodes[facing.getIndex()] = null;
		checkAttachment(facing);
	}
	
	public PipeAttachment removeAttachment(EnumFacing facing){
		PipeAttachment a = attachments[facing.getIndex()];
		attachments[facing.getIndex()] = null;
		return a;
	}
	
	public boolean tryConnect(EnumFacing facing){
		PipeNode node2 = wpl.getNode(this.pos.offset(facing));
		if(node2!=null&&this.connect(node2, facing, true)&&node2.connect(this, facing.getOpposite(), true)){
			this.connect(node2, facing, false);
			node2.connect(this, facing.getOpposite(), false);
			node2.updateNode();
			wpl.markDirty();
			return true;
		}else return false;
	}
	
	public boolean tryAppendAttachment(PipeAttachmentProvider provider, EnumFacing facing){
		if(!provider.getConnectionRequirement().isStateValid(this.isConnected(facing))) return false;
		PipeAttachment a = provider.createPipeAttachment(this.handler, this.wpl.getWorld(), this.pos.offset(facing), facing);
		if(a!=null){
			this.attachments[facing.getIndex()] = a;
			wpl.markDirty();
			return true;
		}else return false;
	}
	
	public boolean checkAttachment(EnumFacing facing){
		PipeAttachment a = this.getAttachment(facing);
		if(a!=null&&a.isDismantled(this, facing)){
			InventoryHelper.spawnItemStack(this.wpl.getWorld(), pos.getX(), pos.getY(), pos.getZ(), removeAttachment(facing).getProvider().stackOf());
		}
		return false;
	}
	
	public void updateNode(){
		TileEntity te = this.wpl.getWorld().getTileEntity(this.pos);
		if(te instanceof TEPipe) DataUtils.updateTileEntity(te);
	}
	
	public void disconnectToAll(boolean noDrop){
		World w = this.wpl.getWorld();
		for(int i = 0; i<6; i++){
			if(nodes[i]!=null){
				nodes[i].disconnect(EnumFacing.VALUES[i].getOpposite());
				nodes[i].updateNode();
			}
			if(!noDrop&&attachments[i]!=null){
				ItemStack s = attachments[i].getProvider().stackOf();
				if(!s.isEmpty()) InventoryHelper.spawnItemStack(w, pos.getX(), pos.getY(), pos.getZ(), s.copy());
			}
		}
		if(!noDrop) InventoryHelper.spawnItemStack(w, pos.getX(), pos.getY(), pos.getZ(), this.getPipeHandler().of().copy());
		wpl.remove(this.pos);
	}
	
	@Nullable
	public PipeNode getConnectedNode(EnumFacing facing){
		return nodes[facing.getIndex()];
	}
	
	@Nullable
	public PipeAttachment getAttachment(EnumFacing facing){
		return attachments[facing.getIndex()];
	}
	
	public boolean isConnected(EnumFacing f){
		return getConnectedNode(f)!=null;
	}
	
	public boolean isLoaded(){
		return this.wpl.getWorld().isBlockLoaded(this.pos);
	}
	
	@Override
	public int hashCode(){
		return pos.hashCode();
	}
	
	@Override
	public JsonElement getDebugInfo(){
		DebugJsonBuilder b = new DebugJsonBuilder(pos);
		b.key("").moveFloor();
		b.key("Pipe Handler").add(handler.getRegistryName());
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
			NBTTagCompound subnbt;
			if(nodes[i]!=null){
				subnbt = new NBTTagCompound();
				subnbt.setBoolean("connected", true);
			}else subnbt = null;
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
		nbt.setShort("handler", PipeHandlers.toId(this.handler));
		return nbt;
	}
}
