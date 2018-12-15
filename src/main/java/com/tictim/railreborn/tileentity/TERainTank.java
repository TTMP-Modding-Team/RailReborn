package com.tictim.railreborn.tileentity;

import com.tictim.railreborn.client.tesr.TESRMultiblockDebug.MultiblockDebugable;
import com.tictim.railreborn.logic.Logic;
import com.tictim.railreborn.logic.LogicRainTank;
import com.tictim.railreborn.multiblock.Blueprint;
import com.tictim.railreborn.multiblock.Blueprints;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class TERainTank extends TELogic implements ITickable, MultiblockDebugable{
	private int tick;
	
	{
		this.resetLogic();
	}
	
	@Override
	public void update(){
		if(!this.world.isRemote){
			if((++tick) >= 10){
				tick = 0;
				Blueprint.TestResult r = Blueprints.RAIN_TANK.test(world, pos);
				if(logic.isValid()!=r.isValid()){
					if(r.isValid()) logic.validate(this, r);
					else logic.invalidate(this, r);
				}
			}
			if(logic.isValid()) logic.update();
		}
	}
	
	@Nullable
	@Override
	public Blueprint.TestResult getMultiblockTest(){
		return Blueprints.RAIN_TANK.test(world, pos);
	}
	
	@Override
	protected Logic createNewLogic(){
		return new LogicRainTank();
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState){
		return oldState.getBlock()!=newState.getBlock();
	}
}
