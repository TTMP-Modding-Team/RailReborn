package com.tictim.railreborn.item;

import static com.tictim.railreborn.item.ModItems.mrl;

import com.tictim.railreborn.block.BlockVar;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockVar extends ItemBlockBase{
	private final BlockVar<?> blockVar;
	
	public ItemBlockVar(BlockVar<?> block){
		super(block);
		this.blockVar = block;
		this.setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack){
		return super.getUnlocalizedName(stack)+"."+blockVar.fromMeta(stack.getItemDamage()).getName();
	}
	
	@Override
	public int getMetadata(int damage){
		return damage;
	}
	
	@SideOnly(Side.CLIENT)
	public void registerModels(){
		String p = getRegistryName().getResourcePath();
		for(int i = 0, j = blockVar.getProperty().getAllowedValues().size(); i<j; i++)
			ModelLoader.setCustomModelResourceLocation(this, i, mrl(p+"_"+blockVar.fromMeta(i).getName()));
	}
}
