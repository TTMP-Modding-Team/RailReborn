package com.tictim.railreborn.pipelink;

import com.google.gson.JsonElement;
import com.tictim.railreborn.RailReborn;
import com.tictim.railreborn.capability.Debugable;
import com.tictim.railreborn.pipelink.handler.PipeHandler;
import com.tictim.railreborn.util.DebugJsonBuilder;
import com.tictim.railreborn.util.NBTTypes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public final class WorldPipeLink extends WorldSavedData implements Debugable{
	public static final String MAP_NAME = RailReborn.MODID+":wpl";
	
	private final Map<BlockPos, PipeNode> nodes = new HashMap<>();
	
	private World world;
	
	public WorldPipeLink(World world){
		super(MAP_NAME);
		setWorld(world);
	}
	
	public WorldPipeLink(String key){
		super(key);
	}
	
	public void setWorld(World world){
		this.world = world;
	}
	
	public World getWorld(){
		return world;
	}
	
	@Nullable
	public PipeNode getNode(BlockPos pos){
		return nodes.get(pos);
	}
	
	public PipeNode create(BlockPos pos, PipeHandler handler){
		if(nodes.containsKey(pos)) return nodes.get(pos);
		PipeNode node = new PipeNode(this, pos, handler);
		nodes.put(pos, node);
		markDirty();
		return node;
	}
	
	public void remove(BlockPos pos){
		if(this.nodes.remove(pos)!=null) markDirty();
	}
	
	@Override
	public int hashCode(){
		return nodes.hashCode();
	}
	
	@SideOnly(Side.CLIENT)
	public Map<BlockPos, PipeNode> map(){
		return nodes;
	}
	
	@Override
	public JsonElement getDebugInfo(){
		DebugJsonBuilder b = new DebugJsonBuilder();
		b.key("").moveFloor();
		for(PipeNode n: this.nodes.values()) b.add(n);
		return b.getDebugInfo();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		NBTTagList list = new NBTTagList();
		for(PipeNode n: this.nodes.values()){
			NBTTagCompound subnbt = n.serializeNBT();
			list.appendTag(subnbt);
		}
		if(!list.hasNoTags()) nbt.setTag("links", list);
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt){
		NBTTagList list = nbt.getTagList("links", NBTTypes.COMPOUND);
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
		RailReborn.LOGGER.info(Debugable.toDebugString(this.getDebugInfo()));
	}
	
	public static WorldPipeLink get(World world){
		MapStorage storage = world.getPerWorldStorage();
		WorldPipeLink instance = (WorldPipeLink)storage.getOrLoadData(WorldPipeLink.class, WorldPipeLink.MAP_NAME);
		
		if(instance==null){
			(instance = new WorldPipeLink(WorldPipeLink.MAP_NAME)).setWorld(world);
			storage.setData(instance.mapName, instance);
		}else instance.setWorld(world);
		return instance;
	}
}
