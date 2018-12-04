package com.tictim.railreborn.recipe;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;

public interface MachineRecipe{
	@Nullable
	Crafting getCrafting(Machine machine);
	@Nullable
	Crafting getCrafting(ItemStack input);
}
