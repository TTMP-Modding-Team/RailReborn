package com.tictim.railreborn.recipe;

public class BlastFurnaceRecipe extends MachineRecipeImpl{
	public BlastFurnaceRecipe(Crafting c){
		super(c);
		c.expectInputSize(1, 2);
		c.expectOutputSize(1);
		c.expectFluidInputSize(0);
		c.expectFluidOutputSize(0);
	}
}
