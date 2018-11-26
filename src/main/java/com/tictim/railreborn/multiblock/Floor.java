package com.tictim.railreborn.multiblock;

import java.util.List;
import java.util.Map;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;

public class Floor{
	private final MutableBlockPos mpos = new MutableBlockPos();
	
	private final BlockPredicate[][] predicate;
	private final boolean[][][] groups;
	
	Floor(List<String> tiles, Map<Character, BlockPredicate> replacements, List<String> groups){
		this(tiles, replacements, toPredicate(tiles, replacements), groups);
	}
	
	private Floor(
			List<String> tiles, Map<Character, BlockPredicate> replacements, BlockPredicate[][] predicate, List<String> groups
	){
		this.predicate = predicate;
		this.groups = new boolean[groups.size()+1][][];
		final int strlen = tiles.get(0).length();
		{
			boolean[][] g0 = new boolean[predicate.length][predicate[0].length];
			for(int y = 0; y<tiles.size(); y++){
				for(int x = 0; x<strlen; x++)
					g0[y][x] = predicate[y][x]==BlockSimplePredicate.ANY;
			}
			this.groups[0] = g0;
		}
		for(int i = 1; i<this.groups.length; i++){
			boolean[][] g = new boolean[predicate.length][predicate[0].length];
			String groupFilter = groups.get(i-1);
			for(int y = 0; y<tiles.size(); y++){
				for(int x = 0; x<strlen; x++)
					g[y][x] = groupFilter.indexOf(tiles.get(y).charAt(x))>-1;
			}
			this.groups[i] = g;
		}
	}
	
	private static BlockPredicate[][] toPredicate(List<String> tiles, Map<Character, BlockPredicate> replacements){
		final int strlen = tiles.get(0).length();
		char[][] cp = new char[tiles.size()][strlen];
		for(int y = 0; y<tiles.size(); y++){
			for(int x = 0; x<strlen; x++)
				cp[y][x] = tiles.get(y).charAt(x);
		}
		BlockPredicate[][] predicate = new BlockPredicate[tiles.size()][strlen];
		for(int y = 0; y<tiles.size(); y++){
			for(int x = 0; x<strlen; x++){
				char c = cp[y][x];
				predicate[y][x] = replacements.containsKey(c) ? replacements.get(c) : BlockSimplePredicate.ANY;
			}
		}
		return predicate;
	}
	
	public boolean test(TransformableBlockAccess world){
		for(int z = 0, zp = predicate.length; z<predicate.length; z++, zp--){
			BlockPredicate[] p = predicate[z];
			for(int x = 0; x<p.length; x++){
				if(!p[x].matches(world, mpos.setPos(x, 0, zp))) return false;
			}
		}
		return true;
	}
	
	public void collectGroup(TransformableBlockAccess world, List<BlockPos> l, int idx){
		boolean[][] group = groups[idx];
		for(int z = 0, zp = group.length; z<group.length; z++, zp--){
			boolean[] b = group[z];
			for(int x = 0; x<b.length; x++)
				if(b[x]&&predicate[z][x].matches(world, mpos.setPos(x, 0, zp))) l.add(world.getTransformedCopy(mpos));
		}
	}
}
