package com.tictim.railreborn.item;

import org.apache.commons.lang3.Validate;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreIngredient;

public final class IngredientStack{ /* implements Predicate<ItemStack>, com.google.common.base.Predicate<ItemStack> */
	private final Ingredient ing;
	private final int quantity;
	
	public IngredientStack(Ingredient ing){
		this(ing, 1);
	}
	
	public IngredientStack(Ingredient ing, int quantity){
		Validate.inclusiveBetween(1, Integer.MAX_VALUE, quantity);
		this.ing = ing;
		this.quantity = quantity;
	}
	
	public IngredientStack(Item item){
		this(item, 1);
	}
	
	public IngredientStack(Item item, int quantity){
		this(Ingredient.fromItem(item), quantity);
	}
	
	public IngredientStack(Block block){
		this(block, 1);
	}
	
	public IngredientStack(Block block, int quantity){
		this(Ingredient.fromItem(Item.getItemFromBlock(block)), quantity);
	}
	
	public IngredientStack(ItemStack stack){
		this(Ingredient.fromStacks(stack), stack.getCount());
	}
	
	public IngredientStack(String oreDict){
		this(oreDict, 1);
	}
	
	public IngredientStack(String oreDict, int quantity){
		this(new OreIngredient(oreDict), quantity);
	}
	
	public Ingredient getIngredient(){
		return ing;
	}
	
	public int getQuantity(){
		return this.quantity;
	}
	/*
	@Override
	public boolean apply(ItemStack input){
		return test(input);
	}
	
	@Override
	public boolean test(ItemStack t){
		return this.quantity<=0 ? false : ing.test(t)&&this.quantity<=t.getCount();
	}
	*/
	@Override
	public String toString(){
		return "["+ing+"] * "+quantity;
	}
}
