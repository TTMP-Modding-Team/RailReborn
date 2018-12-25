package com.tictim.railreborn.enums;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;

public enum Diameter{
	D40(40, 6/16., 6/16., 6/16., 10/16., 10/16., 10/16.),
	D60(60, 4/16., 4/16., 4/16., 12/16., 12/16., 12/16.),
	D80(80, 2/16., 2/16., 2/16., 14/16., 14/16., 14/16.);
	
	private final int diameter;
	private final AxisAlignedBB center;
	private final AxisAlignedBB[] faces = new AxisAlignedBB[6];
	
	Diameter(int diameter, double x, double y, double z, double x2, double y2, double z2){
		this.diameter = diameter;
		center = new AxisAlignedBB(x, y, z, x2, y2, z2);
		for(int i = 0; i<6; i++){
			EnumFacing f = EnumFacing.VALUES[i];
			switch(f.getAxis()){
				case X:
					faces[i] = f.getAxisDirection()==EnumFacing.AxisDirection.NEGATIVE ? new AxisAlignedBB(x, y, z, 0, y2, z2) : new AxisAlignedBB(1, y, z, x2, y2, z2);
					break;
				case Y:
					faces[i] = f.getAxisDirection()==EnumFacing.AxisDirection.NEGATIVE ? new AxisAlignedBB(x, y, z, x2, 0, z2) : new AxisAlignedBB(x, 1, z, x2, y2, z2);
					break;
				default: // case Z:
					faces[i] = f.getAxisDirection()==EnumFacing.AxisDirection.NEGATIVE ? new AxisAlignedBB(x, y, z, x2, y2, 0) : new AxisAlignedBB(x, y, 1, x2, y2, z2);
			}
		}
	}
	
	public int diameter(){
		return diameter;
	}
	
	public int transferSize(){
		return diameter*diameter;
	}
	
	public AxisAlignedBB getCenterBB(){
		return center;
	}
	
	public AxisAlignedBB getSidedBB(EnumFacing facing){
		return faces[facing.getIndex()];
	}
}
