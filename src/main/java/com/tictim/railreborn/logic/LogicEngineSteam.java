package com.tictim.railreborn.logic;

import com.tictim.railreborn.RailReborn;
import com.tictim.railreborn.fluid.FluidWrapper;
import com.tictim.railreborn.fluid.ModFluids;
import com.tictim.railreborn.inventory.Inventory;
import com.tictim.railreborn.util.NBTTypes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

public class LogicEngineSteam extends LogicEngine{
	public FluidTank tank = new FluidTank(16000);

	@Override
	public void update(){
			if(!(this.tank.getFluidAmount() == 0) && !isRJFull()) {
				generateRJfromFluid(20, ModFluids.STEAM);
			}
	}
	
	@Override
	public NBTTagCompound serializeNBT(){
		NBTTagCompound nbt = super.serializeNBT();
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
		if(nbt.hasKey("fluidTank", NBTTypes.COMPOUND)) tank.readFromNBT(nbt.getCompoundTag("fluidTank"));
		this.capacity = nbt.getLong("max");
		this.current = nbt.getLong("current");
		this.in = nbt.getLong("in");
		this.out = nbt.getLong("out");
		}

	@Override
	public ITextComponent getDisplayName(){
		return new TextComponentTranslation(RailReborn.MODID+".steam_engine");
	}

	@Override
	public FluidTank getTank() {
		return this.tank;
	}

	@Override
	public Inventory getInventory() {
		return null;
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
		if(cap==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY){
			if(tankCapability==null) tankCapability = new FluidWrapper(tank, true, false);
			return (T)tankCapability;
		}else return super.getCapability(cap, facing);
	}

	@Override
	public long capacityRJ() {
		return 2000;
	}

	@Override
	public long maxRJIn(){
		return 300;
	}

	@Override
	public long maxRJOut(){
		return 300;
	}

}
