package com.tictim.railreborn.tileentity;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.tictim.railreborn.block.BlockMultibrickCore;
import com.tictim.railreborn.capability.Debugable;
import com.tictim.railreborn.client.tesr.TESRMultiblockDebug.MultiblockDebugable;
import com.tictim.railreborn.enums.Multibricks;
import com.tictim.railreborn.logic.Logic;
import com.tictim.railreborn.multiblock.Blueprint.TestResult;
import com.tictim.railreborn.recipe.Crafting;
import com.tictim.railreborn.util.NBTTypes;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class TEMultibrick extends TileEntity implements ITickable, MultiblockDebugable, Debugable{
	@Nullable
	private Logic<TEMultibrick> logic;
	@Nullable
	private Multibricks multibrick;
	
	private int tick;
	private boolean valid;
	
	public Multibricks getMultibrick(){
		return this.multibrick;
	}
	
	public TileEntity setMultibrick(Multibricks value){
		this.multibrick = value;
		if(logic!=null) this.logic.invalidate(this, null);
		this.logic = value.createLogic();
		return this;
	}
	
	public void onBreak(){
		if(logic!=null) this.logic.invalidate(this, null);
	}
	
	public boolean isLogicValid(){
		return logic!=null&&valid;
	}
	
	public Container getContainer(EntityPlayer player, Multibricks assure){
		if(multibrick!=assure) throw new IllegalStateException("Wrong Multibrick");
		return logic.getContainer(this, player);
	}
	
	@SideOnly(Side.CLIENT)
	public GuiContainer getGui(EntityPlayer player, Multibricks assure){
		if(multibrick!=assure) throw new IllegalStateException("Wrong Multibrick");
		return logic.getGui(this, player);
	}
	
	@Override
	public void update(){
		if(!this.world.isRemote&&logic!=null){
			if((++tick) >= 10){
				tick = 0;
				TestResult r = multibrick.getBlueprint().test(world, pos);
				if(valid!=r.isValid()){
					if(valid = r.isValid()) logic.validate(this, r);
					else logic.invalidate(this, r);
				}
			}
			if(valid) logic.update();
		}
	}
	
	@Override
	@Nullable
	public TestResult getMultiblockTest(){
		return multibrick!=null ? multibrick.getBlueprint().test(world, pos) : null;
	}
	
	@Nullable
	@Override
	public ITextComponent getDisplayName(){
		return logic==null ? null : logic.getInventory().getDisplayName();
	}
	
	public double getProgress(){
		if(logic==null) return 0;
		Crafting c = logic.getCrafting(0);
		return c==null ? 0 : c.getProgress();
	}
	
	@Override
	public JsonElement getDebugInfo(){
		JsonObject obj = new JsonObject();
		obj.addProperty("Multibrick Type", multibrick.getName());
		obj.add("Logic", logic==null ? JsonNull.INSTANCE : logic.getDebugInfo());
		return Debugable.stateClassType(this.getClass(), obj);
	}
	
	@Override
	public boolean hasCapability(Capability<?> cap, EnumFacing facing){
		if(cap==Debugable.CAP) return true;
		return logic!=null&&logic.hasCapability(cap, facing)||super.hasCapability(cap, facing);
	}
	
	@Override
	@Nullable
	public <T> T getCapability(Capability<T> cap, EnumFacing facing){
		if(cap==Debugable.CAP) return (T)this;
		T t = logic!=null ? logic.getCapability(cap, facing) : null;
		return t!=null ? t : super.getCapability(cap, facing);
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState){
		return oldState.getBlock()!=newState.getBlock()||oldState.getValue(BlockMultibrickCore.PROPERTY)!=newState.getValue(BlockMultibrickCore.PROPERTY);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		if(multibrick!=null){
			nbt.setInteger("multibrick", multibrick.ordinal());
			NBTTagCompound subnbt = logic.serializeNBT();
			if(!subnbt.hasNoTags()) nbt.setTag("logic", subnbt);
		}
		if(!valid) nbt.setBoolean("invalid", true);
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		if(nbt.hasKey("multibrick", NBTTypes.INTEGER)){
			setMultibrick(Multibricks.fromMeta(nbt.getInteger("multibrick")));
			logic.deserializeNBT(nbt.getCompoundTag("logic"));
		}
		this.valid = !nbt.getBoolean("invalid");
	}
	
}
