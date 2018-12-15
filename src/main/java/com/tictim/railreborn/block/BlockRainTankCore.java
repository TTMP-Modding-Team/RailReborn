package com.tictim.railreborn.block;

import com.tictim.railreborn.tileentity.TERainTank;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;

import javax.annotation.Nullable;

public class BlockRainTankCore extends BlockMultiblockCore<TERainTank>{
	public BlockRainTankCore(){
		super(Material.WOOD);
		this.setSoundType(SoundType.WOOD);
	}
	
	@Nullable
	@Override
	protected TERainTank tryConvertTE(TileEntity te){
		if(!(te instanceof TERainTank)) return null;
		TERainTank core = (TERainTank)te;
		return core.isLogicValid() ? core : null;
	}
	
	@Override
	protected void handleBlockActivate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ, TERainTank teRainTank){
		FluidUtil.interactWithFluidHandler(player, hand, world, pos, facing);
	}
	
	@Override
	public TERainTank createTileEntity(World world, IBlockState state){
		return new TERainTank();
	}
}
