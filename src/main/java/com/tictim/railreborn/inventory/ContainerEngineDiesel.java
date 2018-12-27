package com.tictim.railreborn.inventory;

import com.tictim.railreborn.logic.Logic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;

public class ContainerEngineDiesel extends ContainerLogic<TileEntity>{
	public ContainerEngineDiesel(TileEntity te, Logic logic, IInventory inventory, EntityPlayer player){
		super(te, logic, inventory, player);
		this.addPlayerInventory(8, 86, true);
	}
}
