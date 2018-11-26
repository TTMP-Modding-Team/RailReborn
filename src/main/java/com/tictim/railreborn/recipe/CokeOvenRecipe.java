package com.tictim.railreborn.recipe;

public class CokeOvenRecipe extends MachineRecipeImpl{
	public CokeOvenRecipe(Crafting c){
		super(c);
		c.expectInputSize(1);
		c.expectOutputSize(1);
		c.expectFluidInputSize(0);
		c.expectFluidOutputSize(0, 1);
	}
}
