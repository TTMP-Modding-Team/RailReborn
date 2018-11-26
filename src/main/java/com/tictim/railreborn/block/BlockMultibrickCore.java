package com.tictim.railreborn.block;

import com.tictim.railreborn.enums.Multibricks;
import com.tictim.railreborn.tileentity.TEMultibrick;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMultibrickCore extends Block{
	public static final PropertyEnum<Multibricks> PROPERTY = PropertyEnum.create("property", Multibricks.class);
	
	public BlockMultibrickCore(){
		super(Material.ROCK);
	}
	
	// TODO
	@Override
	public boolean onBlockActivated(
			World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ
	){
		if(!world.isRemote){
			TileEntity te = world.getTileEntity(pos);
			
			if(te instanceof TEMultibrick){
				TEMultibrick core = (TEMultibrick)te;
				if(core.isLogicValid()) player.sendMessage(new TextComponentString("The Multibrick is valid!"));
			}
		}
		return true;
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state){
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TEMultibrick){
			TEMultibrick core = (TEMultibrick)te;
			core.onBreak();
		}
		world.removeTileEntity(pos);
	}
	
	@Override
	public IBlockState getStateForPlacement(
			World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand
	){
		return getStateFromMeta(meta).withProperty(BlockHorizontal.FACING, placer.getHorizontalFacing().getOpposite());
	}
	
	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items){
		for(int i = 0; i<2; i++)
			items.add(new ItemStack(this, 1, i));
	}
	
	@Override
	public int getMetaFromState(IBlockState state){
		return state.getValue(PROPERTY).ordinal()+state.getValue(BlockHorizontal.FACING).getHorizontalIndex()*2;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta){
		return this.getDefaultState().withProperty(PROPERTY, Multibricks.fromMeta(meta%2)).withProperty(BlockHorizontal.FACING, EnumFacing.getHorizontal(meta/2));
	}
	
	@Override
	protected BlockStateContainer createBlockState(){
		return new BlockStateContainer(this, PROPERTY, BlockHorizontal.FACING);
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
		return new TEMultibrick().setMultibrick(state.getValue(PROPERTY));
	}
}
