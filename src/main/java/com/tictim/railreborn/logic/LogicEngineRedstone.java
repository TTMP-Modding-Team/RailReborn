package com.tictim.railreborn.logic;

import com.tictim.railreborn.RailReborn;
import com.tictim.railreborn.enums.Engines;
import com.tictim.railreborn.inventory.Inventory;
import com.tictim.railreborn.util.NBTTypes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;

public class LogicEngineRedstone extends LogicEngine{
	private final Inventory.Name name = new Inventory.Name(RailReborn.MODID+".engine.redstone_repeater", true);
	private boolean toggle;
	
	@Override
	protected Engines getEngineType(){
		return Engines.REDSTONE_REPEATER;
	}
	
	@Override
	protected int fuel(){
		return toggle ? 1 : 0;
	}
	
	@Override
	public ITextComponent getDisplayName(){
		return name.getDisplayName();
	}
	
	@Override
	public NBTTagCompound serializeNBT(){
		NBTTagCompound nbt = super.serializeNBT();
		if(toggle) nbt.setBoolean("toggle", true);
		{
			NBTTagCompound subnbt = name.serializeNBT();
			if(!subnbt.hasNoTags()) nbt.setTag("name", subnbt);
		}
		return nbt;
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound nbt){
		super.deserializeNBT(nbt);
		toggle = nbt.getBoolean("toggle");
		if(nbt.hasKey("name", NBTTypes.COMPOUND)){
			name.deserializeNBT(nbt.getCompoundTag("name"));
		}
	}
}
