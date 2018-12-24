package com.tictim.railreborn.logic;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tictim.railreborn.RailReborn;
import com.tictim.railreborn.api.RJ;
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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.RangedWrapper;

import javax.annotation.Nullable;

public class LogicSteamEngine extends Logic implements InventoryBuilder, Machine, SidedItemHandlerFactory, AccessValidator, FieldHandler, RJ{
	private final Inventory inv = this.createInventory();
	private final FluidTank tank = new FluidTank(16000);

	private long capacity = 100000, current = 0, in = 4000, out = 4000;
	private boolean infinite = false;


	@Nullable
	private Crafting crafting;
	
	@Override
	public void update(){
		if(crafting!=null) crafting.update();
			crafting = MachineRecipes.ENGINE.getCrafting(this);
			if(crafting!=null&&!crafting.extractInput(this.inputSlotHandler(), false)) crafting = null;
			if(!(this.tank.getFluidAmount() == 0)) {
				this.tank.setFluid(new FluidStack(ModFluids.STEAM, this.tank.getFluidAmount() - 1));
				this.insertRJ(20, true, true);
			}
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
		if(crafting!=null) nbt.setTag("crafting", crafting.serializeNBT());
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
		crafting = nbt.hasKey("crafting", NBTTypes.COMPOUND) ? new Crafting(this).read(nbt.getCompoundTag("crafting")) : null;
	}

	@Override
	public int size(){
		return 2;
	}
	
	@Override
	public String invName(){
		return RailReborn.MODID+".steam_engine";
	}

	@Override
	public boolean interact(Crafting c){
		return true;
	}

	@Override
	public boolean process(Crafting c){
		return true;
	}

	@Override
	public void collectResult(Crafting c){
		c.insertOutput(this.outputSlotHandler(), false);
		c.insertFluidOutput(this.outputFluidHandler(), false);
	}

	@Override
	public void finalize(Crafting c){
		crafting = null;
	}

	private IItemHandler inputSlotHandler, outputSlotHandler;

	@Override
	public IFluidHandler outputFluidHandler(){
		return tank;
	}
	
	@Override
	public boolean canInsertItem(IInventory inv, int index, ItemStack stack, EnumFacing direction){
		return index==0&&inv.isItemValidForSlot(index, stack);
	}
	
	@Override
	public boolean canExtractItem(IInventory inv, int index, ItemStack stack, EnumFacing direction){
		return index==1;
	}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack){
		return index!=0||MachineRecipes.ENGINE.getCrafting(stack)!=null;
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
	
	@Nullable
	@Override
	public Crafting getCrafting(int idx){
		return this.crafting;
	}
	
	@Override
	public int getField(int id){
		switch(id){
			case 0:
				return this.crafting==null ? 0 : this.crafting.getCurrentTime();
			case 1:
				return this.crafting==null ? 0 : this.crafting.getTotalTime();
			default:
				throw new IllegalArgumentException("id: "+id);
		}
	}
	
	@Override
	public void setField(int id, int value){
		switch(id){
			case 0:
				if(this.crafting==null) this.crafting = new Crafting(this);
				this.crafting.setCurrentTime(value);
				break;
			case 1:
				if(value<=0){
					this.crafting = null;
				}else{
					if(this.crafting==null) this.crafting = new Crafting(this);
					this.crafting.setTotalTime(value);
				}
				break;
			default:
				throw new IllegalArgumentException("id: "+id);
		}
	}
	
	@Override
	public int getFieldCount(){
		return 2;
	}
	
	@Override
	public JsonElement getDebugInfo(){
		JsonObject obj = new JsonObject();
		obj.add("Inventory", this.inv.getDebugInfo());
		obj.add("Fluid Tank", Debugable.debugFluidTank(this.tank));
		if(this.crafting!=null) obj.add("Crafting", this.crafting.getDebugInfo());
		return Debugable.stateClassType(this.getClass(), obj);
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
			if(tankCapability==null) tankCapability = new FluidWrapper(tank, true, false);
			return (T)tankCapability;
		}else return super.getCapability(cap, facing);
	}

	@Override
	public long capacityRJ(){
		return capacity;
	}

	@Override
	public long currentRJ(){
		return current;
	}

	@Override
	public long maxRJIn(){
		return in;
	}

	@Override
	public long maxRJOut(){
		return out;
	}

	@Override
	public long insertRJ(long amount, boolean internally, boolean simulate){
		if(infinite) return 0;
		else{
			long i = Math.min(capacityRJ()-currentRJ(), internally ? amount : Math.min(amount, maxRJIn()));
			if(!simulate) this.current += i;
			return i;
		}
	}

	@Override
	public long extractRJ(long amount, boolean internally, boolean simulate){
		if(infinite) return Long.MAX_VALUE;
		else{
			long i = Math.min(currentRJ(), internally ? amount : Math.min(amount, maxRJOut()));
			if(!simulate) this.current += i;
			return i;
		}
	}
}
