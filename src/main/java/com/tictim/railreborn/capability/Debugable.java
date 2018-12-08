package com.tictim.railreborn.capability;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

import javax.annotation.Nullable;

public interface Debugable{
	@CapabilityInject(Debugable.class)
	Capability<Debugable> CAP = null;
	
	JsonElement getDebugInfo();
	
	static JsonArray debugInstances(Iterable<? extends Debugable> insts){
		JsonArray items = new JsonArray();
		for(Debugable d: insts){
			items.add(d.getDebugInfo());
		}
		return items;
	}
	
	static JsonArray debugInstances(Debugable... insts){
		JsonArray items = new JsonArray();
		for(Debugable d: insts){
			items.add(d.getDebugInfo());
		}
		return items;
	}
	
	static JsonArray debugItemStacks(IInventory inv){
		JsonArray items = new JsonArray();
		for(int i = 0, j = inv.getSizeInventory(); i<j; i++){
			items.add(debugItemStack(inv.getStackInSlot(i)));
		}
		return items;
	}
	
	static JsonArray debugItemStacks(Iterable<ItemStack> stacks){
		JsonArray items = new JsonArray();
		for(ItemStack stack: stacks){
			items.add(debugItemStack(stack));
		}
		return items;
	}
	
	static JsonArray debugItemStacks(ItemStack... stacks){
		JsonArray items = new JsonArray();
		for(ItemStack stack: stacks){
			items.add(debugItemStack(stack));
		}
		return items;
	}
	
	static JsonPrimitive debugItemStack(ItemStack stack){
		if(stack.isEmpty()) return new JsonPrimitive("Empty");
		else{
			Item i = stack.getItem();
			StringBuilder stb = new StringBuilder(stack.getItem().getRegistryName().toString());
			if(i.getHasSubtypes()||i.isDamageable()) stb.append("@").append(TextFormatting.GOLD).append(stack.getItemDamage()).append(TextFormatting.RESET);
			return new JsonPrimitive(stb.append(" * ").append(TextFormatting.GOLD).append(stack.getCount()).toString());
		}
	}
	
	static JsonArray debugFluidStacks(Iterable<FluidStack> fluids){
		JsonArray items = new JsonArray();
		for(FluidStack fluid: fluids){
			items.add(debugFluidStack(fluid));
		}
		return items;
	}
	
	static JsonArray debugFluidStacks(FluidStack... fluids){
		JsonArray items = new JsonArray();
		for(FluidStack fluid: fluids){
			items.add(debugFluidStack(fluid));
		}
		return items;
	}
	
	static JsonPrimitive debugFluidStack(@Nullable FluidStack fluid){
		return new JsonPrimitive(fluid==null||fluid.getFluid()==null||fluid.amount<=0 ? "Empty" : fluid.getFluid().getName()+" * "+TextFormatting.GOLD+fluid.amount);
	}
	
	static JsonPrimitive debugFluidTank(IFluidTank tank){
		FluidStack fluid = tank.getFluid();
		boolean isEmpty = fluid==null||fluid.getFluid()==null||fluid.amount<=0;
		String fluidStr = isEmpty ? "Empty" : fluid.getFluid().getName();
		return new JsonPrimitive(fluidStr+" ( "+TextFormatting.GOLD+(isEmpty ? 0 : fluid.amount)+TextFormatting.RESET+" / "+TextFormatting.GOLD+tank.getCapacity()+TextFormatting.RESET+" )");
	}
	
	static JsonArray cutOff(JsonArray arr, int maxLength){
		if(arr.size()>maxLength){
			int s = arr.size()-maxLength;
			for(int i = arr.size()-1; i >= maxLength; i--)
				arr.remove(i);
			arr.add("..."+TextFormatting.GOLD+s+TextFormatting.RESET+" more...");
		}
		return arr;
	}
	
	static JsonArray stateClassType(Class<?> classOf, JsonElement e){
		JsonArray arr = new JsonArray();
		arr.add(TextFormatting.BOLD+classOf.getSimpleName());
		arr.add(e);
		return arr;
	}
}
