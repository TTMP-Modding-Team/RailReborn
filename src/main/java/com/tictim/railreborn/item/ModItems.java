package com.tictim.railreborn.item;

import com.tictim.railreborn.RailReborn;
import com.tictim.railreborn.block.ModBlocks;
import com.tictim.railreborn.config.RailRebornCfg;
import com.tictim.railreborn.enums.*;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber(modid = RailReborn.MODID)
public final class ModItems{
	private ModItems(){}
	
	public static final CreativeTabs TAB_ITEMS = new CreativeTabs("railReborn.items"){
		@Override
		public ItemStack getTabIconItem(){
			return new ItemStack(CROWBAR_IRON);
		}
	};
	
	public static final Item CROWBAR_IRON = new ItemCrowbar(6, 400, "ingotIron", EnumRarity.COMMON, EnumRarity.RARE);
	public static final Item CROWBAR_STEEL = new ItemCrowbar(9, 800, "ingotSteel", EnumRarity.UNCOMMON, EnumRarity.RARE);
	public static final Item CROWBAR_STAINLESS = new ItemCrowbar(8, 1200, Shape.INGOT.oreName(RailRebornCfg.getStainlessName()), EnumRarity.RARE, EnumRarity.EPIC);
	public static final Item CROWBAR_FOOL = new ItemFoolsCrowbar();
	
	public static final Item MISC = new ItemMisc();
	public static final ItemRefinedMetal IRON = new ItemRefinedMetal().setShapes(Shape.DUST, Shape.PLATE, Shape.GEAR);
	public static final ItemRefinedMetal GOLD = new ItemRefinedMetal().setShapes(Shape.DUST, Shape.PLATE, Shape.GEAR);
	public static final ItemRefinedMetal STEEL = new ItemRefinedMetal();
	public static final ItemRefinedMetal STAINLESS_STEEL = new ItemRefinedMetal();
	public static final ItemRefinedMetal CHROME = new ItemRefinedMetal().setShapes(Shape.INGOT, Shape.NUGGET, Shape.DUST);
	
	public static final Item COAL_COKE_BLOCK = new ItemBlock(ModBlocks.COAL_COKE_BLOCK);
	public static final Item ORE_TIN = new ItemBlock(ModBlocks.ORE_TIN);
	public static final Item ORE_CHROME = new ItemBlock(ModBlocks.ORE_CHROME);
	public static final ItemBlockVar<Metals> METAL_BLOCKS = new ItemBlockVar<>(ModBlocks.METAL_BLOCKS);
	public static final ItemBlockVar<Multibricks> BRICKS = new ItemBlockVar<>(ModBlocks.BRICKS);
	public static final ItemBlockVar<WoodType> TREATED_WOODS = new ItemBlockVar<>(ModBlocks.TREATED_WOODS);
	
	public static final ItemBlockBase COKE_OVEN_CORE = new ItemBlockBase(ModBlocks.COKE_OVEN_CORE);
	public static final ItemBlockBase BLAST_FURNACE_CORE = new ItemBlockBase(ModBlocks.BLAST_FURNACE_CORE);
	public static final ItemBlockBase RAIN_TANK_CORE = new ItemBlockBase(ModBlocks.RAIN_TANK_CORE);
	
	public static final ItemBlockBase COKE_OVEN_PART = new ItemBlockBase(ModBlocks.COKE_OVEN_PART);
	public static final ItemBlockBase BLAST_FURNACE_PART = new ItemBlockBase(ModBlocks.BLAST_FURNACE_PART);
	public static final ItemBlockBase RAIN_TANK_PART = new ItemBlockBase(ModBlocks.RAIN_TANK_PART);
	
	public static final ItemBlockBase ENGINE_REDSTONE_REPEATER = new ItemBlockBase(ModBlocks.ENGINE_REDSTONE_REPEATER);
	public static final ItemBlockBase ENGINE_HOBBYIST_STEAM = new ItemBlockBase(ModBlocks.ENGINE_HOBBYIST_STEAM);
	public static final ItemBlockBase ENGINE_STEAM = new ItemBlockBase(ModBlocks.ENGINE_STEAM);
	public static final ItemBlockBase ENGINE_DIESEL = new ItemBlockBase(ModBlocks.ENGINE_DIESEL);
	
