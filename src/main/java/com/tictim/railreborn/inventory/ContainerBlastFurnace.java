package com.tictim.railreborn.inventory;

import com.tictim.railreborn.tileentity.TEMultibrick;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

public class ContainerBlastFurnace extends ContainerBase<TEMultibrick>{
	public ContainerBlastFurnace(TEMultibrick te, IInventory inventory, EntityPlayer player){
		super(te, inventory, player);
		this.addSlotToContainer(new SlotInv(inventory, 0, 35, 26));
		this.addSlotToContainer(new SlotInv(inventory, 1, 53, 26));
		this.addSlotToContainer(new SlotInv(inventory, 2, 121, 26).lockInput());
		this.addPlayerInventory(8, 86, true);
		this.addDefaultTransferAction(1, 0, 2);
		this.addCraftTransferAction(1, 2, 1);
	}
}
