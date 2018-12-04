package com.tictim.railreborn.block;

import com.tictim.railreborn.RailReborn;
import com.tictim.railreborn.enums.Metals;
import com.tictim.railreborn.enums.Multibricks;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber(modid = RailReborn.MODID)
public final class ModBlocks{
	private ModBlocks(){}
	
	public static final CreativeTabs TAB_BLOCKS = new CreativeTabs("railReborn.blocks"){
		@Override
		public ItemStack getTabIconItem(){
			return new ItemStack(MULTIBRICK_CORE);
		}
	};
	
	public static final Block COAL_COKE_BLOCK = new Block(Material.ROCK).setRegistryName("coal_coke_block").setUnlocalizedName("railReborn.coal_coke_block").setHardness(3).setResistance(5).setCreativeTab(TAB_BLOCKS);
	public static final Block ORE_TIN = new Block(Material.ROCK).setRegistryName("ore_tin").setUnlocalizedName("railReborn.tin_ore").setHardness(3).setResistance(5).setCreativeTab(TAB_BLOCKS);
	public static final Block ORE_CHROME = new Block(Material.ROCK).setRegistryName("ore_chrome").setUnlocalizedName("railReborn.chrome_ore").setHardness(5).setResistance(10).setCreativeTab(TAB_BLOCKS);
	public static final BlockVar<Metals> METAL_BLOCKS = (BlockVar<Metals>)BlockVar.create(Material.IRON, SoundType.METAL, Metals.class).setRegistryName("metal_block").setUnlocalizedName("railReborn.block").setHardness(5).setResistance(10).setCreativeTab(TAB_BLOCKS);
	public static final BlockVar<Multibricks> BRICKS = (BlockVar<Multibricks>)BlockVar.create(Material.ROCK, SoundType.STONE, BlockMultibrickCore.PROPERTY).setRegistryName("brick").setUnlocalizedName("railReborn.brick").setHardness(3).setResistance(5).setCreativeTab(TAB_BLOCKS);
	public static final Block MULTIBRICK_CORE = new BlockMultibrickCore().setRegistryName("multibrick_core").setUnlocalizedName("railReborn.core").setHardness(3).setResistance(5).setCreativeTab(TAB_BLOCKS);
	public static final BlockVar<Multibricks> MULTIBRICK_PART = (BlockVar<Multibricks>)new BlockMultibrickPart().setRegistryName("multibrick_part").setUnlocalizedName("railReborn.part").setHardness(3).setResistance(5).setCreativeTab(TAB_BLOCKS);
	
	static{
		COAL_COKE_BLOCK.setHarvestLevel("pickaxe", 0);
		ORE_TIN.setHarvestLevel("pickaxe", 1);
		ORE_CHROME.setHarvestLevel("pickaxe", 3);
		METAL_BLOCKS.setHarvestLevel("pickaxe", 1);
		BRICKS.setHarvestLevel("pickaxe", 0);
		MULTIBRICK_CORE.setHarvestLevel("pickaxe", 0);
		MULTIBRICK_PART.setHarvestLevel("pickaxe", 0);
	}
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event){
		IForgeRegistry<Block> registry = event.getRegistry();
		registry.register(COAL_COKE_BLOCK);
		registry.register(ORE_TIN);
		registry.register(ORE_CHROME);
		registry.register(METAL_BLOCKS);
		registry.register(BRICKS);
		registry.register(MULTIBRICK_CORE);
		registry.register(MULTIBRICK_PART);
	}
}
