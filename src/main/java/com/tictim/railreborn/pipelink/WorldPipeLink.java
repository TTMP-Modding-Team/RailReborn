package com.tictim.railreborn.pipelink;

import com.tictim.railreborn.pipelink.handler.PipeHandler;
import com.tictim.railreborn.world.WPLSimple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;

import javax.annotation.Nullable;

public interface WorldPipeLink{
	World getWorld();
	void markDirty();
	
	@Nullable
	PipeNode getNode(PipeHandler handler, BlockPos pos);
	PipeNode assign(PipeHandler handler, BlockPos pos);
	void remove(PipeLink link);
	
	static WorldPipeLink get(World world){
		MapStorage storage = world.getPerWorldStorage();
		WPLSimple instance = (WPLSimple)storage.getOrLoadData(WPLSimple.class, WPLSimple.MAP_NAME);
		
		if(instance==null){
			(instance = new WPLSimple(WPLSimple.MAP_NAME)).setWorld(world);
			storage.setData(instance.mapName, instance);
		}else instance.setWorld(world);
		return instance;
	}
}
