package com.tictim.railreborn.item;

import static com.tictim.railreborn.item.ModItems.mrl;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;
import com.google.common.collect.ImmutableSet;
import com.tictim.railreborn.enums.Shape;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class ItemRefinedMetal extends Item{
	public ItemRefinedMetal(){
		this.setHasSubtypes(true);
	}
	
	public ItemRefinedMetal setShapes(Shape... availableShapes){
		this.availableShapes = ImmutableSet.copyOf(availableShapes).toArray(new Shape[0]);
		Validate.notEmpty(this.availableShapes);
		this.setHasSubtypes(this.availableShapes.length>1);
		this.itemForShapes = null;
		return this;
	}
	
	private Shape[] availableShapes = Shape.values();
	
	private Shape getShape(int meta){
		return availableShapes[MathHelper.clamp(meta, 0, availableShapes.length)];
	}
	
	private ItemStack[] itemForShapes;
	
	public ItemStack of(Shape shape, int amount){
		if(amount<=0) return ItemStack.EMPTY;
		ItemStack s = of(shape).copy();
		if(amount>1) s.setCount(amount);
		return s;
	}
	
	public ItemStack of(Shape shape){
		int idx = ArrayUtils.indexOf(availableShapes, shape);
		return idx<0 ? ItemStack.EMPTY : of(idx);
	}
	
	private ItemStack of(int shapeIdx){
		if(itemForShapes==null){
			itemForShapes = new ItemStack[availableShapes.length];
			for(int i = 0; i<availableShapes.length; i++)
				itemForShapes[i] = new ItemStack(this, 1, i);
		}
		return itemForShapes[shapeIdx];
	}
	
	public void registerOre(String... prefix){
		for(Shape s: availableShapes){
			ItemStack stack = of(s);
			for(String p: prefix)
				OreDictionary.registerOre(s.oreName(p), stack);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void registerModels(String prefix){
		for(int i = 0; i<availableShapes.length; i++)
			ModelLoader.setCustomModelResourceLocation(this, i, mrl("metal/"+prefix+"_"+availableShapes[i]));
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack){
		Shape shape = getShape(stack.getItemDamage());
		return super.getUnlocalizedName(stack)+"."+shape;
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items){
		if(this.isInCreativeTab(tab)) for(Shape s: availableShapes)
			items.add(of(s));
	}
}
