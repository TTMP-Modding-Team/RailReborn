package com.tictim.railreborn.item;

import java.util.List;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

@FunctionalInterface
public interface Tooltip{
	void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced);
	
	public static class TooltipFixed implements Tooltip{
		private final String[] tooltips;
		
		public TooltipFixed(String... tooltips){
			this.tooltips = tooltips;
		}
		
		@Override
		public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced){
			for(String s : this.tooltips)
				tooltip.add(I18n.translateToLocal(s));
		}
	}
	
	public static class TooltipSimple implements Tooltip{
		private final int lines;
		
		public TooltipSimple(int lines){
			this.lines = lines;
		}
		
		@Override
		public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced){
			String base = stack.getUnlocalizedName();
			for(int i = 0; i<lines; i++){
				tooltip.add(I18n.translateToLocal(base+"."+i));
			}
		}
	}
}
