package com.tictim.railreborn.block;

import com.tictim.railreborn.enums.Engines;
import com.tictim.railreborn.tileentity.TEEngine;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockEngineRedstone extends BlockEngine{
	public BlockEngineRedstone(){
		super(Engines.REDSTONE_REPEATER);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		return false;
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack){
		if(!world.isRemote) recalculateRedstoneEngine(world, pos);
	}
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos){
		if(!world.isRemote) recalculateRedstoneEngine(world, pos);
	}
	
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state){
		if(!world.isRemote&&world.getTileEntity(pos)==null) recalculateRedstoneEngine(world, pos);
	}
	
	@Override
	public boolean eventReceived(IBlockState state, World world, BlockPos pos, int id, int param){
		if(!world.isRemote) recalculateRedstoneEngine(world, pos);
		return false;
	}
	
	private static void recalculateRedstoneEngine(World world, BlockPos pos){
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TEEngine){
			TEEngine engine = (TEEngine)te;
			if(engine.getEngine()==Engines.REDSTONE_REPEATER){
				boolean powered = false;
				for(EnumFacing f: EnumFacing.values()){
					if(world.isSidePowered(pos.offset(f), f)){
						powered = true;
						break;
					}
				}
				// TODO and what to do?
			}
		}
	}
}