	static{
		CROWBAR_IRON.setRegistryName("crowbar_iron").setUnlocalizedName("crowbar_iron").setCreativeTab(TAB_ITEMS);
		CROWBAR_STEEL.setRegistryName("crowbar_steel").setUnlocalizedName("crowbar_steel").setCreativeTab(TAB_ITEMS);
		CROWBAR_STAINLESS.setRegistryName("crowbar_stainless").setUnlocalizedName("crowbar_stainless").setCreativeTab(TAB_ITEMS);
		CROWBAR_FOOL.setRegistryName("crowbar_fool").setUnlocalizedName("crowbar_fool").setCreativeTab(TAB_ITEMS);
		
		MISC.setRegistryName("misc").setUnlocalizedName("misc").setCreativeTab(TAB_ITEMS);
		IRON.setRegistryName("iron").setUnlocalizedName("railReborn.iron").setCreativeTab(TAB_ITEMS);
		GOLD.setRegistryName("gold").setUnlocalizedName("railReborn.gold").setCreativeTab(TAB_ITEMS);
		STEEL.setRegistryName("steel").setUnlocalizedName("railReborn.steel").setCreativeTab(TAB_ITEMS);
		STAINLESS_STEEL.setRegistryName("stainless_steel").setUnlocalizedName("railReborn.stainless_steel").setCreativeTab(TAB_ITEMS);
		CHROME.setRegistryName("chrome").setUnlocalizedName("railReborn.chrome").setCreativeTab(TAB_ITEMS);
		
		COAL_COKE_BLOCK.setRegistryName("coal_coke_block");
		ORE_TIN.setRegistryName("ore_tin");
		ORE_CHROME.setRegistryName("ore_chrome");
		METAL_BLOCKS.setRegistryName("metal_block");
		BRICKS.setTooltip("tooltip.safe_for_decoration", "tooltip.not_multiblock_part").setRegistryName("brick");
		TREATED_WOODS.setRegistryName("treated_woods");
		
		COKE_OVEN_CORE.setTooltip("tooltip.prevents_spawn").setRegistryName("coke_oven_core");
		BLAST_FURNACE_CORE.setTooltip("tooltip.prevents_spawn").setRegistryName("blast_furnace_core");
		RAIN_TANK_CORE.setTooltip("tooltip.prevents_spawn").setRegistryName("rain_tank_core");
		
		COKE_OVEN_PART.setTooltip("tooltip.prevents_spawn").setRegistryName("coke_oven_part");
		BLAST_FURNACE_PART.setTooltip("tooltip.prevents_spawn").setRegistryName("blast_furnace_part");
		RAIN_TANK_PART.setTooltip("tooltip.prevents_spawn").setRegistryName("rain_tank_part");
		
		ENGINE_REDSTONE_REPEATER.setRegistryName("engine.redstone_repeater");
		ENGINE_HOBBYIST_STEAM.setRegistryName("engine.hobbyist_steam");
		ENGINE_STEAM.setRegistryName("engine.steam");
		ENGINE_DIESEL.setRegistryName("engine.diesel");
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event){
		IForgeRegistry<Item> registry = event.getRegistry();
		registry.register(CROWBAR_IRON);
		registry.register(CROWBAR_STEEL);
		registry.register(CROWBAR_STAINLESS);
		registry.register(CROWBAR_FOOL);
		
		registry.register(MISC);
		registry.register(IRON);
		registry.register(GOLD);
		registry.register(STEEL);
		registry.register(STAINLESS_STEEL);
		registry.register(CHROME);
		
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
	}
	
	public static void registerOreDict(){
		for(Misc m: Misc.values()){
			String[] oreDict = m.getOreDict();
			if(oreDict.length>0){
				ItemStack of = m.of();
				for(String oreName: oreDict)
					OreDictionary.registerOre(oreName, of);
			}
		}
		IRON.registerOre("iron");
		GOLD.registerOre("gold");
		STEEL.registerOre("steel");
		STAINLESS_STEEL.registerOre(RailRebornCfg.getStainlessName());
		CHROME.registerOre(RailRebornCfg.getChromeName(), RailRebornCfg.getChromiumName());
		
		OreDictionary.registerOre("blockCoke", COAL_COKE_BLOCK);
		OreDictionary.registerOre("oreTin", ORE_TIN);
		OreDictionary.registerOre(Shape.oreName("ore", RailRebornCfg.getChromeName()), ORE_CHROME);
		OreDictionary.registerOre(Shape.oreName("ore", RailRebornCfg.getChromiumName()), ORE_CHROME);
		// Metal Blocks
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void registerModels(ModelRegistryEvent event){
		registerByDefault(CROWBAR_IRON);
		registerByDefault(CROWBAR_STEEL);
		registerByDefault(CROWBAR_STAINLESS);
		registerByDefault(CROWBAR_FOOL);
		
		for(Misc m: Misc.values())
			ModelLoader.setCustomModelResourceLocation(MISC, m.ordinal(), mrl("misc/"+m.name().toLowerCase()));
		IRON.registerModels("iron");
		GOLD.registerModels("gold");
		STEEL.registerModels("steel");
		STAINLESS_STEEL.registerModels("stainless");
		CHROME.registerModels("chrome");
		
		registerByDefault(COAL_COKE_BLOCK);
		registerByDefault(ORE_TIN);
		registerByDefault(ORE_CHROME);
		METAL_BLOCKS.registerModels();
		BRICKS.registerModels();
		TREATED_WOODS.registerModels();
		
		registerByDefault(COKE_OVEN_CORE);
		registerByDefault(BLAST_FURNACE_CORE);
		registerByDefault(RAIN_TANK_CORE);
		
		registerByDefault(COKE_OVEN_PART);
		registerByDefault(BLAST_FURNACE_PART);
		registerByDefault(RAIN_TANK_PART);
		
		ModelLoader.setCustomModelResourceLocation(ENGINE_REDSTONE_REPEATER, 0, mrl("engine/redstone_repeater"));
		ModelLoader.setCustomModelResourceLocation(ENGINE_HOBBYIST_STEAM, 0, mrl("engine/hobbyist_steam"));
		ModelLoader.setCustomModelResourceLocation(ENGINE_STEAM, 0, mrl("engine/steam"));
		ModelLoader.setCustomModelResourceLocation(ENGINE_DIESEL, 0, mrl("engine/diesel"));
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerByDefault(Item item){
		ModelLoader.setCustomModelResourceLocation(item, 0, mrl(item.getRegistryName()));
	}
	
	@SideOnly(Side.CLIENT)
	public static ModelResourceLocation mrl(String name){
		return mrl(new ResourceLocation(RailReborn.MODID, name));
	}
	
	@SideOnly(Side.CLIENT)
	public static ModelResourceLocation mrl(String modid, String name){
		return mrl(new ResourceLocation(modid, name));
	}
	
	@SideOnly(Side.CLIENT)
	public static ModelResourceLocation mrl(ResourceLocation rl){
		return new ModelResourceLocation(rl, "inventory");
	}
}
