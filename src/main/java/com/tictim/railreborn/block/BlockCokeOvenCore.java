package com.tictim.railreborn.block;

import com.tictim.railreborn.RailRebornGui;
import com.tictim.railreborn.enums.Multibricks;
import com.tictim.railreborn.tileentity.TEMultibrick;
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

public class BlockCokeOvenCore extends BlockMultiblockCore<TEMultibrick>{
	public BlockCokeOvenCore(){
		super(Material.ROCK);
		this.setSoundType(SoundType.STONE);
	}
	
	@Nullable
	@Override
	protected TEMultibrick tryConvertTE(TileEntity te){
		if(!(te instanceof TEMultibrick)) return null;
		TEMultibrick core = (TEMultibrick)te;
		return core.getMultibrick()==Multibricks.COKE_OVEN&&core.isLogicValid() ? core : null;
	}
	
	@Override
	protected void handleBlockActivate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ, TEMultibrick te){
		if(!FluidUtil.interactWithFluidHandler(player, hand, world, pos, facing)) RailRebornGui.COKE_OVEN.openGui(player, world, pos);
	}
	
	@Override
	public TEMultibrick createTileEntity(World world, IBlockState state){
		return new TEMultibrick().setMultibrick(Multibricks.COKE_OVEN);
	}
}
