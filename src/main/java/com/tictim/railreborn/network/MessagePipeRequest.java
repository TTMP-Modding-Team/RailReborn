package com.tictim.railreborn.network;

import com.tictim.railreborn.util.DataUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @deprecated
 */
public class MessagePipeRequest implements IMessage{
	BlockPos pos;
	
	public MessagePipeRequest(){}
	public MessagePipeRequest(BlockPos pos){
		this.pos = pos;
	}
	
	@Override
	public void fromBytes(ByteBuf buf){
		pos = DataUtils.readBlockPos(buf);
	}
	
	@Override
	public void toBytes(ByteBuf buf){
		DataUtils.writeBlockPos(buf, pos);
	}
	
	public static class Handler implements IMessageHandler<MessagePipeRequest, IMessage>{
		@Override
		public IMessage onMessage(MessagePipeRequest message, MessageContext ctx){
			/*
			MinecraftServer s = FMLCommonHandler.instance().getMinecraftServerInstance();
			s.addScheduledTask(() -> {
				EntityPlayerMP player = ctx.getServerHandler().player;
				TileEntity te = player.world.getTileEntity(message.pos);
				if(te instanceof TEPipe){
					PipeNode node = ((TEPipe)te).getNode();
					if(node!=null){
						MessagePipeData m = new MessagePipeData(message.pos, node.getLink());
						RailReborn.NET.sendTo(m, player);
					}
				}
			});
			*/
			return null;
		}
	}
}
