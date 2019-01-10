package com.tictim.railreborn.pipelink.attachment;

import com.tictim.railreborn.RailReborn;
import com.tictim.railreborn.Registries;
import com.tictim.railreborn.item.ModItems;
import com.tictim.railreborn.pipelink.attachment.provider.PipeAttachmentProvider;
import com.tictim.railreborn.pipelink.attachment.provider.PipeAttachmentProviderDefault;
import com.tictim.railreborn.pipelink.attachment.provider.PipeAttachmentProviderInvalid;
import com.tictim.railreborn.util.NBTTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import static com.tictim.railreborn.pipelink.attachment.provider.PipeAttachmentProvider.ConnectionRequirement.MUST_BE_DISCONNCTED;

@Mod.EventBusSubscriber(modid = RailReborn.MODID)
public final class PipeAttachments{
	private PipeAttachments(){}
	
	public static final PipeAttachmentProvider INVALID = new PipeAttachmentProviderInvalid().setRegistryName("invalid").setCollectible(false);
	public static final PipeAttachmentProvider DEFAULT = new PipeAttachmentProviderDefault().setRegistryName("default").setCollectible(false).setConnectionRequirement(MUST_BE_DISCONNCTED);
	
	@SubscribeEvent
	public static void createRegistry(RegistryEvent.NewRegistry event){}
	
	@SubscribeEvent
	public static void register(RegistryEvent.Register<PipeAttachmentProvider> event){
		IForgeRegistry<PipeAttachmentProvider> registry = event.getRegistry();
		registry.register(INVALID);
		registry.register(DEFAULT);
	}
	
	public static PipeAttachmentProvider deserialize(ItemStack stack){
		if(stack.getItem()!=ModItems.PIPE_ATTACHMENTS) return INVALID;
		NBTTagCompound nbt = stack.getTagCompound();
		if(nbt!=null&&nbt.hasKey("attachment", NBTTypes.STRING)){
			PipeAttachmentProvider a = Registries.PIPE_ATTACHMENTS.getValue(new ResourceLocation(nbt.getString("attachment")));
			return a.isCollectible() ? a : INVALID;
		}else return INVALID;
	}
}
