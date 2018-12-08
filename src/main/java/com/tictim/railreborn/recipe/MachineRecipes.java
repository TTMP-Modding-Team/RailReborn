package com.tictim.railreborn.recipe;

import com.tictim.railreborn.enums.Misc;
import com.tictim.railreborn.enums.Shape;
import com.tictim.railreborn.fluid.ModFluids;
import com.tictim.railreborn.item.IngredientStack;
import com.tictim.railreborn.item.ModItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum MachineRecipes implements MachineRecipe{
	INSTANCE;
	
	public static final Pattern RECIPE_KEY_PATTERN = Pattern.compile("^([a-zA-Z_-]+).([a-zA-Z_-][.a-zA-Z_-]*)$");
	
	private final Map<String, MachineRecipe> recipes = new HashMap<>();
	
	public void addRecipe(MachineRecipe e){
		e.expectValidKey();
		if(recipes.containsKey(e.getKey())){
			throw new IllegalStateException("MachineRecipe named "+e.getKey()+" already exists");
		}else{
			recipes.put(e.getKey(), e);
		}
	}
	
	@Override
	@Nullable
	public Crafting getCrafting(Machine m){
		for(MachineRecipe e: recipes.values()){
			Crafting c = e.getCrafting(m);
			if(c!=null) return c.setRecipeKey(c.getRecipeKey());
		}
		return null;
	}
	
	@Override
	@Nullable
	public Crafting getCrafting(ItemStack input){
		for(MachineRecipe e: recipes.values()){
			Crafting c = e.getCrafting(input);
			if(c!=null) return c.setRecipeKey(c.getRecipeKey());
		}
		return null;
	}
	
	@Override
	@Nullable
	public Crafting getCrafting(Fluid fluid){
		for(MachineRecipe e: recipes.values()){
			Crafting c = e.getCrafting(fluid);
			if(c!=null) return c.setRecipeKey(c.getRecipeKey());
		}
		return null;
	}
	
	@Nullable
	@Override
	public Crafting getCrafting(String key){
		Matcher m = MachineRecipes.RECIPE_KEY_PATTERN.matcher(key);
		if(m.matches()){
			MachineRecipe e = recipes.get(m.group(1));
			if(e!=null){
				Crafting c = e.getCrafting(m.group(2));
				if(c!=null) return c.setRecipeKey(c.getRecipeKey());
			}
		}
		return null;
	}
	
	@Override
	public String getKey(){
		return "";
	}
	
	public static final MachineRecipeList COKE_OVEN = new MachineRecipeList("coke_oven");
	public static final MachineRecipeList BLAST_FURNACE = new MachineRecipeList("blast_furnace");
	
	public static void addMachineRecipes(){
		INSTANCE.addRecipe(COKE_OVEN);
		INSTANCE.addRecipe(BLAST_FURNACE);
		
		COKE_OVEN.addRecipe(new CokeOvenRecipe(new Crafting(DummyCraftingHandler.INSTANCE).setRecipeKey("coke")//
				.setInput(new IngredientStack(new ItemStack(Items.COAL)))//
				.setOutput(Misc.COKE.of())//
				.setFluidOutput(new FluidStack(ModFluids.CREOSOTE_OIL, 500))//
				.setTotalTime(3000)));
		COKE_OVEN.addRecipe(new CokeOvenRecipe(new Crafting(DummyCraftingHandler.INSTANCE).setRecipeKey("coke_block")//
				.setInput(new IngredientStack("blockCoal"))//
				.setOutput(new ItemStack(ModItems.COAL_COKE_BLOCK))//
				.setFluidOutput(new FluidStack(ModFluids.CREOSOTE_OIL, 4500))//
				.setTotalTime(27000)));
		COKE_OVEN.addRecipe(new CokeOvenRecipe(new Crafting(DummyCraftingHandler.INSTANCE).setRecipeKey("charcoal")//
				.setInput(new IngredientStack("logWood"))//
				.setOutput(new ItemStack(Items.COAL, 1, 1))//
				.setFluidOutput(new FluidStack(ModFluids.CREOSOTE_OIL, 500))//
				.setTotalTime(3000)));
		
		BLAST_FURNACE.addRecipe(new BlastFurnaceRecipe(new Crafting(DummyCraftingHandler.INSTANCE).setRecipeKey("steel_coal")//
				.setInput(new IngredientStack("ingotIron"), new IngredientStack(new ItemStack(Items.COAL, 4, OreDictionary.WILDCARD_VALUE)))//
				.setOutput(ModItems.STEEL.of(Shape.INGOT))//
				.setTotalTime(2560)));
		BLAST_FURNACE.addRecipe(new BlastFurnaceRecipe(new Crafting(DummyCraftingHandler.INSTANCE).setRecipeKey("steel_coke")//
				.setInput(new IngredientStack("ingotIron"), new IngredientStack("fuelCoke"))//
				.setOutput(ModItems.STEEL.of(Shape.INGOT))//
				.setTotalTime(2560)));
	}
}
