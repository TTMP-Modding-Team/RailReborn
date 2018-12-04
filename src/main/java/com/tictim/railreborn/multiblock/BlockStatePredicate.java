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
		this.exact = exact|!state.getPropertyKeys().stream().anyMatch(k -> k.getName().equals("facing"));
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
	/*
	@Override
	public boolean equals(Object obj){
		if(obj==null) return false;
		else if(obj==this) return true;
		else if(obj instanceof BlockStatePredicate){
			BlockStatePredicate p = (BlockStatePredicate)obj;
			return this.actual==p.actual&&this.exact==p.exact&&(this.exact ? this.state==p.state : this.matches(p.state)&&p.matches(this.state));
		}else return false;
	}
	*/
	
	@Override
	public String toString(){
		String s = "S:"+state;
		if(exact) s += " exact";
		if(actual) s += " actual";
		return s;
	}
}
