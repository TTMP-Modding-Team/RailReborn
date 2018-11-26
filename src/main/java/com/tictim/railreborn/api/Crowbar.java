package com.tictim.railreborn.api;

import net.minecraft.entity.EntityLivingBase;

public interface Crowbar{
	boolean canWork();
	void applyDamage(EntityLivingBase target);
}
