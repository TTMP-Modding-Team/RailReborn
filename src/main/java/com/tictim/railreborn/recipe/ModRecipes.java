package com.tictim.railreborn.recipe;

import com.tictim.railreborn.enums.Misc;
import com.tictim.railreborn.enums.Shape;
import com.tictim.railreborn.fluid.ModFluids;
import com.tictim.railreborn.item.IngredientStack;
import com.tictim.railreborn.item.ModItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public final class ModRecipes{
	private ModRecipes(){}
	
	public static final MachineRecipeList COKE_OVEN = new MachineRecipeList();
	public static final MachineRecipeList BLAST_FURNACE = new MachineRecipeList();
	
	public static void addMachineRecipes(){
		COKE_OVEN.addRecipe(new CokeOvenRecipe(new Crafting(DummyCraftingHandler.INSTANCE)//
				.setInput(new IngredientStack(new ItemStack(Items.COAL)))//
				.setOutput(Misc.COKE.of())//
				.setFluidOutput(new FluidStack(ModFluids.CREOSOTE_OIL, 500))//
				.setTotalTime(3000)));
		COKE_OVEN.addRecipe(new CokeOvenRecipe(new Crafting(DummyCraftingHandler.INSTANCE)//
				.setInput(new IngredientStack("blockCoal"))//
				.setOutput(new ItemStack(ModItems.COAL_COKE_BLOCK))//
				.setFluidOutput(new FluidStack(ModFluids.CREOSOTE_OIL, 4500))//
				.setTotalTime(27000)));
		COKE_OVEN.addRecipe(new CokeOvenRecipe(new Crafting(DummyCraftingHandler.INSTANCE)//
				.setInput(new IngredientStack("logWood"))//
				.setOutput(new ItemStack(Items.COAL, 1, 1))//
				.setFluidOutput(new FluidStack(ModFluids.CREOSOTE_OIL, 500))//
				.setTotalTime(3000)));
		
		BLAST_FURNACE.addRecipe(new BlastFurnaceRecipe(new Crafting(DummyCraftingHandler.INSTANCE)//
				.setInput(new IngredientStack("ingotIron"), new IngredientStack(new ItemStack(Items.COAL, 4, OreDictionary.WILDCARD_VALUE)))//
				.setOutput(ModItems.STEEL.of(Shape.INGOT))//
				.setTotalTime(2560)));
		BLAST_FURNACE.addRecipe(new BlastFurnaceRecipe(new Crafting(DummyCraftingHandler.INSTANCE)//
				.setInput(new IngredientStack("ingotIron"), new IngredientStack("fuelCoke"))//
				.setOutput(ModItems.STEEL.of(Shape.INGOT))//
				.setTotalTime(2560)));
	}
}
