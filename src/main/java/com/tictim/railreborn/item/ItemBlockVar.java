package com.tictim.railreborn.item;

import com.tictim.railreborn.block.BlockVar;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.function.BiConsumer;

import static com.tictim.railreborn.item.ModItems.mrl;

public class ItemBlockVar<E extends Enum<E>&IStringSerializable> extends ItemBlockBase{
	private final BlockVar<E> blockVar;
	
	public ItemBlockVar(BlockVar<E> block){
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
		registerModels(getRegistryName().getResourcePath());
	}
	
	@SideOnly(Side.CLIENT)
	public void registerModels(String prefix){
		for(int i = 0, j = blockVar.getProperty().getAllowedValues().size(); i<j; i++)
			ModelLoader.setCustomModelResourceLocation(this, i, mrl(prefix+"_"+blockVar.fromMeta(i).getName()));
	}
	
	public void forEachVariations(BiConsumer<E, ItemStack> consumer){
		for(int i = 0, j = blockVar.getProperty().getAllowedValues().size(); i<j; i++){
			consumer.accept(blockVar.fromMeta(i), new ItemStack(this, 1, i));
		}
	}
}
