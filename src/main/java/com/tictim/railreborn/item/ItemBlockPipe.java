package com.tictim.railreborn.item;

import com.tictim.railreborn.pipelink.handler.PipeHandlers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlockPipe extends ItemBlockBase{
	public ItemBlockPipe(Block block){
		super(block);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		if(!block.isReplaceable(world, pos)) pos = pos.offset(facing);
		
		ItemStack stack = player.getHeldItem(hand);
		if(!stack.isEmpty()&&player.canPlayerEdit(pos, facing, stack)&&mayPlace(stack, world, pos, facing)){
			int i = this.getMetadata(stack.getMetadata());
			IBlockState statePlacement = this.block.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, i, player, hand);
			
			if(placeBlockAt(stack, player, world, pos, facing, hitX, hitY, hitZ, statePlacement)){
				statePlacement = world.getBlockState(pos);
				SoundType soundtype = statePlacement.getBlock().getSoundType(statePlacement, world, pos, player);
				world.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume()+1.0F)/2.0F, soundtype.getPitch()*0.8F);
				stack.shrink(1);
			}
			
			return EnumActionResult.SUCCESS;
		}else return EnumActionResult.FAIL;
	}
	
	@Override
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack){
		Block block = world.getBlockState(pos).getBlock();
		
		if(block==Blocks.SNOW_LAYER&&block.isReplaceable(world, pos)){
			side = EnumFacing.UP;
		}else if(!block.isReplaceable(world, pos)){
			pos = pos.offset(side);
		}
		
		return mayPlace(stack, world, pos, side);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack){
		return this.block.getUnlocalizedName()+"."+PipeHandlers.fromItem(stack).getRegistryName().getResourcePath();
	}
	
	private boolean mayPlace(ItemStack stack, World world, BlockPos pos, EnumFacing facing){
		AxisAlignedBB aabb = PipeHandlers.fromItem(stack).getDiameter().getCenterBB();
		return (aabb==null||world.checkNoEntityCollision(aabb.offset(pos), null))&&world.mayPlace(this.block, pos, true, facing, (Entity)null);
	}
}
