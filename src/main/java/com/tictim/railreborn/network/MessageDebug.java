package com.tictim.railreborn.network;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.tictim.railreborn.capability.Debugable;
import com.tictim.railreborn.client.event.FoolsCrowbarEvent;
import com.tictim.railreborn.util.DataUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class MessageDebug implements IMessage{
	private static final Gson GSON = new GsonBuilder().create();
	
	public BlockPos pos;
	@Nullable
	public JsonElement e;
	
	public MessageDebug(){}
	
	public MessageDebug(BlockPos pos){
		this.pos = pos;
	}
	
	@Override
	public void fromBytes(ByteBuf buf){
		this.pos = DataUtils.readBlockPos(buf);
		byte[] b = new byte[buf.readInt()];
		if(b.length>0){
			buf.readBytes(b);
			e = GSON.fromJson(new String(b), JsonElement.class);
		}
	}
	
	@Override
	public void toBytes(ByteBuf buf){
		DataUtils.writeBlockPos(buf, pos);
		byte[] b = e==null ? new byte[0] : e.toString().getBytes(Charsets.UTF_8);
		if(b.length>0){
			buf.writeInt(b.length);
			buf.writeBytes(b);
		}else buf.writeInt(0);
	}
	
	public static class Server implements IMessageHandler<MessageDebug, MessageDebug>{
		@Override
		public MessageDebug onMessage(MessageDebug message, MessageContext ctx){
			MessageDebug send = new MessageDebug(message.pos);
			TileEntity te = ctx.getServerHandler().player.world.getTileEntity(message.pos);
			if(te!=null){
				Debugable debugable = te.getCapability(Debugable.CAP, null);
				if(debugable!=null) send.e = debugable.getDebugInfo();
			}
			return send;
		}
	}
	
	public static class Client implements IMessageHandler<MessageDebug, IMessage>{
		@Override
		@SideOnly(Side.CLIENT)
		public IMessage onMessage(MessageDebug message, MessageContext ctx){
			FoolsCrowbarEvent.updateDebugEvent(message);
			return null;
		}
	}
}
