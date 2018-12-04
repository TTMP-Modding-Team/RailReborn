package com.tictim.railreborn.logic;

import javax.annotation.Nullable;

import com.tictim.railreborn.RailReborn;
import com.tictim.railreborn.inventory.Inventory;
import com.tictim.railreborn.inventory.InventoryBuilder;
import com.tictim.railreborn.inventory.InventoryBuilder.AccessValidator;
import com.tictim.railreborn.inventory.InventoryBuilder.SidedItemHandlerFactory;
import com.tictim.railreborn.multiblock.Blueprint.TestResult;
import com.tictim.railreborn.recipe.Crafting;
import com.tictim.railreborn.recipe.Machine;
import com.tictim.railreborn.recipe.ModRecipes;
import com.tictim.railreborn.tileentity.TEMultibrickPart;
import com.tictim.railreborn.util.NBTTypes;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.RangedWrapper;

public class LogicCokeOven implements Logic<TileEntity>, InventoryBuilder, Machine, SidedItemHandlerFactory, AccessValidator{
	private final Inventory inv = this.createInventory();
	private final FluidTank tank = new FluidTank(16000);
	
	@Nullable
	private Crafting crafting;
	
	public LogicCokeOven(){
		tank.setCanFill(false);
	}
	
	@Override
	public void update(){
		if(crafting!=null) crafting.update();
		else if(!inv.getStackInSlot(0).isEmpty()){
			crafting = ModRecipes.COKE_OVEN.getCrafting(this);
		}
	}
	
	@Override
	public void validate(TileEntity te, @Nullable TestResult multiblockTest){
		if(multiblockTest!=null) for(BlockPos pos: multiblockTest.getGroup(1)){
			TileEntity te2 = te.getWorld().getTileEntity(pos);
			if(te2 instanceof TEMultibrickPart) ((TEMultibrickPart)te2).setCorePos(te.getPos());
		}
		tank.setCanDrain(true);
	}
	
	@Override
	public void invalidate(TileEntity te, @Nullable TestResult multiblockTest){
		if(multiblockTest!=null) for(BlockPos pos: multiblockTest.getGroup(1)){
			TileEntity te2 = te.getWorld().getTileEntity(pos);
			if(te2 instanceof TEMultibrickPart) ((TEMultibrickPart)te2).setCorePos(null);
		}
		if(crafting!=null){
			crafting.cancel();
			crafting = null;
		}
		InventoryHelper.dropInventoryItems(te.getWorld(), te.getPos(), inv);
		tank.setCanDrain(false);
	}
	
	@Override
	public NBTTagCompound serializeNBT(){
		NBTTagCompound nbt = new NBTTagCompound();
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
		if(nbt.hasKey("inventory", NBTTypes.COMPOUND)) inv.deserializeNBT(nbt.getCompoundTag("inventory"));
		if(nbt.hasKey("fluidTank", NBTTypes.COMPOUND)) tank.readFromNBT(nbt.getCompoundTag("fluidTank"));
		crafting = nbt.hasKey("crafting", NBTTypes.COMPOUND) ? new Crafting(this).read(nbt.getCompoundTag("crafting")) : null;
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
		return index!=0||ModRecipes.COKE_OVEN.getCrafting(stack)!=null;
	}
	
	@Override
	public boolean hasCapability(Capability<?> cap, EnumFacing facing){
		if(cap==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return true;
		else if(cap==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) return true;
		else return true;
	}
	
	@Override
	@Nullable
	public <T> T getCapability(Capability<T> cap, EnumFacing facing){
		if(cap==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return (T)this.inv.create(facing);
		else if(cap==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) return (T)this.tank;
		else return null;
	}
}
