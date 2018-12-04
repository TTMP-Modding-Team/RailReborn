package com.tictim.railreborn.item;

import java.util.List;
import javax.annotation.Nullable;

import com.tictim.railreborn.item.Tooltip.TooltipFixed;
import com.tictim.railreborn.item.Tooltip.TooltipSimple;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBlockBase extends ItemBlock{
	@Nullable
	private Tooltip tooltip;
	
	public ItemBlockBase(Block block){
		super(block);
	}
	
	public ItemBlock setTooltip(int lines){
		return setTooltip(new TooltipSimple(lines));
	}
	
	public ItemBlock setTooltip(String... tooltips){
		return setTooltip(new TooltipFixed(tooltips));
	}
	
	public ItemBlock setTooltip(Tooltip tooltip){
		this.tooltip = tooltip;
		return this;
	}
	
	@Override
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced){
		if(this.tooltip!=null) this.tooltip.addInformation(stack, player, tooltip, advanced);
	}
}
