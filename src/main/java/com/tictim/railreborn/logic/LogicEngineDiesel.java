package com.tictim.railreborn.logic;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tictim.railreborn.RailReborn;
import com.tictim.railreborn.capability.Debugable;
import com.tictim.railreborn.client.gui.GuiSteamEngine;
import com.tictim.railreborn.fluid.FluidWrapper;
import com.tictim.railreborn.fluid.ModFluids;
import com.tictim.railreborn.inventory.ContainerSteamEngine;
import com.tictim.railreborn.inventory.Inventory;
import com.tictim.railreborn.inventory.InventoryBuilder;
import com.tictim.railreborn.inventory.InventoryBuilder.AccessValidator;
import com.tictim.railreborn.inventory.InventoryBuilder.FieldHandler;
import com.tictim.railreborn.inventory.InventoryBuilder.SidedItemHandlerFactory;
import com.tictim.railreborn.multiblock.Blueprint.TestResult;
import com.tictim.railreborn.recipe.Crafting;
import com.tictim.railreborn.recipe.Machine;
import com.tictim.railreborn.recipe.MachineRecipes;
import com.tictim.railreborn.util.NBTTypes;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.RangedWrapper;

import javax.annotation.Nullable;

public class LogicEngineDiesel extends LogicEngine implements InventoryBuilder, SidedItemHandlerFactory, FieldHandler{
	private final Inventory inv = this.createInventory();
	private final FluidTank tank = new FluidTank(16000);
	
	@Override
	public void update(){
		generateRJfromFluid(20, ModFluids.DIESEL);
	}

	@Override
	public FluidTank getTank() {
		return this.tank;
	}

	@Override
	public Inventory getInventory() {
		return this.inv;
	}

	@Override
	public NBTTagCompound serializeNBT(){
		NBTTagCompound nbt = super.serializeNBT();
		{
			NBTTagCompound subnbt = inv.serializeNBT();
			if(!subnbt.hasNoTags()) nbt.setTag("inventory", subnbt);
		}
		{
			NBTTagCompound subnbt = tank.writeToNBT(new NBTTagCompound());
			if(!subnbt.hasNoTags()) nbt.setTag("fluidTank", subnbt);
		}
		if(this.capacity>0) nbt.setLong("max", this.capacity);
		if(this.current>0) nbt.setLong("current", this.current);
		if(this.in>0) nbt.setLong("in", this.in);
		if(this.out>0) nbt.setLong("out", this.out);
		return nbt;
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound nbt){
		super.deserializeNBT(nbt);
		if(nbt.hasKey("inventory", NBTTypes.COMPOUND)) inv.deserializeNBT(nbt.getCompoundTag("inventory"));
		if(nbt.hasKey("fluidTank", NBTTypes.COMPOUND)) tank.readFromNBT(nbt.getCompoundTag("fluidTank"));
		this.capacity = nbt.getLong("max");
		this.current = nbt.getLong("current");
		this.in = nbt.getLong("in");
		this.out = nbt.getLong("out");
	}

	@Override
	public int size(){
		return 2;
	}
	
	@Override
	public String invName(){
		return RailReborn.MODID+".diesel_engine";
	}

	private IItemHandler inputSlotHandler, outputSlotHandler;

	@Override
	public boolean canInsertItem(IInventory inv, int index, ItemStack stack, EnumFacing direction){
		return index==0&&inv.isItemValidForSlot(index, stack);
	}
	
	@Override
	public boolean canExtractItem(IInventory inv, int index, ItemStack stack, EnumFacing direction){
		return index==1;
	}
	
	@Override
	public ContainerSteamEngine getContainer(TileEntity te, EntityPlayer player){
		return new ContainerSteamEngine(te, this, this.inv, player);
	}
	
	@Override
	public GuiContainer getGui(TileEntity te, EntityPlayer player){
		return new GuiSteamEngine(getContainer(te, player));
	}
	
	@Override
	public ITextComponent getDisplayName(){
		return this.inv.getDisplayName();
	}

	@Override
	public int getField(int id){
		switch(id){
			default:
				throw new IllegalArgumentException("id: "+id);
		}
	}
	
	@Override
	public void setField(int id, int value){
		switch(id){
			default:
				throw new IllegalArgumentException("id: "+id);
		}
	}
	
	@Override
	public int getFieldCount(){
		return 0;
	}
	
	@Override
	public boolean hasCapability(Capability<?> cap, EnumFacing facing){
		if(cap==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return true;
		else if(cap==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) return true;
		else return super.hasCapability(cap, facing);
	}
	
	@Nullable
	private IFluidHandler tankCapability;
	
	@Override
	@Nullable
	@SuppressWarnings("unchecked")
	public <T> T getCapability(Capability<T> cap, EnumFacing facing){
		if(cap==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
			return (T)this.inv.create(facing);
		}else if(cap==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY){
			if(tankCapability==null) tankCapability = new FluidWrapper(tank, false, true);
			return (T)tankCapability;
		}else return super.getCapability(cap, facing);
	}

	@Override
	public long capacityRJ(){
		return 2000;
	}

	@Override
	public long maxRJIn(){
		return 30;
	}

	@Override
	public long maxRJOut(){
		return 30;
	}
}
