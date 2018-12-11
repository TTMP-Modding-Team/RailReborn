package com.tictim.railreborn.recipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MachineRecipeList implements MachineRecipe{
	public static final Pattern RECIPE_KEY_PATTERN = Pattern.compile("^[a-zA-Z_-]+$");
	
	private final Map<String, MachineRecipe> recipes = new HashMap<>();
	private final String key;
	
	public MachineRecipeList(String key){
		this.key = key;
		this.expectValidKey();
	}
	
	@Override
	public Pattern keyPattern(){
		return RECIPE_KEY_PATTERN;
	}
	
	public void addRecipe(MachineRecipe e){
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
			if(c!=null) return c.setRecipeKey(key+"."+c.getRecipeKey());
		}
		return null;
	}
	
	@Override
	@Nullable
	public Crafting getCrafting(ItemStack input){
		for(MachineRecipe e: recipes.values()){
			Crafting c = e.getCrafting(input);
			if(c!=null) return c.setRecipeKey(key+"."+c.getRecipeKey());
		}
		return null;
	}
	
	@Override
	@Nullable
	public Crafting getCrafting(Fluid fluid){
		for(MachineRecipe e: recipes.values()){
			Crafting c = e.getCrafting(fluid);
			if(c!=null) return c.setRecipeKey(key+"."+c.getRecipeKey());
		}
		return null;
	}
	
	@Nullable
	@Override
	public Crafting getCrafting(String key){
		Matcher m = RECIPE_KEY_PATTERN.matcher(key);
		if(m.matches()){
			MachineRecipe e = recipes.get(key);
			if(e!=null){
				Crafting c = e.getCrafting(key);
				if(c!=null) return c.setRecipeKey(this.key+"."+c.getRecipeKey());
			}
		}
		return null;
	}
	
	@Override
	public String getKey(){
		return this.key;
	}
}
