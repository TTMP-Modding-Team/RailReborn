package com.tictim.railreborn.multiblock;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockStatePredicate extends BlockPredicate{
	private final IBlockState state;
	private final boolean exact, actual;
	
	public BlockStatePredicate(IBlockState state){
		this(state, false, false);
	}
	
	public BlockStatePredicate(IBlockState state, boolean exact, boolean actual){
		this.state = state;
		this.exact = exact;
		this.actual = actual;
	}
	
	@Override
	public boolean matches(IBlockAccess world, BlockPos pos){
		return matches(actual ? world.getBlockState(pos).getActualState(world, pos) : world.getBlockState(pos));
	}
	
	private boolean matches(IBlockState state){
		return state==this.state ? true : (exact ? false : isStateEqualsExceptFacing(state, this.state));
	}
	
	@Override
	public IBlockState example(){
		return state;
	}
}
