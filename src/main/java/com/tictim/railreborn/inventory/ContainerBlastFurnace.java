package com.tictim.railreborn.inventory;

import com.tictim.railreborn.logic.Logic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;

public class ContainerBlastFurnace extends ContainerLogic<TileEntity>{
	public ContainerBlastFurnace(TileEntity te, Logic logic, IInventory inventory, EntityPlayer player){
		super(te, logic, inventory, player);
		this.addSlotToContainer(new SlotInv(inventory, 0, 35, 26));
		this.addSlotToContainer(new SlotInv(inventory, 1, 53, 26));
		this.addSlotToContainer(new SlotInv(inventory, 2, 121, 26).lockInput());
		this.addPlayerInventory(8, 86, true);
		this.addDefaultTransferAction(1, 0, 2);
		this.addCraftTransferAction(1, 2, 1);
	}
}
