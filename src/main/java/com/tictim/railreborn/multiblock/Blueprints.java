package com.tictim.railreborn.multiblock;

import com.tictim.railreborn.block.ModBlocks;

public final class Blueprints{
	private Blueprints(){}
	/*
	public static final Blueprint COKE_OVEN = new BlueprintBuilder().floor(0, "I D", " X ", "E A")//
			.setReplacement('I', ModBlocks.MULTIBRICK_PART.getDefaultState().withProperty(BlockCokeOvenCore.PROPERTY, Multibricks.COKE_OVEN))//
			.setReplacement('D', ModBlocks.MULTIBRICK_PART.getDefaultState().withProperty(BlockCokeOvenCore.PROPERTY, Multibricks.BLAST_FURNACE))//
			.setReplacement('E', ModBlocks.METAL_BLOCKS.withVar(Metals.CHROME))//
			.setReplacement('A', ModBlocks.METAL_BLOCKS.withVar(Metals.STAINLESS_STEEL))//
			.setReplacementAsCenter('X', ModBlocks.MULTIBRICK_CORE.getDefaultState().withProperty(BlockCokeOvenCore.PROPERTY, Multibricks.COKE_OVEN))//
			.addGroup("I")//
			.addGroup("D")//
			.addGroup("E")//
			.addGroup("A")//
			.build();
	*/
	public static final Blueprint COKE_OVEN = new BlueprintBuilder().floor(0, "XXX", "XXX", "XXX")//
			.floor(1, "XXX", "X X", "XCX")//
			.floor(2, "XXX", "XXX", "XXX")//
			.setReplacementAsCenter('C', ModBlocks.COKE_OVEN_CORE)//
			.setReplacement('X', ModBlocks.COKE_OVEN_PART)//
			.setReplacement(' ', BlockPredicate.AIR)//
			.addGroup("X")//
			.build();
	public static final Blueprint BLAST_FURNACE = new BlueprintBuilder()//
			.floor(0, "XXX", "XXX", "XCX")//
			.floor(1, "XXX", "X X", "XXX")//
			.floor(2, "XXX", "X X", "XXX")//
			.floor(3, "XXX", "XXX", "XXX")//
			.setReplacementAsCenter('C', ModBlocks.BLAST_FURNACE_CORE)//
			.setReplacement('X', ModBlocks.BLAST_FURNACE_PART)//
			.setReplacement(' ', BlockPredicate.AIR)//
			.addGroup("X")//
			.build();
	public static final Blueprint RAIN_TANK = new BlueprintBuilder()//
			.floor(0, "XXX", "XXX", "XXX")//
			.floor(1, "XXX", "X X", "XCX")//
			.floor(2, "XXX", "XXX", "XXX")//
			.setReplacementAsCenter('C', ModBlocks.RAIN_TANK_CORE)//
			.setReplacement('X', ModBlocks.RAIN_TANK_PART)//
			.setReplacement(' ', BlockPredicate.AIR)//
			.addGroup("X")//
			.build();
}
