package com.tictim.railreborn.inventory;

import com.tictim.railreborn.logic.Logic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;

public class ContainerCokeOven extends ContainerLogic<TileEntity>{
	public ContainerCokeOven(TileEntity te, Logic logic, IInventory inventory, EntityPlayer player){
		super(te, logic, inventory, player);
		this.addSlotToContainer(new SlotInv(inventory, 0, 26, 26));
		this.addSlotToContainer(new SlotInv(inventory, 1, 94, 26).lockInput());
		this.addPlayerInventory(8, 86, true);
		this.addDefaultTransferAction(1, 0, 1);
		this.addCraftTransferAction(1, 1, 1);
	}
}
