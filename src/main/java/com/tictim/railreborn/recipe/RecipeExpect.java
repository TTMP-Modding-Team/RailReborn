package com.tictim.railreborn.recipe;

public final class RecipeExpect{
	/**
	 * [i] [j]<br>
	 * i:0 = Input<Br>
	 * i:1 = Output<Br>
	 * i:2 = Fluid Input<Br>
	 * i:3 = Fluid Output<br>
	 * <br>
	 * [j]<br>
	 * j:0 = no expected values<br>
	 * j:1 = exact value<br>
	 * j:2 = ranged values([0] ~ [1])
	 */
	private final int[][] expectedIOs = new int[4][0];
	
	public RecipeExpect copy(RecipeExpect expect){
		for(int i = 0; i<4; i++) expectedIOs[i] = expect.expectedIOs[i];
		return this;
	}
	
	public RecipeExpect setInputSize(int value){
		expectedIOs[0] = new int[]{value};
		return this;
	}
	
	public RecipeExpect setInputSize(int min, int max){
		expectedIOs[0] = new int[]{min, max};
		return this;
	}
	
	public RecipeExpect setOutputSize(int value){
		expectedIOs[1] = new int[]{value};
		return this;
	}
	
	public RecipeExpect setOutputSize(int min, int max){
		expectedIOs[1] = new int[]{min, max};
		return this;
	}
	
	public RecipeExpect setFluidInputSize(int value){
		expectedIOs[2] = new int[]{value};
		return this;
	}
	
	public RecipeExpect setFluidInputSize(int min, int max){
		expectedIOs[2] = new int[]{min, max};
		return this;
	}
	
	public RecipeExpect setFluidOutputSize(int value){
		expectedIOs[3] = new int[]{value};
		return this;
	}
	
	public RecipeExpect setFluidOutputSize(int min, int max){
		expectedIOs[3] = new int[]{min, max};
		return this;
	}
	
	public void expect(Crafting c){
		{
			int[] arr = expectedIOs[0];
			switch(arr.length){
				case 1:
					c.expectInputSize(arr[0]);
					break;
				case 2:
					c.expectInputSize(arr[0], arr[1]);
					break;
				//default:
			}
		}
		{
			int[] arr = expectedIOs[1];
			switch(arr.length){
				case 1:
					c.expectOutputSize(arr[0]);
					break;
				case 2:
					c.expectOutputSize(arr[0], arr[1]);
					break;
				//default:
			}
		}
		{
			int[] arr = expectedIOs[2];
			switch(arr.length){
				case 1:
					c.expectFluidInputSize(arr[0]);
					break;
				case 2:
					c.expectFluidOutputSize(arr[0], arr[1]);
					break;
				//default:
			}
		}
		{
			int[] arr = expectedIOs[3];
			switch(arr.length){
				case 1:
					c.expectFluidOutputSize(arr[0]);
					break;
				case 2:
					c.expectFluidOutputSize(arr[0], arr[1]);
					break;
				//default:
			}
		}
	}
}
