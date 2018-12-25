package com.tictim.railreborn.network;

import com.tictim.railreborn.client.event.FoolsGoggleEvent;
import com.tictim.railreborn.pipelink.PipeLink;
import com.tictim.railreborn.pipelink.PipeNode;
import com.tictim.railreborn.pipelink.WorldPipeLink;
import com.tictim.railreborn.pipelink.handler.PipeHandler;
import com.tictim.railreborn.util.DataUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class MessagePipeData implements IMessage{
	public BlockPos pos;
	public PipeLink link;
	
	public MessagePipeData(){}
	
	public MessagePipeData(BlockPos pos, PipeLink link){
		this.pos = pos;
		this.link = link;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void fromBytes(ByteBuf buf){
		pos = DataUtils.readBlockPos(buf);
		(link = new PipeLink(WPLCache.INSTANCE)).deserializeNBT(ByteBufUtils.readTag(buf));
	}
	
	@Override
	public void toBytes(ByteBuf buf){
		DataUtils.writeBlockPos(buf, pos);
		ByteBufUtils.writeTag(buf, link.serializeNBT());
	}
	
	public static class Handler implements IMessageHandler<MessagePipeData, IMessage>{
		@Override
		@SideOnly(Side.CLIENT)
		public IMessage onMessage(MessagePipeData message, MessageContext ctx){
			FoolsGoggleEvent.update(message);
			return null;
		}
	}
	
	@SideOnly(Side.CLIENT)
	public enum WPLCache implements WorldPipeLink{
		INSTANCE;
		
		@Override
		public World getWorld(){
			return Minecraft.getMinecraft().world;
		}
		
		@Override
		public void markDirty(){}
		
		@Override
		@Nullable
		public PipeNode getNode(PipeHandler handler, BlockPos pos){
			return null;
		}
		
		@Override
		public PipeNode assign(PipeHandler handler, BlockPos pos){
			return null;
		}
		
		@Override
		public void remove(PipeLink link){}
	}
}
