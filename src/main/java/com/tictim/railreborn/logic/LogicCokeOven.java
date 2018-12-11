package com.tictim.railreborn.logic;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tictim.railreborn.RailReborn;
import com.tictim.railreborn.capability.Debugable;
import com.tictim.railreborn.client.gui.GuiCokeOven;
import com.tictim.railreborn.inventory.ContainerCokeOven;
import com.tictim.railreborn.inventory.Inventory;
import com.tictim.railreborn.inventory.InventoryBuilder;
import com.tictim.railreborn.inventory.InventoryBuilder.AccessValidator;
import com.tictim.railreborn.inventory.InventoryBuilder.FieldHandler;
import com.tictim.railreborn.inventory.InventoryBuilder.SidedItemHandlerFactory;
import com.tictim.railreborn.multiblock.Blueprint.TestResult;
import com.tictim.railreborn.recipe.Crafting;
import com.tictim.railreborn.recipe.Machine;
import com.tictim.railreborn.recipe.MachineRecipes;
import com.tictim.railreborn.tileentity.TEMultibrick;
import com.tictim.railreborn.tileentity.TEMultibrickPart;
import com.tictim.railreborn.util.NBTTypes;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.RangedWrapper;

import javax.annotation.Nullable;

public class LogicCokeOven extends Logic<TEMultibrick> implements InventoryBuilder, Machine, SidedItemHandlerFactory, AccessValidator, FieldHandler{
	private final Inventory inv = this.createInventory();
	private final FluidTank tank = new FluidTank(16000);
	@Nullable
	private Crafting crafting;
	
	{
		resetTank();
	}
	
	@Override
	public void update(){
		if(crafting!=null) crafting.update();
		else if(!inv.getStackInSlot(0).isEmpty()){
			crafting = MachineRecipes.COKE_OVEN.getCrafting(this);
		}
	}
	
	@Override
	protected void onValidate(TEMultibrick te, @Nullable TestResult multiblockTest){
		if(multiblockTest!=null) for(BlockPos pos: multiblockTest.getGroup(1)){
			TileEntity te2 = te.getWorld().getTileEntity(pos);
			if(te2 instanceof TEMultibrickPart) ((TEMultibrickPart)te2).setCorePos(te.getPos());
		}
		resetTank();
	}
	
	@Override
	protected void onInvalidate(TEMultibrick te, @Nullable TestResult multiblockTest){
		if(multiblockTest!=null) for(BlockPos pos: multiblockTest.getGroup(1)){
			TileEntity te2 = te.getWorld().getTileEntity(pos);
			if(te2 instanceof TEMultibrickPart) ((TEMultibrickPart)te2).setCorePos(null);
		}
		if(crafting!=null){
			crafting.cancel();
			crafting = null;
		}
		InventoryHelper.dropInventoryItems(te.getWorld(), te.getPos(), inv);
		resetTank();
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
		return nbt;
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound nbt){
		super.deserializeNBT(nbt);
		if(nbt.hasKey("inventory", NBTTypes.COMPOUND)) inv.deserializeNBT(nbt.getCompoundTag("inventory"));
		if(nbt.hasKey("fluidTank", NBTTypes.COMPOUND)) tank.readFromNBT(nbt.getCompoundTag("fluidTank"));
		crafting = nbt.hasKey("crafting", NBTTypes.COMPOUND) ? new Crafting(this).read(nbt.getCompoundTag("crafting")) : null;
		resetTank();
	}
	
	private void resetTank(){
		tank.setCanFill(valid);
		tank.setCanDrain(valid);
	}
	
	@Override
	public int size(){
		return 2;
	}
	
	@Override
	public String invName(){
		return RailReborn.MODID+".coke_oven";
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
		if(c.extractInput(this.inputSlotHandler(), true)){
			c.extractInput(this.inputSlotHandler(), false);
			c.insertOutput(this.outputSlotHandler(), false);
			c.insertFluidOutput(this.outputFluidHandler(), false);
		}else c.cancel();
	}
	
	@Override
	public void finalize(Crafting c){
		crafting = null;
	}
	
	private IItemHandler inputSlotHandler, outputSlotHandler;
	
	@Override
	public IItemHandler inputSlotHandler(){
		if(inputSlotHandler==null) inputSlotHandler = new RangedWrapper(inv, 0, 1);
		return inputSlotHandler;
	}
	
	@Override
	public IItemHandler outputSlotHandler(){
		if(outputSlotHandler==null) outputSlotHandler = new RangedWrapper(inv, 1, 2);
		return outputSlotHandler;
	}
	
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
		return index!=0||MachineRecipes.COKE_OVEN.getCrafting(stack)!=null;
	}
	
	@Override
	public ContainerCokeOven getContainer(TEMultibrick te, EntityPlayer player){
		return new ContainerCokeOven(te, this.inv, player);
	}
	
	@Override
	public GuiContainer getGui(TEMultibrick te, EntityPlayer player){
		return new GuiCokeOven(getContainer(te, player));
	}
	
	@Override
	public Inventory getInventory(){
		return this.inv;
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
			if(tankCapability==null){
				tankCapability = new IFluidHandler(){
					@Override
					public IFluidTankProperties[] getTankProperties(){
						return tank.getTankProperties();
					}
					@Override
					public int fill(FluidStack resource, boolean doFill){
						return 0;
					}
					@Nullable
					@Override
					public FluidStack drain(FluidStack resource, boolean doDrain){
						return tank.drain(resource, doDrain);
					}
					@Nullable
					@Override
					public FluidStack drain(int maxDrain, boolean doDrain){
						return tank.drain(maxDrain, doDrain);
					}
				};
			}
			return (T)tankCapability;
		}else return super.getCapability(cap, facing);
	}
}
