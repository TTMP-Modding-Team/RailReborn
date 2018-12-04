package com.tictim.railreborn.multiblock;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableSet;
import com.tictim.railreborn.RailReborn;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;

public class Floor{
	private final MutableBlockPos mpos = new MutableBlockPos();
	
	private final BlockPredicate[][] predicate;
	private final boolean[][][] groups;
	
	Floor(List<String> tiles, Map<Character, BlockPredicate> replacements, List<String> groups){
		this(tiles, replacements, toPredicate(tiles, replacements), groups);
	}
	
	private Floor(List<String> tiles, Map<Character, BlockPredicate> replacements, BlockPredicate[][] predicate, List<String> groups){
		this.predicate = predicate;
		this.groups = new boolean[groups.size()+1][][];
		final int strlen = tiles.get(0).length();
		{
			boolean[][] g0 = new boolean[predicate.length][predicate[0].length];
			for(int z = 0; z<tiles.size(); z++){
				for(int x = 0; x<strlen; x++)
					g0[z][x] = predicate[z][x]!=BlockSimplePredicate.ANY;
			}
			this.groups[0] = g0;
		}
		for(int i = 1; i<this.groups.length; i++){
			boolean[][] g = new boolean[predicate.length][predicate[0].length];
			String groupFilter = groups.get(i-1);
			for(int z = 0; z<tiles.size(); z++){
				for(int x = 0; x<strlen; x++)
					g[z][x] = groupFilter.indexOf(tiles.get(z).charAt(x))>-1;
			}
			this.groups[i] = g;
		}
	}
	
	private static BlockPredicate[][] toPredicate(List<String> tiles, Map<Character, BlockPredicate> replacements){
		final int strlen = tiles.get(0).length();
		char[][] cp = new char[tiles.size()][strlen];
		for(int z = 0; z<tiles.size(); z++){
			for(int x = 0; x<strlen; x++)
				cp[z][x] = tiles.get(z).charAt(x);
		}
		BlockPredicate[][] predicate = new BlockPredicate[tiles.size()][strlen];
		for(int z = 0; z<tiles.size(); z++){
			for(int x = 0; x<strlen; x++){
				char c = cp[z][x];
				predicate[z][x] = replacements.containsKey(c) ? replacements.get(c) : BlockSimplePredicate.ANY;
			}
		}
		return predicate;
	}
	
	public boolean test(TransformableBlockAccess world){
		for(int z = 0; z<predicate.length; z++){
			BlockPredicate[] p = predicate[z];
			for(int x = 0; x<p.length; x++){
				if(!p[x].matches(world, mpos.setPos(x, 0, z))){
					//RailReborn.LOGGER.info("No matches. World state: {}, Example: {}", world.getBlockState(mpos), p[x].example());
					return false;
				}
			}
		}
		return true;
	}
	
	public void collectGroup(TransformableBlockAccess world, ImmutableSet.Builder<BlockPos> l, int idx){
		boolean[][] group = groups[idx];
		for(int z = 0; z<group.length; z++){
			boolean[] b = group[z];
			for(int x = 0; x<b.length; x++){
				if(b[x]){
					mpos.setPos(x, 0, z);
					l.add(world.getTransformedCopy(mpos)); // &&predicate[z][x].matches(world, mpos.setPos(x, 0, z))
				}
			}
		}
	}
	
	void appendFloors(StringBuilder stb){
		for(int z = 0; z<predicate.length; z++){
			if(z>0) stb.append("\n");
			BlockPredicate[] p = predicate[z];
			for(int x = 0; x<p.length; x++){
				if(x>0) stb.append(" ");
				stb.append("[").append(p[x]).append("]");
			}
		}
	}
	
	void appendGroups(StringBuilder stb, int g){
		boolean[][] group = groups[g];
		boolean flag = true;
		for(int z = group.length-1; z >= 0; z--){
			if(flag) flag = false;
			else stb.append("\n");
			boolean[] b = group[z];
			for(int x = 0; x<b.length; x++){
				if(x>0) stb.append(" ");
				stb.append("[").append(b[x] ? "O" : "X").append("]");
			}
		}
	}
}
