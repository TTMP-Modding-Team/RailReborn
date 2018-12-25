package com.tictim.railreborn.item;

import com.tictim.railreborn.enums.Misc;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.tictim.railreborn.item.ModItems.mrl;

public class ItemMisc extends Item{
	public ItemMisc(){
		this.setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack){
		Misc misc = Misc.fromMeta(stack.getItemDamage());
		return super.getUnlocalizedName(stack)+"."+misc;
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items){
		if(this.isInCreativeTab(tab)) for(int i = 0, j = Misc.values().length; i<j; i++)
			items.add(new ItemStack(this, 1, i));
	}
	
	@SideOnly(Side.CLIENT)
	public void registerModels(){
		for(int i = 0, j = Misc.values().length; i<j; i++)
			ModelLoader.setCustomModelResourceLocation(this, i, mrl("misc/"+Misc.fromMeta(i).toString()));
	}
}
