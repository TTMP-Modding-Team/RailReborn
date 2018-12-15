package com.tictim.railreborn.block;

import com.tictim.railreborn.RailReborn;
import com.tictim.railreborn.enums.Engines;
import com.tictim.railreborn.enums.Metals;
import com.tictim.railreborn.enums.Multibricks;
import com.tictim.railreborn.enums.WoodType;
import com.tictim.railreborn.fluid.ModFluids;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber(modid = RailReborn.MODID)
public final class ModBlocks{
	private ModBlocks(){}
	
	public static final CreativeTabs TAB_BLOCKS = new CreativeTabs("railReborn.blocks"){
		@Override
		public ItemStack getTabIconItem(){
			return new ItemStack(COKE_OVEN_CORE);
		}
	};
	
	public static final Block COAL_COKE_BLOCK = new Block(Material.ROCK);
	public static final Block ORE_TIN = new Block(Material.ROCK);
	public static final Block ORE_CHROME = new Block(Material.ROCK);
	public static final BlockVar<Metals> METAL_BLOCKS = BlockVar.create(Material.IRON, SoundType.METAL, Metals.class);
	public static final BlockVar<Multibricks> BRICKS = BlockVar.create(Material.ROCK, SoundType.STONE, Multibricks.class);
	public static final BlockVar<WoodType> TREATED_WOODS = BlockVar.create(Material.WOOD, SoundType.WOOD, WoodType.class);
	
	public static final Block COKE_OVEN_CORE = new BlockCokeOvenCore();
	public static final Block BLAST_FURNACE_CORE = new BlockBlastFurnaceCore();
	public static final Block RAIN_TANK_CORE = new BlockRainTankCore();
	
	public static final Block COKE_OVEN_PART = new BlockMultiblockPart(Material.ROCK, SoundType.STONE);
	public static final Block BLAST_FURNACE_PART = new BlockMultiblockPart(Material.ROCK, SoundType.STONE);
	public static final Block RAIN_TANK_PART = new BlockMultiblockPart(Material.WOOD, SoundType.WOOD);
	
	public static final Block ENGINE_REDSTONE_REPEATER = new BlockEngine(Engines.REDSTONE_REPEATER);
	public static final Block ENGINE_HOBBYIST_STEAM = new BlockEngine(Engines.HOBBYIST_STEAM);
	public static final Block ENGINE_STEAM = new BlockEngine(Engines.STEAM);
	public static final Block ENGINE_DIESEL = new BlockEngine(Engines.DIESEL);
	
	public static final Block CREOSOTE_OIL = new BlockFluidClassic(ModFluids.CREOSOTE_OIL, Material.WATER);
	public static final Block OIL = new BlockFluidClassic(ModFluids.OIL, Material.WATER);
	public static final Block DIESEL = new BlockFluidClassic(ModFluids.DIESEL, Material.WATER);
	public static final Block STEAM = new BlockFluidClassic(ModFluids.STEAM, Material.LAVA);
	
