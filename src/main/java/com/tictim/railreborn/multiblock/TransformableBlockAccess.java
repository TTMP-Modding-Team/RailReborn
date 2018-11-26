package com.tictim.railreborn.multiblock;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;

public class TransformableBlockAccess implements IBlockAccess{
	private final MutableBlockPos mpos = new MutableBlockPos();
	private final IBlockAccess delegate;
	private final BlockPos centerOrigin;
	private final MutableBlockPos center = new MutableBlockPos();
	private final EnumFacing facing;
	
	public TransformableBlockAccess(IBlockAccess delegate, BlockPos center, EnumFacing facing){
		if(facing==null||facing==EnumFacing.UP||facing==EnumFacing.DOWN) throw new IllegalArgumentException("facing");
		this.delegate = delegate;
		this.center.setPos(this.centerOrigin = center);
		this.facing = facing;
	}
	
	public MutableBlockPos getCenter(){
		return center;
	}
	
	public void retrieveCenter(){
		center.setPos(centerOrigin);
	}
	
	public BlockPos getTransformedCopy(BlockPos pos){
		return transformBlockPos(pos).toImmutable();
	}
	
	private BlockPos transformBlockPos(BlockPos pos){
		return transform(pos.getX(), pos.getY(), pos.getZ());
	}
	
	@SuppressWarnings("incomplete-switch")
	private BlockPos transform(int x, int y, int z){
		mpos.setPos(x-center.getX(), y-center.getY(), z-center.getZ());
		switch(getRotation()){
		case CLOCKWISE_90:
			mpos.setPos(-mpos.getZ(), mpos.getY(), mpos.getX());
			break;
		case CLOCKWISE_180:
			mpos.setPos(-mpos.getX(), mpos.getY(), -mpos.getZ());
			break;
		case COUNTERCLOCKWISE_90:
			mpos.setPos(mpos.getZ(), mpos.getY(), -mpos.getX());
			break;
		// default: case NONE:
		}
		return mpos;
	}
	
	private Rotation getRotation(){
		switch(facing){
		case EAST:
			return Rotation.COUNTERCLOCKWISE_90;
		case SOUTH:
			return Rotation.CLOCKWISE_180;
		case WEST:
			return Rotation.CLOCKWISE_90;
		default: //	case NORTH: case UP: case DOWN:
			return Rotation.NONE;
		}
	}
	
	@Override
	public TileEntity getTileEntity(BlockPos pos){
		return delegate.getTileEntity(transformBlockPos(pos));
	}
	
	@Override
	public int getCombinedLight(BlockPos pos, int lightValue){
		return delegate.getCombinedLight(transformBlockPos(pos), lightValue);
	}
	
	@Override
	public IBlockState getBlockState(BlockPos pos){
		return delegate.getBlockState(transformBlockPos(pos));
	}
	
	@Override
	public boolean isAirBlock(BlockPos pos){
		return delegate.isAirBlock(transformBlockPos(pos));
	}
	
	@Override
	public Biome getBiome(BlockPos pos){
		return delegate.getBiome(transformBlockPos(pos));
	}
	
	@Override
	public int getStrongPower(BlockPos pos, EnumFacing direction){
		return delegate.getStrongPower(transformBlockPos(pos), direction);
	}
	
	@Override
	public WorldType getWorldType(){
		return delegate.getWorldType();
	}
	
	@Override
	public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default){
		return delegate.isSideSolid(transformBlockPos(pos), getRotation().rotate(side), _default);
	}
	
	public TileEntity getTileEntity(int x, int y, int z){
		return delegate.getTileEntity(transform(x, y, z));
	}
	
	public int getCombinedLight(int x, int y, int z, int lightValue){
		return delegate.getCombinedLight(transform(x, y, z), lightValue);
	}
	
	public IBlockState getBlockState(int x, int y, int z){
		return delegate.getBlockState(transform(x, y, z));
	}
	
	public boolean isAirBlock(int x, int y, int z){
		return delegate.isAirBlock(transform(x, y, z));
	}
	
	public Biome getBiome(int x, int y, int z){
		return delegate.getBiome(transform(x, y, z));
	}
	
	public int getStrongPower(int x, int y, int z, EnumFacing direction){
		return delegate.getStrongPower(transform(x, y, z), direction);
	}
	
	public boolean isSideSolid(int x, int y, int z, EnumFacing side, boolean _default){
		return delegate.isSideSolid(transform(x, y, z), getRotation().rotate(side), _default);
	}
}
