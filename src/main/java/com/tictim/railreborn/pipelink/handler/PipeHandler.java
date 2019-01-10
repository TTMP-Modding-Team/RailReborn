package com.tictim.railreborn.pipelink.handler;

import com.tictim.railreborn.client.render.PipeRenderer;
import com.tictim.railreborn.client.render.PipeRendererTransparent;
import com.tictim.railreborn.enums.Diameter;
import com.tictim.railreborn.item.ModItems;
import com.tictim.railreborn.pipelink.attachment.PipeAttachment;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;

public abstract class PipeHandler extends IForgeRegistryEntry.Impl<PipeHandler>{
	private int color = 0xFFFFFF;
	private Diameter diameter = Diameter.D20;
	
	private Material blockMaterial = Material.IRON;
	private SoundType blockSound = SoundType.METAL;
	
	public Diameter getDiameter(){
		return diameter;
	}
	
	public PipeHandler setDiameter(Diameter diameter){
		this.diameter = diameter;
		return this;
	}
	
	public Material getBlockMaterial(){
		return this.blockMaterial;
	}
	
	public SoundType getBlockSound(){
		return blockSound;
	}
	
	public PipeHandler setBlockMaterial(Material blockMaterial){
		this.blockMaterial = blockMaterial;
		return this;
	}
	
	public PipeHandler setBlockSound(SoundType blockSound){
		this.blockSound = blockSound;
		return this;
	}
	
	public PipeHandler setColor(int color){
		this.color = color&0xFFFFFF;
		return this;
	}
	
	public int getColor(){
		return color;
	}
	
	@SideOnly(Side.CLIENT)
	private PipeRenderer pipeRenderer;
	
	@SideOnly(Side.CLIENT)
	public PipeRenderer getPipeRenderer(){
		if(pipeRenderer==null) pipeRenderer = createPipeRenderer();
		return pipeRenderer;
	}
	
	@SideOnly(Side.CLIENT)
	protected PipeRenderer createPipeRenderer(){
		return new PipeRendererTransparent(this);
	}
	
	@Nullable
	public abstract PipeAttachment createDefaultPipeAttachment(World world, BlockPos target, EnumFacing facing);
	public abstract PipeAttachment deserializeDefaultPipeAttachment(NBTTagCompound nbt);
	
	// TODO
	public void update(World w){}
	
	private ItemStack of;
	
	public ItemStack of(){
		if(of==null){
			of = new ItemStack(ModItems.PIPES);
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("pipe", getRegistryName().toString());
			of.setTagCompound(nbt);
		}
		return of;
	}
}
