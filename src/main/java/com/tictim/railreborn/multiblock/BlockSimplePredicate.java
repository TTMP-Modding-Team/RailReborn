package com.tictim.railreborn.multiblock;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockSimplePredicate extends BlockPredicate{
	private final Block block;
	
	public BlockSimplePredicate(Block block){
		this.block = block;
	}
	
	@Override
	public boolean matches(IBlockAccess world, BlockPos pos){
		IBlockState state = world.getBlockState(pos);
		Block b = state.getBlock();
		return b.isAir(state, world, pos) ? this.block==Blocks.AIR : state.getBlock()==this.block;
	}
	
	@Override
	public IBlockState example(){
		return block.getDefaultState();
	}
	/*
	@Override
	public boolean equals(Object obj){
		if(obj==null) return false;
		else if(obj==this) return true;
		else if(obj instanceof BlockSimplePredicate) {
			BlockSimplePredicate p = (BlockSimplePredicate)obj;
			return p.block==this.block;
		}else return false;
	}
	*/
	
	@Override
	public String toString(){
		return "B:"+block.getRegistryName();
	}
}
