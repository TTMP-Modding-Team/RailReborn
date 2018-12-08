package com.tictim.railreborn.inventory;

import com.tictim.railreborn.tileentity.TEMultibrick;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

public class ContainerCokeOven extends ContainerBase<TEMultibrick>{
	public ContainerCokeOven(TEMultibrick te, IInventory inventory, EntityPlayer player){
		super(te, inventory, player);
		this.addSlotToContainer(new SlotInv(inventory, 0, 26, 26));
		this.addSlotToContainer(new SlotInv(inventory, 1, 94, 26).lockInput());
		this.addPlayerInventory(8, 86, true);
		this.addDefaultTransferAction(1, 0, 1);
		this.addCraftTransferAction(1, 1, 1);
	}
}
