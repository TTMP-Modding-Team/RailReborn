package com.tictim.railreborn.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.tictim.railreborn.capability.Debugable;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public final class DebugJsonBuilder implements Debugable{
	private final LinkedList<JsonArray> elements = new LinkedList<>();
	@Nullable
	private JsonObject latestObject = null;
	@Nullable
	private String key;
	
	public DebugJsonBuilder(){
		elements.add(new JsonArray());
	}
	
	public DebugJsonBuilder(@Nullable Object obj){
		this();
		add(obj);
	}
	
	public DebugJsonBuilder add(@Nullable Object obj){
		if(obj==null) return add(null, DebugFormat.NULL);
		else if(obj instanceof Class<?>) return add((Class)obj);
		else if(obj instanceof Debugable) return add((Debugable)obj);
		else if(obj instanceof JsonElement) return add((JsonElement)obj);
		else if(obj instanceof ResourceLocation) return add((ResourceLocation)obj);
		else if(obj instanceof BlockPos) return add((BlockPos)obj);
		else if(obj instanceof ItemStack) return add((ItemStack)obj);
		else if(obj instanceof FluidStack) return add((FluidStack)obj);
		else if(obj instanceof IInventory) return add((IInventory)obj);
		else if(obj instanceof IFluidTank) return add((IFluidTank)obj);
		else if(obj instanceof Boolean) return add(((Boolean)obj).booleanValue());
		else if(obj instanceof Byte) return add(((Byte)obj).byteValue());
		else if(obj instanceof Short) return add(((Short)obj).shortValue());
		else if(obj instanceof Character) return add(((Character)obj).charValue());
		else if(obj instanceof Integer) return add(((Integer)obj).intValue());
		else if(obj instanceof Long) return add(((Long)obj).longValue());
		else if(obj instanceof Double) return add(((Double)obj).doubleValue());
		else if(obj instanceof Float) return add(((Float)obj).floatValue());
		else return add(obj, DebugFormat.DEFAULT);
	}
	
	public DebugJsonBuilder add(@Nullable Class<?> classOf){
		return add(classOf==null ? null : classOf.getSimpleName(), DebugFormat.CLASS);
	}
	
	public DebugJsonBuilder add(@Nullable Debugable debugable){
		return debugable==null ? add("null", DebugFormat.NULL) : append(debugable.getDebugInfo());
	}
	
	public DebugJsonBuilder add(JsonElement debug){
		return append(debug);
	}
	
	public DebugJsonBuilder add(ResourceLocation loc){
		return add(loc, DebugFormat.ID);
	}
	
	public DebugJsonBuilder add(BlockPos pos){
		return append(new JsonPrimitive("("+DebugFormat.NUMBER.format(pos.getX()+" "+pos.getY()+" "+pos.getZ())+")"));
	}
	
	public DebugJsonBuilder add(ItemStack stack){
		if(stack.isEmpty()) return add("Empty", DebugFormat.NULL);
		else{
			Item i = stack.getItem();
			StringBuilder stb = new StringBuilder(stack.getItem().getRegistryName().toString());
			if(i.getHasSubtypes()||i.isDamageable()) stb.append("@").append(DebugFormat.NUMBER.format(stack.getItemDamage()));
			return append(new JsonPrimitive(stb.append(" * ").append(DebugFormat.NUMBER.format(stack.getCount())).toString()));
		}
	}
	
	public DebugJsonBuilder add(@Nullable FluidStack fluid){
		return append(new JsonPrimitive(fluid==null||fluid.amount<=0 ? DebugFormat.NULL.format("Empty") : fluid.getFluid().getName()+" * "+DebugFormat.NUMBER.format(fluid.amount)));
	}
	
	public DebugJsonBuilder add(IInventory inv){
		DebugJsonBuilder b = new DebugJsonBuilder();
		for(int i = 0, j = inv.getSizeInventory(); i<j; i++) b.add(inv.getStackInSlot(i));
		return add(b);
	}
	
	public DebugJsonBuilder add(IFluidTank tank){
		FluidStack fluid = tank.getFluid();
		boolean isEmpty = fluid==null||fluid.amount<=0;
		String fluidStr = isEmpty ? DebugFormat.NULL.format("Empty") : fluid.getFluid().getName();
		return append(new JsonPrimitive(fluidStr+" ( "+DebugFormat.NUMBER.format(isEmpty ? 0 : fluid.amount)+" / "+DebugFormat.NUMBER.format(tank.getCapacity())+" )"));
	}
	
	public DebugJsonBuilder add(boolean b){
		return add(b ? "Yes" : "No", b ? DebugFormat.TRUE : DebugFormat.FALSE);
	}
	
	public DebugJsonBuilder add(byte b){
		return add(b, DebugFormat.NUMBER);
	}
	
	public DebugJsonBuilder add(short s){
		return add(s, DebugFormat.NUMBER);
	}
	
	public DebugJsonBuilder add(char c){
		return add(c, DebugFormat.CHAR);
	}
	
	public DebugJsonBuilder add(int i){
		return add(i, DebugFormat.NUMBER);
	}
	
	public DebugJsonBuilder add(long l){
		return add(l, DebugFormat.NUMBER);
	}
	
	public DebugJsonBuilder add(double d){
		return add(d, DebugFormat.NUMBER);
	}
	
	public DebugJsonBuilder add(float f){
		return add(f, DebugFormat.NUMBER);
	}
	
	public DebugJsonBuilder add(@Nullable Object obj, DebugFormat format){
		return append(new JsonPrimitive((obj==null ? DebugFormat.NULL : format).format(obj)));
	}
	
	public DebugJsonBuilder append(JsonElement json){
		if(key==null){
			elements.getLast().add(json);
			latestObject = null;
		}else{
			JsonObject o = new JsonObject();
			o.add(key, json);
			elements.getLast().add(o);
			latestObject = o;
			key = null;
		}
		return this;
	}
	
	public DebugJsonBuilder key(String key){
		this.key = key;
		return this;
	}
	
	public DebugJsonBuilder moveFloor(){
		if(key!=null){
			String key2 = key;
			key = null;
			JsonObject o = new JsonObject();
			append(o);
			JsonArray a = new JsonArray();
			o.add(key2, a);
			elements.add(a);
		}else if(latestObject!=null){
			JsonArray a = new JsonArray();
			elements.add(a);
			latestObject.add("", a);
		}
		return this;
	}
	
	public DebugJsonBuilder prevFloor(){
		if(elements.size()>1){
			JsonArray a = elements.removeLast();
			if(a.size()==0) for(JsonElement json: elements.getLast()){
				if(json.isJsonObject()){
					Iterator<Map.Entry<String, JsonElement>> it = json.getAsJsonObject().entrySet().iterator();
					while(it.hasNext()){
						if(it.next().getValue()==a){
							it.remove();
							break;
						}
					}
				}
			}
		}
		return this;
	}
	
	@Override
	public JsonElement getDebugInfo(){
		return elements.getFirst();
	}
	
	public enum DebugFormat{
		DEFAULT(""),
		CLASS(TextFormatting.BOLD.toString()),
		NUMBER(TextFormatting.GOLD.toString()),
		TRUE(TextFormatting.GREEN.toString()),
		FALSE(TextFormatting.RED.toString()),
		ID(TextFormatting.GREEN.toString()),
		CHAR(TextFormatting.YELLOW.toString()),
		NULL(TextFormatting.GRAY.toString());
		
		private final String format;
		
		DebugFormat(String format){
			this.format = format;
		}
		
		public String getFormat(){
			return format;
		}
		
		public String format(Object obj){
			return getFormat()+obj+TextFormatting.RESET;
		}
	}
}
