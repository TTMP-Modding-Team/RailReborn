package com.tictim.railreborn.block;

import com.tictim.railreborn.enums.Multibricks;
import com.tictim.railreborn.tileentity.TEMultibrick;
import com.tictim.railreborn.tileentity.TEMultibrickPart;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMultibrickPart extends BlockVar<Multibricks>{
	public BlockMultibrickPart(){
		super(Material.ROCK, SoundType.STONE);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TEMultibrickPart){
			TEMultibrickPart part = (TEMultibrickPart)te;
			TEMultibrick core = part.getCore();
			if(core!=null&&core.isLogicValid()){
				core.getMultibrick().getGui(false).openGui(player, world, pos);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items){
		for(int i = 0; i<2; i++)
			items.add(new ItemStack(this, 1, i));
	}
	
	@Override
	public IProperty<Multibricks> getProperty(){
		return BlockMultibrickCore.PROPERTY;
	}
	
	@Override
	public Multibricks fromMeta(int meta){
		return Multibricks.fromMeta(meta);
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
		return new TEMultibrickPart();
	}
}
