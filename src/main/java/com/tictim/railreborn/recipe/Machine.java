package com.tictim.railreborn.recipe;

import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.EmptyFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.EmptyHandler;

/**
 * Interaction between machine and recipe(consume energy, boost speed etc)
 */
public interface Machine{
	boolean interact(Crafting c);
	boolean process(Crafting c);
	void collectResult(Crafting c);
	void finalize(Crafting c);
	
	default IItemHandler inputSlotHandler(){
		return EmptyHandler.INSTANCE;
	}
	
	default IItemHandler outputSlotHandler(){
		return EmptyHandler.INSTANCE;
	}
	
	default IFluidHandler inputFluidHandler(){
		return EmptyFluidHandler.INSTANCE;
	}
	
	default IFluidHandler outputFluidHandler(){
		return EmptyFluidHandler.INSTANCE;
	}
}
