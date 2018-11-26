package com.tictim.railreborn.multiblock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.Validate;
import com.google.common.collect.ImmutableList;
import com.tictim.railreborn.RailReborn;
import net.minecraft.util.math.BlockPos;

public class BlueprintBuilder{
	private final ArrayList<LinkedList<String>> floors = new ArrayList<>();
	private final Map<Character, BlockPredicate> replacements = new HashMap<>();
	private final List<String> groups = new LinkedList<>();
	private char center = 0;
	private int expectedLength;
	private BlockPos offset = BlockPos.ORIGIN;
	
	public BlueprintBuilder floor(int floor, String... tiles){
		LinkedList<String> l = getNthFloor(floor);
		for(String row : tiles){
			lengthTest(row.length());
			l.add(row);
		}
		return this;
	}
	
	public BlueprintBuilder setReplacement(char c, Object obj){
		if(c==0) throw new IllegalArgumentException("0(char) is invalid replacement key");
		BlockPredicate r = BlockPredicate.of(obj);
		if(replacements.containsKey(c))
			RailReborn.LOGGER.warn("Duplicated Blueprint replacement '{}' with {}, skipping", c, r);
		replacements.put(c, r);
		return this;
	}
	
	public BlueprintBuilder setReplacementAsCenter(char c, Object obj){
		return setReplacement(c, obj).setCenter(c);
	}
	
	public BlueprintBuilder setCenter(char c){
		if(c==0) throw new IllegalArgumentException("0(char) is invalid center key");
		this.center = c;
		return this;
	}
	
	public BlueprintBuilder addGroup(String chars){
		Validate.notEmpty(chars);
		groups.add(chars);
		return this;
	}
	
	private void lengthTest(int len){
		if(expectedLength==0){
			if((expectedLength = len)>0) return;
		}else if(expectedLength==len) return;
		throw new IllegalArgumentException("Invalid X tile size - "+len);
	}
	
	private LinkedList<String> getNthFloor(int floor){
		if(floor<0) throw new IllegalArgumentException("Negative floor is not allowed");
		boolean exists = floors.size()>floor;
		LinkedList<String> l = exists ? floors.get(floor) : null;
		if(l==null){
			l = new LinkedList<>();
			if(exists) floors.set(floor, l);
			else{
				for(int i = floors.size(); i<floor; i++)
					floors.add(null);
				floors.add(l);
			}
		}
		return l;
	}
	
	public Blueprint build(){
		if(center==0) throw new IllegalArgumentException("0(char) is invalid center key");
		BlockPos centerPos = null;
		if(floors.size()==0) throw new IllegalArgumentException("Invalid Y tile size - 0");
		else{
			int expected = 0;
			for(LinkedList<?> floor : floors){
				if(floor!=null){
					if(expected==0){
						if((expected = floor.size())>0) continue;
					}else if(expected==floor.size()) continue;
					throw new IllegalArgumentException("Invalid Z tile size - "+floor.size());
				}
			}
			if(expected==0) throw new IllegalArgumentException("Empty blueprint");
			for(int y = 0; y<floors.size(); y++){
				LinkedList<String> l = floors.get(y);
				if(l!=null) for(int x = 0; x<expected; x++){
					String s = l.get(x);
					char[] c = s.toCharArray();
					for(int z = 0; z<c.length; z++){
						if(c[z]==center){
							if(centerPos==null) centerPos = new BlockPos(x, y, z);
							else throw new IllegalArgumentException("Duplicated center key");
						}
					}
				}
			}
		}
		Floor[] f = new Floor[floors.size()];
		for(int i = 0; i<floors.size(); i++){
			LinkedList<String> l = floors.get(i);
			if(l!=null) f[i] = (new Floor(ImmutableList.copyOf(l), replacements, groups));
		}
		return new Blueprint(f, centerPos, groups.size()+1);
	}
}
