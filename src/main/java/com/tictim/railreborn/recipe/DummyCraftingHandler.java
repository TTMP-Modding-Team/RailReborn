package com.tictim.railreborn.recipe;

public enum DummyCraftingHandler implements Machine{
	INSTANCE;
	
	@Override
	public boolean interact(Crafting c){
		return false;
	}
	
	@Override
	public boolean process(Crafting c){
		return false;
	}
	
	@Override
	public void collectResult(Crafting c){}
	
	@Override
	public void finalize(Crafting c){}
}
