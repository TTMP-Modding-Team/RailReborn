package com.tictim.railreborn.pipelink;

import com.google.common.collect.Iterators;
import com.google.gson.JsonElement;
import com.tictim.railreborn.RailReborn;
import com.tictim.railreborn.capability.Debugable;
import com.tictim.railreborn.pipelink.handler.PipeHandler;
import com.tictim.railreborn.pipelink.handler.PipeHandlers;
import com.tictim.railreborn.util.DebugJsonBuilder;
import com.tictim.railreborn.util.NBTTypes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.*;

public final class PipeLink implements INBTSerializable<NBTTagCompound>, Iterable<PipeNode>, Debugable{
	private final WorldPipeLink wpl;
	private PipeHandler handler;
	private final Map<BlockPos, PipeNode> nodes = new HashMap<>();
	
	@Nullable
	private PipeLink replacedLink;
	
	public PipeLink(WorldPipeLink wpl){
		this.wpl = wpl;
	}
	
	public PipeNode init(PipeHandler handler, BlockPos initialNode){
		if(!isEmpty()) throw new IllegalStateException("PipeLink#init called twice");
		this.handler = handler;
		PipeNode node = new PipeNode(this, initialNode);
		nodes.put(node.getPos(), node);
		return node;
	}
	
	public void merge(PipeLink link){
		nodes.putAll(link.nodes);
		link.nodes.clear();
		link.replacedLink = this;
		wpl.remove(link);
	}
	
	public void add(PipeNode node){
		nodes.put(node.getPos(), node);
		wpl.markDirty();
	}
	
	public void remove(BlockPos pos){
		if(nodes.remove(pos)!=null) wpl.markDirty();
	}
	
	public void markDirty(){
		wpl.markDirty();
	}
	
	public World getWorld(){
		return this.wpl.getWorld();
	}
	
	public PipeHandler getPipeHandler(){
		return this.handler;
	}
	
	@Nullable
	public PipeNode getNode(BlockPos pos){
		return replacedLink!=null ? replacedLink.getNode(pos) : nodes.get(pos);
	}
	
	public boolean containsNode(BlockPos pos){
		return replacedLink!=null ? replacedLink.containsNode(pos) : nodes.containsKey(pos);
	}
	
	public boolean isEmpty(){
		return replacedLink!=null ? replacedLink.isEmpty() : this.nodes.isEmpty();
	}
	
	public boolean isSameLink(PipeLink link){
		if(this==link) return true;
		else if(this.replacedLink!=null) return this.replacedLink.isSameLink(link);
		else if(link.replacedLink!=null) return this==link.replacedLink;
		else return false;
	}
	
	public Set<Chunk> getOccupyingChunks(){
		Set<Chunk> chunks = new HashSet<>();
		for(BlockPos pos: nodes.keySet()){
			chunks.add(getWorld().getChunkFromBlockCoords(pos));
		}
		return chunks;
	}
	
	public boolean isFullyUnloaded(){
		for(BlockPos pos: nodes.keySet()){
			if(getWorld().isBlockLoaded(pos)) return false;
		}
		return true;
	}
	
	@Override
	public JsonElement getDebugInfo(){
		DebugJsonBuilder b = new DebugJsonBuilder(this.getClass());
		b.key("Pipe Handler").add(handler.getRegistryName());
		if(replacedLink!=null){
			b.key("Referenced Link").add(replacedLink);
		}else{
			for(PipeNode nodes: nodes.values()) b.add(nodes);
		}
		return b.getDebugInfo();
	}
	
	@Override
	public Iterator<PipeNode> iterator(){
		return Iterators.unmodifiableIterator(nodes.values().iterator());
	}
	
	@Override
	public int hashCode(){
		return nodes.hashCode();
	}
	
	@Override
	public NBTTagCompound serializeNBT(){
		NBTTagCompound nbt = new NBTTagCompound();
		if(handler!=PipeHandlers.INVALID) nbt.setString("pipe", getPipeHandler().getRegistryName().toString());
		
		NBTTagList list = new NBTTagList();
		for(Map.Entry<BlockPos, PipeNode> e: nodes.entrySet()){
			list.appendTag(e.getValue().serializeNBT());
		}
		nbt.setTag("nodes", list);
		return nbt;
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound nbt){
		handler = PipeHandlers.REGISTRY.getValue(new ResourceLocation(nbt.getString("pipe")));
		if(handler==PipeHandlers.INVALID) RailReborn.LOGGER.error("Couldn't get PipeHandler of PipeLink.");
		NBTTagList list = nbt.getTagList("nodes", NBTTypes.COMPOUND);
		Map<PipeNode, NBTTagCompound> map = new HashMap<>();
		for(int i = 0; i<list.tagCount(); i++){
			NBTTagCompound subnbt = list.getCompoundTagAt(i);
			PipeNode node = new PipeNode(this, subnbt);
			nodes.put(node.getPos(), node);
			map.put(node, subnbt);
		}
		for(Map.Entry<PipeNode, NBTTagCompound> e: map.entrySet()){
			e.getKey().restoreConnection(e.getValue());
		}
	}
}
