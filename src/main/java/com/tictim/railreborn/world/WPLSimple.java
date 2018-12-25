package com.tictim.railreborn.world;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonElement;
import com.tictim.railreborn.RailReborn;
import com.tictim.railreborn.capability.Debugable;
import com.tictim.railreborn.pipelink.PipeLink;
import com.tictim.railreborn.pipelink.PipeNode;
import com.tictim.railreborn.pipelink.WorldPipeLink;
import com.tictim.railreborn.pipelink.handler.PipeHandler;
import com.tictim.railreborn.util.DebugJsonBuilder;
import com.tictim.railreborn.util.NBTTypes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;

import javax.annotation.Nullable;

public final class WPLSimple extends WorldSavedData implements WorldPipeLink, Debugable{
	public static final String MAP_NAME = RailReborn.MODID+":wpl";
	
	private final Multimap<PipeHandler, PipeLink> links = HashMultimap.create();
	
	private World world;
	
	public WPLSimple(String key){
		super(key);
	}
	
	public void setWorld(World world){
		this.world = world;
	}
	
	@Override
	public World getWorld(){
		return world;
	}
	
	@Nullable
	@Override
	public PipeNode getNode(PipeHandler handler, BlockPos pos){
		for(PipeLink link: links.get(handler)){
			if(link.containsNode(pos)) return link.getNode(pos);
		}
		return null;
	}
	
	@Override
	public PipeNode assign(PipeHandler handler, BlockPos pos){
		PipeLink link = new PipeLink(this);
		links.put(handler, link);
		markDirty();
		return link.init(handler, pos);
	}
	
	@Override
	public void remove(PipeLink link){
		this.links.remove(link.getPipeHandler(), link);
		markDirty();
	}
	
	@Override
	public int hashCode(){
		return links.hashCode();
	}
	
	@Override
	public JsonElement getDebugInfo(){
		DebugJsonBuilder b = new DebugJsonBuilder();
		b.key("").moveFloor();
		for(PipeLink l: this.links.values()) b.add(l);
		return b.getDebugInfo();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		NBTTagList list = new NBTTagList();
		for(PipeLink l: links.values()){
			if(!l.isEmpty()){
				NBTTagCompound subnbt = l.serializeNBT();
				list.appendTag(subnbt);
			}
		}
		if(!list.hasNoTags()) nbt.setTag("links", list);
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt){
		NBTTagList list = nbt.getTagList("links", NBTTypes.COMPOUND);
		for(int i = 0; i<list.tagCount(); i++){
			NBTTagCompound subnbt = list.getCompoundTagAt(i);
			PipeLink l = new PipeLink(this);
			l.deserializeNBT(subnbt);
			links.put(l.getPipeHandler(), l);
		}
		RailReborn.LOGGER.info("{}, {}", nbt, this.getDebugInfo());
	}
}
