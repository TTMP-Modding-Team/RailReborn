package com.tictim.railreborn.recipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

import javax.annotation.Nullable;

public class MachineRecipeImpl implements MachineRecipe{
	private final Crafting c;
	
	public MachineRecipeImpl(Crafting c){
		this.c = c;
		expectValidKey();
	}
	
	@Override
	@Nullable
	public Crafting getCrafting(Machine machine){
		if(!(c.extractInput(machine.inputSlotHandler(), true)&&c.extractFluidInput(machine.inputFluidHandler(), true)&&c.insertOutput(machine.outputSlotHandler(), true)&&c.insertFluidOutput(machine.outputFluidHandler(), true))) return null;
		return new Crafting(machine).copy(c);
	}
	
	@Override
	@Nullable
	public Crafting getCrafting(ItemStack input){
		return c.requiresLeastOne(input) ? new Crafting(c) : null;
	}
	
	@Override
	@Nullable
	public Crafting getCrafting(Fluid fluid){
		return c.requiresLeastOne(fluid) ? new Crafting(c) : null;
	}
	
	@Nullable
	@Override
	public Crafting getCrafting(String key){
		return key==null||!key.equals(c.getRecipeKey()) ? null : new Crafting(c);
	}
	
	@Override
	public String getKey(){
		return c.getRecipeKey();
	}
}
