package com.tictim.railreborn.logic;

import javax.annotation.Nullable;

import com.tictim.railreborn.multiblock.Blueprint.TestResult;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public interface Logic<TE extends TileEntity> extends ITickable, ICapabilitySerializable<NBTTagCompound>{
	void validate(TE te, @Nullable TestResult multiblockTest);
	/**
	 * Breaking block, invalidating multiblock structure, etc.
	 */
	void invalidate(TE te, @Nullable TestResult multiblockTest);
}
