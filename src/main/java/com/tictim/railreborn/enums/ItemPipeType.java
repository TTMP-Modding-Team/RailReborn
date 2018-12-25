package com.tictim.railreborn.enums;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public enum ItemPipeType{
	STONE_40CM(PipeIngr.STONE, 40),
	STONE_60CM(PipeIngr.STONE, 60),
	TREATED_40CM(PipeIngr.TREATED_WOOD, 40),
	TREATED_60CM(PipeIngr.TREATED_WOOD, 60),
	IRON_40CM(PipeIngr.IRON, 40),
	IRON_60CM(PipeIngr.IRON, 60),
	IRON_80CM(PipeIngr.IRON, 80),
	STEEL_40CM(PipeIngr.STEEL, 40),
	STEEL_60CM(PipeIngr.STEEL, 60),
	STEEL_80CM(PipeIngr.STEEL, 80),
	STAINLESS_40CM(PipeIngr.STAINLESS_STEEL, 40),
	STAINLESS_60CM(PipeIngr.STAINLESS_STEEL, 60),
	STAINLESS_80CM(PipeIngr.STAINLESS_STEEL, 80);
	
	private final PipeIngr ingredient;
	private final int diameter;
	
	ItemPipeType(PipeIngr i, int diameter){
		this.ingredient = i;
		this.diameter = diameter;
	}
	
	public PipeIngr getIngredient(){
		return ingredient;
	}
	
	public int getDiameter(){
		return diameter;
	}
	
	public int getTransferSize(){
		return diameter*diameter;
	}
	
	public enum PipeIngr{
		STONE(Material.ROCK, SoundType.STONE, true, false, 0.9, 1.1, 1),
		TREATED_WOOD(Material.WOOD, SoundType.WOOD, false, true, 0.9, 1.1, 2),
		IRON(Material.IRON, SoundType.METAL, false, true, 0.7, 1.5, 4),
		STEEL(Material.IRON, SoundType.METAL, true, true, 0.4, 3, 6),
		STAINLESS_STEEL(Material.IRON, SoundType.METAL, true, true, 0, 6, 10);
		
		private final Material material;
		private final SoundType sound;
		private final boolean canCarryHotThing, canCarryFluid;
		private final double lowPressureThreshold, highPressureThreshold;
		/** blocks per second (m/s) */
		private final double momentumThreshold;
		
		PipeIngr(Material m, SoundType sound, boolean canCarryHotThing, boolean canCarryFluid, double lowPressureThreshold, double highPressureThreshold, double momentumThreshold){
			this.material = m;
			this.sound = sound;
			this.canCarryHotThing = canCarryHotThing;
			this.canCarryFluid = canCarryFluid;
			this.lowPressureThreshold = lowPressureThreshold;
			this.highPressureThreshold = highPressureThreshold;
			this.momentumThreshold = momentumThreshold;
		}
		
		public Material getItemMaterial(){
			return material;
		}
		
		public SoundType getItemSound(){
			return sound;
		}
		
		public boolean canCarryHotThing(){
			return canCarryHotThing;
		}
		
		public boolean canCarryFluid(){
			return canCarryFluid;
		}
		
		public double getLowPressureThreshold(){
			return lowPressureThreshold;
		}
		
		public double getHighPressureThreshold(){
			return highPressureThreshold;
		}
		
		public boolean isExceededPressureThreshold(double pressure){
			return pressure<getLowPressureThreshold()||pressure>getHighPressureThreshold();
		}
		
		/** blocks per second (m/s) */
		public double getMomentumThreshold(){
			return momentumThreshold;
		}
	}
}
