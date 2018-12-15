package com.tictim.railreborn.item;

import static com.tictim.railreborn.item.ModItems.mrl;

import com.tictim.railreborn.enums.Multibricks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockMultibrickCore extends ItemBlockBase{
	public ItemBlockMultibrickCore(Block block){
		super(block);
		this.setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack){
		return super.getUnlocalizedName(stack)+"."+Multibricks.fromMeta(stack.getMetadata()).getName();
	}
	
	@Override
	public int getMetadata(int damage){
		return damage;
	}
	
	@SideOnly(Side.CLIENT)
	public void registerModels(){
		String p = getRegistryName().getResourcePath();
		for(int i = 0; i<2; i++)
			ModelLoader.setCustomModelResourceLocation(this, i, mrl(p+"_"+Multibricks.fromMeta(i).getName()));
	}
}
