package com.tictim.railreborn.recipe;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class MachineRecipeImpl implements MachineRecipe{
	private final Crafting c;
	
	public MachineRecipeImpl(Crafting c){
		this.c = c;
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
		ItemStackHandler h = new ItemStackHandler(1);
		h.setStackInSlot(0, input);
		return c.extractInput(h, true) ? c : null;
	}
}
