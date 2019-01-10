package com.tictim.railreborn.network;

import com.tictim.railreborn.client.event.FoolsGoggleEvent;
import com.tictim.railreborn.util.DataUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @deprecated
 */
public class MessagePipeData implements IMessage{
	public BlockPos pos;
	
	public MessagePipeData(){}
	
	public MessagePipeData(BlockPos pos){
		this.pos = pos;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void fromBytes(ByteBuf buf){
		pos = DataUtils.readBlockPos(buf);
		//(link = new PipeLink(WPLCache.INSTANCE)).deserializeNBT(ByteBufUtils.readTag(buf));
	}
	
	@Override
	public void toBytes(ByteBuf buf){
		DataUtils.writeBlockPos(buf, pos);
		//ByteBufUtils.writeTag(buf, link.serializeNBT());
	}
	
	public static class Handler implements IMessageHandler<MessagePipeData, IMessage>{
		@Override
		@SideOnly(Side.CLIENT)
		public IMessage onMessage(MessagePipeData message, MessageContext ctx){
			FoolsGoggleEvent.update(message);
			return null;
		}
	}
}
