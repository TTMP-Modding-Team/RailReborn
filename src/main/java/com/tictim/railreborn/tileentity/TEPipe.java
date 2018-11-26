package com.tictim.railreborn.tileentity;

import com.tictim.railreborn.enums.PipeType;
import net.minecraft.tileentity.TileEntity;

public class TEPipe extends TileEntity{
	private PipeType pipe;
	
	public TileEntity setType(PipeType pipe){
		this.pipe = pipe;
		return this;
	}
	
}
