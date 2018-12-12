package com.tictim.railreborn.inventory;

import com.tictim.railreborn.logic.Logic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;

public class ContainerLogic<TE extends TileEntity> extends ContainerBase<TE>{
	protected final Logic logic;
	
	/**
	 * @throws ClassCastException If {@code logic} is not {@link IInventory} instance
	 */
	protected ContainerLogic(TE te, Logic logic, EntityPlayer player){
		this(te, logic, (IInventory)logic, player);
	}
	
	protected ContainerLogic(TE te, Logic logic, IInventory inv, EntityPlayer player){
		super(te, inv, player);
		this.logic = logic;
	}
	
	public Logic getLogic(){
		return logic;
	}
}


