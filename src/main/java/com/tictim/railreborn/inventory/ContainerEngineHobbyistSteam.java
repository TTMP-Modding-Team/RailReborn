package com.tictim.railreborn.inventory;

import com.tictim.railreborn.logic.Logic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;

public class ContainerEngineHobbyistSteam extends ContainerLogic<TileEntity>{
	public ContainerEngineHobbyistSteam(TileEntity te, Logic logic, IInventory inventory, EntityPlayer player){
		super(te, logic, inventory, player);
		this.addSlotToContainer(new SlotInv(inventory, 0, 26, 26));
		this.addPlayerInventory(8, 86, true);
		this.addDefaultTransferAction(1, 0, 1);
	}
}
