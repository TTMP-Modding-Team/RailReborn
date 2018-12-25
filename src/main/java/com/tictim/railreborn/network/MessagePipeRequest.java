package com.tictim.railreborn.network;

import com.tictim.railreborn.RailReborn;
import com.tictim.railreborn.tileentity.TEPipe;
import com.tictim.railreborn.util.DataUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

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
			MinecraftServer s = FMLCommonHandler.instance().getMinecraftServerInstance();
			s.addScheduledTask(() -> {
				EntityPlayerMP player = ctx.getServerHandler().player;
				World world = player.world;
				TileEntity te = world.getTileEntity(message.pos);
				if(te instanceof TEPipe){
					MessagePipeData m = new MessagePipeData(message.pos, ((TEPipe)te).getNode().getLink());
					RailReborn.NET.sendTo(m, player);
				}
			});
			return null;
		}
	}
}
