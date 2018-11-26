package com.tictim.railreborn.multiblock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class Blueprint{
	private final Floor[] floors;
	private final BlockPos centerPos;
	private final int groupSize;
	
	Blueprint(Floor[] floors, BlockPos centerPos, int groupSize){
		this.floors = floors;
		this.centerPos = centerPos;
		this.groupSize = groupSize;
	}
	
	public TestResult test(IBlockAccess world, BlockPos core, EnumFacing front){
		TransformableBlockAccess world2 = new TransformableBlockAccess(world, core.subtract(centerPos), front);
		boolean isValid = true;
		for(Floor f : floors){
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
		List<BlockPos> l = new ArrayList<>();
		for(Floor f : floors){
			if(f!=null){
				f.collectGroup(world, l, idx);
			}
			world.getCenter().setY(world.getCenter().getY()+1);
		}
		return l;
	}
	
	public class TestResult{
		private boolean isValid;
		private final TransformableBlockAccess world;
		private final Collection<BlockPos>[] pos = new Collection[groupSize];
		
		public TestResult(boolean isValid, TransformableBlockAccess world){
			this.world = world;
			this.isValid = isValid;
		}
		
		public boolean isValid(){
			return isValid;
		}
		
		public Collection<BlockPos> getGroup(int idx){
			if(pos[idx]==null){
				pos[idx] = collectGroup(world, idx);
			}
			return pos[idx];
		}
	}
}
