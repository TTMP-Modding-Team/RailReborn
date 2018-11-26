package com.tictim.railreborn.item;

import com.tictim.railreborn.RailReborn;
import com.tictim.railreborn.block.ModBlocks;
import com.tictim.railreborn.config.RailRebornCfg;
import com.tictim.railreborn.enums.Misc;
import com.tictim.railreborn.enums.Shape;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
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
			return STEEL.of(Shape.GEAR);
		}
	};
	
	public static final Item MISC = new ItemMisc().setRegistryName("misc").setUnlocalizedName("misc").setCreativeTab(TAB_ITEMS);
	public static final ItemRefinedMetal IRON = (ItemRefinedMetal)new ItemRefinedMetal().setShapes(Shape.DUST, Shape.PLATE, Shape.GEAR).setRegistryName("iron").setUnlocalizedName("railReborn.iron").setCreativeTab(TAB_ITEMS);
	public static final ItemRefinedMetal GOLD = (ItemRefinedMetal)new ItemRefinedMetal().setShapes(Shape.DUST, Shape.PLATE, Shape.GEAR).setRegistryName("gold").setUnlocalizedName("railReborn.gold").setCreativeTab(TAB_ITEMS);
	public static final ItemRefinedMetal STEEL = (ItemRefinedMetal)new ItemRefinedMetal().setRegistryName("steel").setUnlocalizedName("railReborn.steel").setCreativeTab(TAB_ITEMS);
	public static final ItemRefinedMetal STAINLESS_STEEL = (ItemRefinedMetal)new ItemRefinedMetal().setRegistryName("stainless_steel").setUnlocalizedName("railReborn.stainless_steel").setCreativeTab(TAB_ITEMS);
	public static final ItemRefinedMetal CHROME = (ItemRefinedMetal)new ItemRefinedMetal().setShapes(Shape.INGOT, Shape.NUGGET, Shape.DUST).setRegistryName("chrome").setUnlocalizedName("railReborn.chrome").setCreativeTab(TAB_ITEMS);
	
	public static final Item CROWBAR_IRON = new ItemCrowbar(6, 400, "ingotIron").setRegistryName("crowbar_iron").setUnlocalizedName("crowbar_iron").setCreativeTab(TAB_ITEMS);
	public static final Item CROWBAR_STEEL = new ItemCrowbar(9, 800, "ingotSteel").setRegistryName("crowbar_steel").setUnlocalizedName("crowbar_steel").setCreativeTab(TAB_ITEMS);
	public static final Item CROWBAR_STAINLESS = new ItemCrowbar(8, 1200, Shape.INGOT.oreName(RailRebornCfg.getStainlessName())).setRegistryName("crowbar_stainless").setUnlocalizedName("crowbar_stainless").setCreativeTab(TAB_ITEMS);
	
	public static final Item COAL_COKE_BLOCK = new ItemBlock(ModBlocks.COAL_COKE_BLOCK).setRegistryName("coal_coke_block");
	public static final Item ORE_TIN = new ItemBlock(ModBlocks.ORE_TIN).setRegistryName("ore_tin");
	public static final Item ORE_CHROME = new ItemBlock(ModBlocks.ORE_CHROME).setRegistryName("ore_chrome");
	public static final ItemBlockVar METAL_BLOCKS = (ItemBlockVar)new ItemBlockVar(ModBlocks.METAL_BLOCKS).setRegistryName("metal_block");
	public static final ItemBlockVar BRICKS = (ItemBlockVar)new ItemBlockVar(ModBlocks.BRICKS).setTooltip("tooltip.safe_for_decoration", "tooltip.not_multiblock_part").setRegistryName("brick");
	public static final ItemBlockMultibrickCore MULTIBRICK_CORE = new ItemBlockMultibrickCore(ModBlocks.MULTIBRICK_CORE);
	public static final ItemBlockVar MULTIBRICK_PART = (ItemBlockVar)new ItemBlockVar(ModBlocks.MULTIBRICK_PART).setTooltip("tooltip.prevents_spawn").setRegistryName("multibrick_part");
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event){
		IForgeRegistry<Item> registry = event.getRegistry();
		registry.register(MISC);
		registry.register(IRON);
		registry.register(GOLD);
		registry.register(STEEL);
		registry.register(STAINLESS_STEEL);
		registry.register(CHROME);
		
		registry.register(CROWBAR_IRON);
		registry.register(CROWBAR_STEEL);
		registry.register(CROWBAR_STAINLESS);
		
		registry.register(COAL_COKE_BLOCK);
		registry.register(ORE_TIN);
		registry.register(ORE_CHROME);
		registry.register(METAL_BLOCKS);
		registry.register(BRICKS);
		registry.register(MULTIBRICK_CORE);
		registry.register(MULTIBRICK_PART);
		RailReborn.proxy.registerModels();
	}
	
	public static void registerOreDict(){
		for(Misc m : Misc.values()){
			String[] oreDict = m.getOreDict();
			if(oreDict.length>0){
				ItemStack of = m.of();
				for(String oreName : oreDict)
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
	
	@SideOnly(Side.CLIENT)
	public static void registerModels(){
		for(Misc m : Misc.values())
			ModelLoader.setCustomModelResourceLocation(MISC, m.ordinal(), mrl("misc/"+m.name().toLowerCase()));
		IRON.registerModels("iron");
		GOLD.registerModels("gold");
		STEEL.registerModels("steel");
		STAINLESS_STEEL.registerModels("stainless");
		CHROME.registerModels("chrome");
		
		ModelLoader.setCustomModelResourceLocation(CROWBAR_IRON, 0, mrl(CROWBAR_IRON.getRegistryName()));
		ModelLoader.setCustomModelResourceLocation(CROWBAR_STEEL, 0, mrl(CROWBAR_STEEL.getRegistryName()));
		ModelLoader.setCustomModelResourceLocation(CROWBAR_STAINLESS, 0, mrl(CROWBAR_STAINLESS.getRegistryName()));
		
		ModelLoader.setCustomModelResourceLocation(COAL_COKE_BLOCK, 0, mrl(COAL_COKE_BLOCK.getRegistryName()));
		ModelLoader.setCustomModelResourceLocation(ORE_TIN, 0, mrl(ORE_TIN.getRegistryName()));
		ModelLoader.setCustomModelResourceLocation(ORE_CHROME, 0, mrl(ORE_CHROME.getRegistryName()));
		METAL_BLOCKS.registerModels();
		BRICKS.registerModels();
		MULTIBRICK_CORE.registerModels();
		MULTIBRICK_PART.registerModels();
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
