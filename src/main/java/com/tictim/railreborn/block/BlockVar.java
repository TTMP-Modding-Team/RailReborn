package com.tictim.railreborn.block;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;

public abstract class BlockVar<E extends Enum<E>&IStringSerializable> extends Block{
	public static <E extends Enum<E>&IStringSerializable> BlockVar<E> create(Material m, SoundType s, Class<E> e, E... vars){
		return create(m, s, PropertyEnum.create("property", e, vars));
	}
	
	public static <E extends Enum<E>&IStringSerializable> BlockVar<E> create(Material m, SoundType s, Class<E> e){
		return create(m, s, PropertyEnum.create("property", e));
	}
	
	public static <E extends Enum<E>&IStringSerializable> BlockVar<E> create(Material m, SoundType s, IProperty<E> property){
		List<E> l = Lists.newArrayList(property.getAllowedValues());
		Collections.sort(l);
		return new BlockVar<E>(m, s){
			@Override
			public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items){
				for(int i = 0; i<l.size(); i++)
					items.add(new ItemStack(this, 1, i));
			}
			
			@Override
			public IProperty<E> getProperty(){
				return property;
			}
			
			@Override
			public E fromMeta(int meta){
				return l.get(meta >= 0&&meta<l.size() ? meta : 0);
			}
		};
	}
	
	public BlockVar(Material m, SoundType s){
		super(m);
		this.setSoundType(s);
	}
	
	@Override
	public int damageDropped(IBlockState state){
		return state.getValue(getProperty()).ordinal();
	}
	
	@Override
	public int getMetaFromState(IBlockState state){
		return state.getValue(getProperty()).ordinal();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta){
		return this.getDefaultState().withProperty(getProperty(), fromMeta(meta));
	}
	
	@Override
	protected BlockStateContainer createBlockState(){
		return new BlockStateContainer(this, getProperty());
	}
	
	public abstract IProperty<E> getProperty();
	
	public abstract E fromMeta(int meta);
	
	public IBlockState withVar(E value){
		return this.getDefaultState().withProperty(getProperty(), value);
	}
}
