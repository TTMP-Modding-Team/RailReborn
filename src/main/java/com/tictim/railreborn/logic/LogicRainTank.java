package com.tictim.railreborn.logic;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tictim.railreborn.RailReborn;
import com.tictim.railreborn.capability.Debugable;
import com.tictim.railreborn.inventory.Inventory.Name;
import com.tictim.railreborn.multiblock.Blueprint;
import com.tictim.railreborn.tileentity.TEMultiblockPart;
import com.tictim.railreborn.util.NBTTypes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nullable;

public class LogicRainTank extends Logic{
	private final FluidTank tank = new FluidTank(8000){
		@Override
		public boolean canFillFluidType(FluidStack fluid){
			return super.canFillFluidType(fluid)&&fluid.getFluid()==FluidRegistry.WATER;
		}
		
		@Override
		public boolean canDrainFluidType(@Nullable FluidStack fluid){
			return super.canDrainFluidType(fluid)&&fluid.getFluid()==FluidRegistry.WATER;
		}
	};
	
	{
		resetTank();
	}
	
	@Override
	protected void onValidate(TileEntity te, @Nullable Blueprint.TestResult multiblockTest){
		if(multiblockTest!=null) for(BlockPos pos: multiblockTest.getGroup(1)){
			TileEntity te2 = te.getWorld().getTileEntity(pos);
			if(te2 instanceof TEMultiblockPart) ((TEMultiblockPart)te2).setCorePos(te.getPos());
		}
		resetTank();
	}
	
	@Override
	protected void onInvalidate(TileEntity te, @Nullable Blueprint.TestResult multiblockTest){
		if(multiblockTest!=null) for(BlockPos pos: multiblockTest.getGroup(1)){
			TileEntity te2 = te.getWorld().getTileEntity(pos);
			if(te2 instanceof TEMultiblockPart) ((TEMultiblockPart)te2).setCorePos(null);
		}
		tick = 0;
		resetTank();
	}
	
	private int tick;
	
	@Override
	public void update(){
		// TODO whatever
		if(++tick >= 40){
			tick = 0;
			tank.fill(new FluidStack(FluidRegistry.WATER, 10), true);
		}
	}
	
	@Override
	public NBTTagCompound serializeNBT(){
		NBTTagCompound nbt = super.serializeNBT();
		{
			NBTTagCompound subnbt = tank.writeToNBT(new NBTTagCompound());
			if(!subnbt.hasNoTags()) nbt.setTag("fluidTank", subnbt);
		}
		nbt.setInteger("tick", this.tick);
		return nbt;
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound nbt){
		super.deserializeNBT(nbt);
		if(nbt.hasKey("fluidTank", NBTTypes.COMPOUND)) tank.readFromNBT(nbt.getCompoundTag("fluidTank"));
		this.tick = nbt.getInteger("tick");
		resetTank();
	}
	
	private void resetTank(){
		tank.setCanFill(valid);
		tank.setCanDrain(valid);
	}
	
	private final Name name = new Name(RailReborn.MODID+".coke_oven");
	
	@Override
	public ITextComponent getDisplayName(){
		return this.name.getDisplayName();
	}
	
	@Override
	public JsonElement getDebugInfo(){
		JsonObject obj = new JsonObject();
		obj.add("Fluid Tank", Debugable.debugFluidTank(this.tank));
		return Debugable.stateClassType(this.getClass(), obj);
	}
	
	@Override
	public boolean hasCapability(Capability<?> cap, EnumFacing facing){
		if(cap==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) return true;
		else return super.hasCapability(cap, facing);
	}
	
	@Nullable
	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(Capability<T> cap, EnumFacing facing){
		if(cap==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY){
			return (T)tank;
		}else return super.getCapability(cap, facing);
	}
}
