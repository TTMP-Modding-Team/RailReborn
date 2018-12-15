package com.tictim.railreborn.tileentity;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.tictim.railreborn.capability.Debugable;
import com.tictim.railreborn.multiblock.Blueprint;
import com.tictim.railreborn.util.NBTTypes;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.Objects;

public class TEMultiblockPart extends TileEntity implements Debugable{
	@Nullable
	private BlockPos core;
	
	public void setCorePos(@Nullable BlockPos pos){
		if(!Objects.equals(pos, core)){
			this.core = pos;
			update(this);
		}
	}
	
	// TODO Code organization
	private static void update(TileEntity te){
		IBlockState state = te.getWorld().getBlockState(te.getPos());
		te.getWorld().notifyBlockUpdate(te.getPos(), state, state, 0);
	}
	
	@Nullable
	public BlockPos getCorePos(){
		return this.core;
	}
	
	@Nullable
	public TileEntity getCore(){
		if(this.core==null) return null;
		return world.getTileEntity(core);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt){
		this.handleUpdateTag(pkt.getNbtCompound());
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound nbt){
		if(nbt.hasKey("core", NBTTypes.COMPOUND)){
			NBTTagCompound subnbt = nbt.getCompoundTag("core");
			this.core = new BlockPos(subnbt.getInteger("x"), subnbt.getInteger("y"), subnbt.getInteger("z"));
		}else this.core = null;
	}
	
	@Override
	public NBTTagCompound getUpdateTag(){
		NBTTagCompound nbt = super.getUpdateTag();
		if(core!=null){
			NBTTagCompound subnbt = new NBTTagCompound();
			subnbt.setInteger("x", core.getX());
			subnbt.setInteger("y", core.getY());
			subnbt.setInteger("z", core.getZ());
			nbt.setTag("core", subnbt);
		}
		return nbt;
	}
	
	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket(){
		return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
	}
	
	@Override
	public JsonElement getDebugInfo(){
		JsonObject obj = new JsonObject();
		obj.add("Multiblock Core Position", core==null ? JsonNull.INSTANCE : new JsonPrimitive(Blueprint.posToStr(core)));
		if(core!=null){
			TileEntity core = getCore();
			obj.add("", core==null ? JsonNull.INSTANCE : Debugable.debugTileEntity(core));
		}
		return Debugable.stateClassType(this.getClass(), obj);
	}
	
	@Override
	public boolean hasCapability(Capability<?> cap, EnumFacing facing){
		if(cap==Debugable.CAP) return true;
		TileEntity core = getCore();
		return core!=null&&core.hasCapability(cap, facing)||super.hasCapability(cap, facing);
	}
	
	@Override
	@Nullable
	public <T> T getCapability(Capability<T> cap, EnumFacing facing){
		if(cap==Debugable.CAP) return (T)this;
		TileEntity core = getCore();
		T t = core!=null ? core.getCapability(cap, facing) : null;
		return t!=null ? t : super.getCapability(cap, facing);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		if(core!=null){
			NBTTagCompound subnbt = new NBTTagCompound();
			subnbt.setInteger("x", core.getX());
			subnbt.setInteger("y", core.getY());
			subnbt.setInteger("z", core.getZ());
			nbt.setTag("core", subnbt);
		}
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		if(nbt.hasKey("core", NBTTypes.COMPOUND)){
			NBTTagCompound subnbt = nbt.getCompoundTag("core");
			this.core = new BlockPos(subnbt.getInteger("x"), subnbt.getInteger("y"), subnbt.getInteger("z"));
		}else this.core = null;
	}
}
