package com.tictim.railreborn.recipe;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.item.ItemStack;

public class MachineRecipeList implements MachineRecipe{
	private final List<MachineRecipe> recipes = new ArrayList<>();
	
	public void addRecipe(MachineRecipe e){
		recipes.add(e);
	}
	
	@Override
	public @Nullable Crafting getCrafting(Machine m){
		for(int i = 0; i<recipes.size(); i++){
			MachineRecipe e = recipes.get(i);
			Crafting c = e.getCrafting(m);
			if(c!=null) return c;
		}
		return null;
	}

	@Override
	public @Nullable Crafting getCrafting(ItemStack input){
		for(int i = 0; i<recipes.size(); i++){
			MachineRecipe e = recipes.get(i);
			Crafting c = e.getCrafting(input);
			if(c!=null) return c;
		}
		return null;
	}
}
