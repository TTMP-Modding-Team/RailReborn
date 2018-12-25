package com.tictim.railreborn.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public interface Microblock{
	AxisAlignedBB[] getBoundingBoxes(World world, BlockPos pos, IBlockState state);
	
	@Nullable
	static AxisAlignedBB getCollidedAABB(AxisAlignedBB[] aabbs, Vec3d hitVec){
		return getCollidedAABB(aabbs, hitVec.x, hitVec.y, hitVec.z);
	}
	
	@Nullable
	static AxisAlignedBB getCollidedAABB(AxisAlignedBB[] aabbs, double x, double y, double z){
		for(AxisAlignedBB aabb: aabbs)
			if(x >= aabb.minX&&x<=aabb.maxX&&y >= aabb.minY&&y<=aabb.maxY&&z >= aabb.minZ&&z<=aabb.maxZ) return aabb;
		return null;
	}
	
	static AxisAlignedBB getNearestAABB(AxisAlignedBB[] aabbs, Vec3d hitVec){
		return getNearestAABB(aabbs, hitVec.x, hitVec.y, hitVec.z);
	}
	
	static AxisAlignedBB getNearestAABB(AxisAlignedBB[] aabbs, double x, double y, double z){
		AxisAlignedBB nearest = null;
		double dist = Double.POSITIVE_INFINITY;
		for(AxisAlignedBB aabb: aabbs){
			double nx = MathHelper.clamp(x, aabb.minX, aabb.maxX)-x;
			double ny = MathHelper.clamp(y, aabb.minY, aabb.maxY)-y;
			double nz = MathHelper.clamp(z, aabb.minZ, aabb.maxZ)-z;
			double dist2 = Math.sqrt(nx*nx+ny*ny+nz*nz);
			if(dist2<dist){
				nearest = aabb;
				dist = dist2;
			}
		}
		return nearest;
	}
}
