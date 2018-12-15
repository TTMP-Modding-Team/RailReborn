package com.tictim.railreborn.multiblock;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;

public class Blueprint{
	private final Floor[] floors;
	private final BlockPos centerPos;
	private final int groupSize;
	
	Blueprint(Floor[] floors, BlockPos centerPos, int groupSize){
		this.floors = floors;
		this.centerPos = centerPos;
		this.groupSize = groupSize;
	}
	
	public TestResult test(IBlockAccess world, BlockPos core){
		IBlockState s = world.getBlockState(core);
		if(s.getPropertyKeys().contains(BlockHorizontal.FACING)){
			return test(world, core, s.getValue(BlockHorizontal.FACING));
		}else if(s.getPropertyKeys().contains(BlockDirectional.FACING)){
			EnumFacing f = s.getValue(BlockDirectional.FACING);
			if(f.getAxis()!=Axis.Y) return test(world, core, f);
		}
		/*
		TestResult r = null;
		for(EnumFacing facing : EnumFacing.HORIZONTALS){
			r = test(world, core, facing);
			if(r.isValid()) break;
		}
		return r;
		*/
		return new TestResult(false, null);
	}
	
	public TestResult test(IBlockAccess world, BlockPos core, EnumFacing front){
		TransformableBlockAccess world2 = new TransformableBlockAccess(world, core, centerPos, front);
		boolean isValid = true;
		for(Floor f: floors){
			if(f!=null&&!f.test(world2)){
				isValid = false;
				break;
			}
			world2.getCenter().setY(world2.getCenter().getY()+1);
		}
		TestResult result = new TestResult(isValid, world2);
		return result;
	}
	
	private Collection<BlockPos> collectGroup(TransformableBlockAccess world, int idx){
		world.retrieveCenter();
		ImmutableSet.Builder<BlockPos> l = new ImmutableSet.Builder<>();
		for(Floor f: floors){
			if(f!=null){
				f.collectGroup(world, l, idx);
			}
			world.getCenter().setY(world.getCenter().getY()+1);
		}
		return l.build();
	}
	
	@Override
	public String toString(){
		StringBuilder floors = new StringBuilder("\n"), groups = new StringBuilder("\n");
		for(int i = 0; i<this.floors.length; i++){
			if(i>0) floors.append("\n,\n");
			Floor f = this.floors[i];
			if(f!=null) f.appendFloors(floors);
			else floors.append("\t[ EMPTY FLOOR ]");
		}
		for(int g = 0; g<this.groupSize; g++){
			if(g>0) groups.append(",");
			groups.append(g).append(": {");
			
			StringBuilder stb3 = new StringBuilder("\n");
			for(int i = 0; i<this.floors.length; i++){
				if(i>0) stb3.append("\n,\n");
				Floor f = this.floors[i];
				if(f!=null) f.appendGroups(stb3, g);
				else stb3.append("[ EMPTY FLOOR ]");
			}
			groups.append(stb3.toString().replace("\n", "\n\t")).append("\n}");
		}
		return String.format("floors: {%s\n}, groups: {%s\n}, center: %s", floors.toString().replace("\n", "\n\t"), groups.toString().replace("\n", "\n\t"), posToStr(centerPos));
	}
	
	// TODO Code organization
	public static String posToStr(BlockPos pos){
		return new StringBuilder().append("(").append(pos.getX()).append(" ").append(pos.getY()).append(" ").append(pos.getZ()).append(")").toString();
	}
	
	public class TestResult{
		private boolean isValid;
		@Nullable
		private final TransformableBlockAccess world;
		private final Collection<BlockPos>[] pos = new Collection[groupSize];
		
		public TestResult(boolean isValid, @Nullable TransformableBlockAccess world){
			this.world = world;
			this.isValid = isValid;
		}
		
		@Nullable
		public TransformableBlockAccess getWorld(){
			return world;
		}
		
		public boolean isValid(){
			return isValid;
		}
		
		public int groups(){
			return groupSize;
		}
		
		public Collection<BlockPos> getGroup(int idx){
			if(pos[idx]==null) pos[idx] = world==null ? Collections.emptyList() : collectGroup(world, idx);
			return pos[idx];
		}
	}
}
