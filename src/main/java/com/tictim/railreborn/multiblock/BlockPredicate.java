package com.tictim.railreborn.multiblock;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public abstract class BlockPredicate{
	public static final BlockPredicate AIR = new BlockPredicate(){
		@Override
		public boolean matches(IBlockAccess world, BlockPos pos){
			return world.isAirBlock(pos);
		}
		
		@Override
		public IBlockState example(){
			return Blocks.AIR.getDefaultState();
		}
		
		@Override
		public String toString(){
			return "AIR";
		}
	};
	public static final BlockPredicate ANY = new BlockPredicate(){
		@Override
		public boolean matches(IBlockAccess world, BlockPos pos){
			return true;
		}
		
		@Override
		public IBlockState example(){
			return Blocks.AIR.getDefaultState();
		}
		
		@Override
		public String toString(){
			return "ANY";
		}
	};
	
	public static BlockPredicate of(Object o){
		if(o==Blocks.AIR) return AIR;
		else if(o instanceof IBlockState) return new BlockStatePredicate((IBlockState)o);
		else if(o instanceof Block) return new BlockSimplePredicate((Block)o);
		else if(o instanceof BlockPredicate) return (BlockPredicate)o;
		else throw new IllegalArgumentException("Object "+o+" cannot be parsed to BlockPredicate");
	}
	
	public static boolean isStateEqualsExceptFacing(IBlockState s1, IBlockState s2){
		if(s1.getBlock()==s2.getBlock()){
			for(IProperty<?> p: s1.getPropertyKeys())
				if(!s1.getValue(p).equals(s2.getValue(p))&&!p.getName().equals("facing")) return false;
			return true;
		}
		return false;
	}
	
	public abstract boolean matches(IBlockAccess world, BlockPos pos);
	public abstract IBlockState example();
}
