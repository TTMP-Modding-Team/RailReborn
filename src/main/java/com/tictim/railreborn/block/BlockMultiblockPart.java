package com.tictim.railreborn.block;

import com.tictim.railreborn.tileentity.TEMultiblockPart;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMultiblockPart extends Block{
	public BlockMultiblockPart(Material m, SoundType s){
		super(m);
		this.setSoundType(s);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TEMultiblockPart){
			TEMultiblockPart part = (TEMultiblockPart)te;
			TileEntity c = part.getCore();
			if(c!=null){
				IBlockState coreState = c.getWorld().getBlockState(c.getPos());
				return coreState.getBlock().onBlockActivated(c.getWorld(), c.getPos(), coreState, player, hand, facing, hitX, hitY, hitZ);
			}
		}
		return false;
	}
	
	@Override
	public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, SpawnPlacementType type){
		return false;
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state){
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state){
		return new TEMultiblockPart();
	}
}
