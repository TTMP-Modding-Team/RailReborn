package com.tictim.railreborn.multiblock;

import com.tictim.railreborn.block.BlockMultibrickCore;
import com.tictim.railreborn.block.ModBlocks;
import com.tictim.railreborn.enums.Multibricks;

public final class Blueprints{
	private Blueprints(){}
	
	public static final Blueprint COKE_OVEN = new BlueprintBuilder().floor(0, "XXX", "XXX", "XXX")//
			.floor(1, "XXX", "X X", "XCX")//
			.floor(2, "XXX", "XXX", "XXX")//
			.setReplacementAsCenter('C', ModBlocks.MULTIBRICK_CORE.getDefaultState().withProperty(BlockMultibrickCore.PROPERTY, Multibricks.COKE_OVEN))//
			.setReplacement('X', ModBlocks.MULTIBRICK_PART.getDefaultState().withProperty(BlockMultibrickCore.PROPERTY, Multibricks.COKE_OVEN))//
			.setReplacement(' ', BlockPredicate.AIR)//
			.addGroup("X")//
			.build();
	public static final Blueprint BLAST_FURNACE = new BlueprintBuilder()//
			.floor(0, "XXX", "XXX", "XCX")//
			.floor(1, "XXX", "X X", "XXX")//
			.floor(2, "XXX", "X X", "XXX")//
			.floor(3, "XXX", "XXX", "XXX")//
			.setReplacementAsCenter('C', ModBlocks.MULTIBRICK_CORE.getDefaultState().withProperty(BlockMultibrickCore.PROPERTY, Multibricks.BLAST_FURNACE))//
			.setReplacement('X', ModBlocks.MULTIBRICK_PART.getDefaultState().withProperty(BlockMultibrickCore.PROPERTY, Multibricks.BLAST_FURNACE))//
			.setReplacement(' ', BlockPredicate.AIR)//
			.addGroup("X")//
			.build();
}
