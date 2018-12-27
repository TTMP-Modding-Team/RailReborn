package com.tictim.railreborn.logic;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tictim.railreborn.RailReborn;
import com.tictim.railreborn.capability.Debugable;
import com.tictim.railreborn.client.gui.GuiEngineHobbyistSteam;
import com.tictim.railreborn.enums.Engines;
import com.tictim.railreborn.fluid.ModFluids;
import com.tictim.railreborn.fluid.TypedFluidTank;
import com.tictim.railreborn.inventory.ContainerEngineHobbyistSteam;
import com.tictim.railreborn.inventory.Inventory;
import com.tictim.railreborn.inventory.InventoryBuilder;
import com.tictim.railreborn.inventory.InventoryBuilder.AccessValidator;
import com.tictim.railreborn.inventory.InventoryBuilder.FieldHandler;
import com.tictim.railreborn.util.NBTTypes;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerFluidMap;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

public class LogicEngineHobbyistSteam extends LogicEngineFluid implements InventoryBuilder, AccessValidator, FieldHandler{
	private final Inventory inv = this.createInventory();
	private final FluidTank waterTank = new TypedFluidTank(FluidRegistry.WATER, 4000);
	
	{
		waterTank.setCanDrain(false);
	}
	
	@Override
	protected FluidTank createFluidTank(){
		return new TypedFluidTank(ModFluids.STEAM, 12000);
	}
	
	@Override
	protected int fuelTime(){
		return 100;
	}
	
	@Override
	protected Engines getEngineType(){
		return Engines.HOBBYIST_STEAM;
	}
	
	@Override
	public void update(){
		//TODO burn fuels, make steam
		super.update();
	}
	
	@Override
	public NBTTagCompound serializeNBT(){
		NBTTagCompound nbt = super.serializeNBT();
		{
			NBTTagCompound subnbt = inv.serializeNBT();
			if(!subnbt.hasNoTags()) nbt.setTag("inventory", subnbt);
		}
		{
			NBTTagCompound subnbt = waterTank.writeToNBT(new NBTTagCompound());
			if(!subnbt.hasNoTags()) nbt.setTag("waterTank", subnbt);
		}
		return nbt;
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound nbt){
		super.deserializeNBT(nbt);
		if(nbt.hasKey("inventory", NBTTypes.COMPOUND)) inv.deserializeNBT(nbt.getCompoundTag("inventory"));
		if(nbt.hasKey("waterTank", NBTTypes.COMPOUND)) waterTank.readFromNBT(nbt.getCompoundTag("waterTank"));
		
	}
	
	@Override
	public int size(){
		return 1;
	}
	
	@Override
	public String invName(){
		return RailReborn.MODID+".engine.hobbyist_steam";
	}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack){
		return TileEntityFurnace.isItemFuel(stack);
	}
	
	@Override
	public ContainerEngineHobbyistSteam getContainer(TileEntity te, EntityPlayer player){
		return new ContainerEngineHobbyistSteam(te, this, this.inv, player);
	}
	
	@Override
	public GuiContainer getGui(TileEntity te, EntityPlayer player){
		return new GuiEngineHobbyistSteam(getContainer(te, player));
	}
	
	@Override
	public ITextComponent getDisplayName(){
		return this.inv.getDisplayName();
	}
	
	@Override
	public int getField(int id){
		return 0;
	}
	
	@Override
	public void setField(int id, int value){
		// TODO temperature
	}
	
	@Override
	public int getFieldCount(){
		return 1;
	}
	
	@Override
	public JsonElement getDebugInfo(){
		JsonObject obj = new JsonObject();
		obj.add("Inventory", this.inv.getDebugInfo());
		obj.add("Fluid Tank", Debugable.debugFluidTank(this.tank));
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
			if(tankCapability==null) tankCapability = new FluidHandlerFluidMap().addHandler(FluidRegistry.WATER, waterTank).addHandler(ModFluids.STEAM, this.tank);
			return (T)tankCapability;
		}else return super.getCapability(cap, facing);
	}
}
