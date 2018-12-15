package com.tictim.railreborn.tileentity;

import com.tictim.railreborn.enums.Engines;
import com.tictim.railreborn.logic.Logic;
import net.minecraft.util.ITickable;

import javax.annotation.Nullable;

public class TEEngine extends TELogic implements ITickable{
	@Nullable
	private Engines engine;
	
	public Engines getEngine(){
		return this.engine;
	}
	
	public TEEngine setEngine(Engines value){
		this.engine = value;
		this.resetLogic();
		return this;
	}
	
	@Nullable
	@Override
	protected Logic createNewLogic(){
		return this.engine==null ? null : engine.createLogic();
	}
	
	@Override
	public void update(){
		if(!this.world.isRemote){
			if(logic!=null){
				if(!logic.isValid()) logic.validate(this, null);
				logic.update();
			}
		}
	}
}
