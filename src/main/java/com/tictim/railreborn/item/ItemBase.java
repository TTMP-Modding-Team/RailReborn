package com.tictim.railreborn.item;

import java.util.List;
import javax.annotation.Nullable;

import com.tictim.railreborn.item.Tooltip.TooltipFixed;
import com.tictim.railreborn.item.Tooltip.TooltipSimple;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBase extends Item{
	@Nullable
	private Tooltip tooltip;
	
	public Item setTooltip(int lines){
		return setTooltip(new TooltipSimple(lines));
	}
	
	public Item setTooltip(String... tooltips){
		return setTooltip(new TooltipFixed(tooltips));
	}
	
	public Item setTooltip(Tooltip tooltip){
		this.tooltip = tooltip;
		return this;
	}
	
	@Override
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced){
		if(this.tooltip!=null) this.tooltip.addInformation(stack, player, tooltip, advanced);
	}
}
