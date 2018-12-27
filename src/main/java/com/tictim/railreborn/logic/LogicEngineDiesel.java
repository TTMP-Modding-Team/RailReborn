package com.tictim.railreborn.logic;

import com.tictim.railreborn.RailReborn;
import com.tictim.railreborn.client.gui.GuiSteamEngine;
import com.tictim.railreborn.enums.Engines;
import com.tictim.railreborn.fluid.ModFluids;
import com.tictim.railreborn.fluid.TypedFluidTank;
import com.tictim.railreborn.inventory.ContainerEngineSteam;
import com.tictim.railreborn.inventory.EmptyInventory;
import com.tictim.railreborn.inventory.Inventory;
import com.tictim.railreborn.util.NBTTypes;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidTank;

public class LogicEngineDiesel extends LogicEngineFluid{
	private final Inventory.Name name = new Inventory.Name(RailReborn.MODID+".engine.diesel");
	
	@Override
	protected int fuelTime(){
		return 400;
	}
	
	@Override
	protected FluidTank createFluidTank(){
		return new TypedFluidTank(ModFluids.DIESEL, 16000);
	}
	
	@Override
	protected Engines getEngineType(){
		return Engines.DIESEL;
	}
	
	@Override
	public ContainerEngineSteam getContainer(TileEntity te, EntityPlayer player){
		return new ContainerEngineSteam(te, this, new EmptyInventory(name), player);
	}
	
	@Override
	public GuiContainer getGui(TileEntity te, EntityPlayer player){
		return new GuiSteamEngine(getContainer(te, player));
	}
	
	@Override
	public ITextComponent getDisplayName(){
		return name.getDisplayName();
	}
	
	@Override
	public NBTTagCompound serializeNBT(){
		NBTTagCompound nbt = super.serializeNBT();
		NBTTagCompound subnbt = name.serializeNBT();
		if(!subnbt.hasNoTags()) nbt.setTag("name", subnbt);
		return nbt;
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound nbt){
		super.deserializeNBT(nbt);
		if(nbt.hasKey("name", NBTTypes.COMPOUND)){
			name.deserializeNBT(nbt.getCompoundTag("name"));
		}
	}
}
