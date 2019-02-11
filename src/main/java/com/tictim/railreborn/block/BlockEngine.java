package com.tictim.railreborn.block;

import com.tictim.railreborn.enums.Engines;
import com.tictim.railreborn.tileentity.TEEngine;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;

public class BlockEngine extends Block{
	public static final PropertyEnum<State> STATE = PropertyEnum.create("state", State.class);
	public static final PropertyEnum<Part> PART = PropertyEnum.create("part", Part.class);
	
	private final Engines engine;

	public float progress = 0;
	
	public BlockEngine(Engines engine){
		super(Material.IRON);
		this.engine = engine;
		this.setDefaultState(this.blockState.getBaseState().withProperty(BlockHorizontal.FACING, EnumFacing.NORTH).withProperty(STATE, State.FLOOR).withProperty(PART, Part.BASE));
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		if(!world.isRemote){
			TileEntity te = world.getTileEntity(pos);
			if(te instanceof TEEngine){
				if(!FluidUtil.interactWithFluidHandler(player, hand, world, pos, null)) engine.getGui().openGui(player, world, pos);
			}
		}
		return true;
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state){
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TEEngine){
			TEEngine core = (TEEngine)te;
			core.invalidateLogic();
		}
		world.removeTileEntity(pos);
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
		switch(facing){
			case UP:
				return getDefaultState().withProperty(STATE, State.FLOOR).withProperty(BlockHorizontal.FACING, placer.getHorizontalFacing().getOpposite());
			case DOWN:
				return getDefaultState().withProperty(STATE, State.CEILING).withProperty(BlockHorizontal.FACING, placer.getHorizontalFacing().getOpposite());
			default:
				return getDefaultState().withProperty(STATE, State.WALL).withProperty(BlockHorizontal.FACING, facing);
		}
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta){
		return this.getDefaultState().withProperty(STATE, State.values()[meta/4]).withProperty(BlockHorizontal.FACING, EnumFacing.HORIZONTALS[meta%4]);
	}
	
	@Override
	public int getMetaFromState(IBlockState state){
		return state.getValue(STATE).ordinal()*4+state.getValue(BlockHorizontal.FACING).getHorizontalIndex();
	}
	
	@Override
	protected BlockStateContainer createBlockState(){
		return new BlockStateContainer(this, BlockHorizontal.FACING, STATE, PART);
	}

	@Override
	public boolean isFullCube(IBlockState state){
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state){
		return false;
	}
	
	@Override
	public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, EntityLiving.SpawnPlacementType type){
		return false;
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state){
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state){
		return new TEEngine().setEngine(this.engine);
	}
	
	public enum State implements IStringSerializable{
		FLOOR,
		WALL,
		CEILING;
		
		@Override
		public String getName(){
			return name().toLowerCase();
		}
	}

	public enum Part implements IStringSerializable{
		BASE,
		LEFT,
		RIGHT;

		@Override
		public String getName() {return name().toLowerCase();}
	}
}