	static{
		COAL_COKE_BLOCK.setRegistryName("coal_coke_block").setUnlocalizedName("railReborn.coal_coke_block").setHardness(3).setResistance(5).setCreativeTab(TAB_BLOCKS).setHarvestLevel("pickaxe", 0);
		ORE_TIN.setRegistryName("ore_tin").setUnlocalizedName("railReborn.tin_ore").setHardness(3).setResistance(5).setCreativeTab(TAB_BLOCKS).setHarvestLevel("pickaxe", 1);
		ORE_CHROME.setRegistryName("ore_chrome").setUnlocalizedName("railReborn.chrome_ore").setHardness(5).setResistance(10).setCreativeTab(TAB_BLOCKS).setHarvestLevel("pickaxe", 3);
		METAL_BLOCKS.setRegistryName("metal_block").setUnlocalizedName("railReborn.block").setHardness(5).setResistance(20).setCreativeTab(TAB_BLOCKS).setHarvestLevel("pickaxe", 1);
		BRICKS.setRegistryName("brick").setUnlocalizedName("railReborn.brick").setHardness(3).setResistance(10).setCreativeTab(TAB_BLOCKS).setHarvestLevel("pickaxe", 0);
		TREATED_WOODS.setRegistryName("treated_woods").setUnlocalizedName("treated_woods").setHardness(2).setResistance(5).setCreativeTab(TAB_BLOCKS);
		
		COKE_OVEN_CORE.setRegistryName("coke_oven_core").setUnlocalizedName("coke_oven_core").setHardness(3).setResistance(10).setCreativeTab(TAB_BLOCKS).setHarvestLevel("pickaxe", 0);
		BLAST_FURNACE_CORE.setRegistryName("blast_furnace_core").setUnlocalizedName("blast_furnace_core").setHardness(3).setResistance(10).setCreativeTab(TAB_BLOCKS).setHarvestLevel("pickaxe", 0);
		RAIN_TANK_CORE.setRegistryName("rain_tank_core").setUnlocalizedName("rain_tank_core").setHardness(2).setResistance(5).setCreativeTab(TAB_BLOCKS);
		
		COKE_OVEN_PART.setRegistryName("coke_oven_part").setUnlocalizedName("coke_oven_part").setHardness(3).setResistance(10).setCreativeTab(TAB_BLOCKS).setHarvestLevel("pickaxe", 0);
		BLAST_FURNACE_PART.setRegistryName("blast_furnace_part").setUnlocalizedName("blast_furnace_part").setHardness(3).setResistance(10).setCreativeTab(TAB_BLOCKS).setHarvestLevel("pickaxe", 0);
		RAIN_TANK_PART.setRegistryName("rain_tank_part").setUnlocalizedName("rain_tank_part").setHardness(2).setResistance(5).setCreativeTab(TAB_BLOCKS);
		
		ENGINE_REDSTONE_REPEATER.setRegistryName("engine.redstone_repeater").setUnlocalizedName("engine.redstone_repeater").setHardness(5).setResistance(20).setCreativeTab(TAB_BLOCKS).setHarvestLevel("pickaxe", 0);
		ENGINE_HOBBYIST_STEAM.setRegistryName("engine.hobbyist_steam").setUnlocalizedName("engine.hobbyist_steam").setHardness(5).setResistance(20).setCreativeTab(TAB_BLOCKS).setHarvestLevel("pickaxe", 0);
		ENGINE_STEAM.setRegistryName("engine.steam").setUnlocalizedName("engine.steam").setHardness(5).setResistance(20).setCreativeTab(TAB_BLOCKS).setHarvestLevel("pickaxe", 1);
		ENGINE_DIESEL.setRegistryName("engine.diesel").setUnlocalizedName("engine.diesel").setHardness(5).setResistance(20).setCreativeTab(TAB_BLOCKS).setHarvestLevel("pickaxe", 1);
		
		CREOSOTE_OIL.setRegistryName("creosote_oil");
		OIL.setRegistryName("oil");
		DIESEL.setRegistryName("diesel");
		STEAM.setRegistryName("steam");
	}
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event){
		IForgeRegistry<Block> registry = event.getRegistry();
		registry.register(COAL_COKE_BLOCK);
		registry.register(ORE_TIN);
		registry.register(ORE_CHROME);
		registry.register(METAL_BLOCKS);
		registry.register(BRICKS);
		registry.register(TREATED_WOODS);
		
		registry.register(COKE_OVEN_CORE);
		registry.register(BLAST_FURNACE_CORE);
		registry.register(RAIN_TANK_CORE);
		
		registry.register(COKE_OVEN_PART);
		registry.register(BLAST_FURNACE_PART);
		registry.register(RAIN_TANK_PART);
		
		registry.register(ENGINE_REDSTONE_REPEATER);
		registry.register(ENGINE_HOBBYIST_STEAM);
		registry.register(ENGINE_STEAM);
		registry.register(ENGINE_DIESEL);
		
		registry.register(CREOSOTE_OIL);
		registry.register(OIL);
		registry.register(DIESEL);
		registry.register(STEAM);
	}
}
