package com.tictim.railreborn;

import com.tictim.railreborn.client.tesr.TESREngine;
import com.tictim.railreborn.tileentity.TEEngine;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientProxy extends CommonProxy{
	@Override
	public void registerTileEntity(){
		super.registerTileEntity();
		//ClientRegistry.bindTileEntitySpecialRenderer(TEMultibrick.class, new TESRMultiblockDebug<>());
	}

	@Override
	public void registerRenderer() {
		ClientRegistry.bindTileEntitySpecialRenderer(TEEngine.class, new TESREngine());
	}
}
