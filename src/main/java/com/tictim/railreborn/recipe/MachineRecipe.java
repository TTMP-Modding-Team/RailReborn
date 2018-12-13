package com.tictim.railreborn.recipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

import javax.annotation.Nullable;
import java.util.regex.Pattern;

public interface MachineRecipe{
	/**
	 * 해당 {@link Machine}의 조건을 검사하여 작업 수행이 가능할 경우 {@link Crafting} 인스턴스를 생성하여 반환합니다.
	 * @param machine 조건 검사에 사용되는 {@link Machine} 인스턴스.
	 * @return 새 {@link Crafting} 인스턴스. 조건에 부합하는 Crafting 인스턴스가 없을 경우 null을 반환합니다.
	 */
	@Nullable
	Crafting getCrafting(Machine machine);
	/**
	 * 해당 {@link ItemStack}을 사용하는 {@link Crafting} 인스턴스를 생성하여 반환합니다.
	 * @param input 조건 검사에 사용되는 {@link ItemStack}.
	 * @return 새 {@link Crafting} 인스턴스. 조건에 부합하는 Crafting 인스턴스가 없을 경우 null을 반환합니다.
	 */
	@Nullable
	Crafting getCrafting(ItemStack input);
	/**
	 * 해당 {@link Fluid}을 사용하는 {@link Crafting} 인스턴스를 생성하여 반환합니다.
	 * @param fluid 조건 검사에 사용되는 {@link Fluid}.
	 * @return 새 {@link Crafting} 인스턴스. 조건에 부합하는 Crafting 인스턴스가 없을 경우 null을 반환합니다.
	 */
	@Nullable
	Crafting getCrafting(Fluid fluid);
	/**
	 * 해당 고유 키 값을 가지는 {@link Crafting} 인스턴스를 생성하여 반환합니다.
	 * @param key 고유 키 값.
	 * @return 새 {@link Crafting} 인스턴스. 조건에 부합하는 Crafting 인스턴스가 없을 경우 null을 반환합니다.
	 */
	@Nullable
	Crafting getCrafting(String key);
	
	String getKey();
	void expectCrafting(RecipeExpect expect);
	
	default void expectValidKey(){
		String key = getKey();
		if(key==null||!keyPattern().matcher(key).matches()) throw new IllegalArgumentException("Invalid recipe key: "+key);
	}
	
	default Pattern keyPattern(){
		return MachineRecipes.RECIPE_KEY_PATTERN;
	}
	
}
