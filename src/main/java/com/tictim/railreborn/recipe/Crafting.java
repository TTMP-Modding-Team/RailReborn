package com.tictim.railreborn.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tictim.railreborn.RailReborn;
import com.tictim.railreborn.capability.Debugable;
import com.tictim.railreborn.item.IngredientStack;
import com.tictim.railreborn.util.NBTTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ITickable;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.stream.IntStream;

public final class Crafting implements ITickable, Debugable{
	private final Machine handler;
	
	@Nullable
	private String recipeKey;
	@Nullable
	private IngredientStack[] input;
	@Nullable
	private ItemStack[] output;
	@Nullable
	private FluidStack[] fluidInput, fluidOutput;
	
	private int totalTime, currentTime, timeIncrement = 1;
	
	private int energy;
	
	private boolean finalized;
	
	public Crafting(Machine handler){
		this.handler = handler;
	}
	
	/**
	 * It's just <code>new Crafting(c.handler).copy(c)</code>.
	 * @param c
	 */
	public Crafting(Crafting c){
		this(c.handler);
		copy(c);
	}
	
	public Crafting copy(Crafting c){
		this.recipeKey = c.recipeKey;
		this.input = copy(c.input);
		this.output = copy(c.output);
		this.fluidInput = copy(c.fluidInput);
		this.fluidOutput = copy(c.fluidOutput);
		this.totalTime = c.totalTime;
		this.currentTime = c.currentTime;
		this.timeIncrement = c.timeIncrement;
		this.energy = c.energy;
		this.finalized = c.finalized;
		return this;
	}
	
	public Crafting read(NBTTagCompound nbt){
		this.recipeKey = nbt.getString("recipeKey");
		Crafting c = MachineRecipes.INSTANCE.getCrafting(this.recipeKey);
		if(c!=null) copy(c);
		else RailReborn.LOGGER.warn("Couldn't find original recipe for Crafting '{}'", this.recipeKey);
		this.output = nbt.hasKey("output", NBTTypes.LIST) ? toItemStackArray(nbt.getTagList("output", NBTTypes.COMPOUND)) : null;
		//this.fluidInput = nbt.hasKey("fluidInput", NBTTypes.LIST) ? toFluidStackArray(nbt.getTagList("fluidInput", NBTTypes.COMPOUND)) : null;
		this.fluidOutput = nbt.hasKey("fluidOutput", NBTTypes.LIST) ? toFluidStackArray(nbt.getTagList("fluidOutput", NBTTypes.COMPOUND)) : null;
		this.totalTime = nbt.getInteger("totalTime");
		this.currentTime = nbt.getInteger("currentTime");
		this.timeIncrement = nbt.hasKey("timeIncrement", NBTTypes.INTEGER) ? nbt.getInteger("timeIncrement") : 1;
		this.energy = nbt.getInteger("energy");
		this.finalized = nbt.getBoolean("finalized");
		return this;
	}
	
	public NBTTagCompound serializeNBT(){
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("recipeKey", this.recipeKey);
		if(output!=null){
			NBTTagList list = new NBTTagList();
			for(ItemStack s: output) list.appendTag(s.serializeNBT());
			nbt.setTag("output", list);
		}
		if(fluidOutput!=null){
			NBTTagList list = new NBTTagList();
			for(FluidStack s: fluidOutput){
				NBTTagCompound subnbt = new NBTTagCompound();
				s.writeToNBT(subnbt);
				list.appendTag(subnbt);
			}
			nbt.setTag("fluidOutput", list);
		}
		if(totalTime>0) nbt.setInteger("totalTime", totalTime);
		if(currentTime>0) nbt.setInteger("currentTime", currentTime);
		if(timeIncrement!=1) nbt.setInteger("timeIncrement", timeIncrement);
		if(energy!=0) nbt.setInteger("energy", energy);
		if(finalized) nbt.setBoolean("finalized", true);
		return nbt;
	}
	
	private static ItemStack[] toItemStackArray(NBTTagList list){
		ItemStack[] arr = new ItemStack[list.tagCount()];
		for(int i = 0; i<arr.length; i++){
			arr[i] = new ItemStack(list.getCompoundTagAt(i));
		}
		return arr;
	}
	
	private static FluidStack[] toFluidStackArray(NBTTagList list){
		FluidStack[] arr = new FluidStack[list.tagCount()];
		for(int i = 0; i<arr.length; i++){
			arr[i] = FluidStack.loadFluidStackFromNBT(list.getCompoundTagAt(i));
		}
		return arr;
	}
	
	@Nullable
	private static <T> T[] copy(@Nullable T[] original){
		if(original==null) return null;
		else return Arrays.copyOf(original, original.length);
	}
	
	@Override
	public void update(){
		Step s = getStep();
		if(s==Step.DONE) return;
		else if(handler.interact(this)){
			if(s==Step.PROCESSING){
				if(handler.process(this)){
					currentTime += timeIncrement;
					if(!isProcessDone()) return;
				}
			}
			if(hasOutput()){
				handler.collectResult(this);
				if(hasOutput()) return;
			}
			if(!finalized){
				handler.finalize(this);
				finalized = true;
				cancel();
			}
		}
	}
	
	public Step getStep(){
		return isProcessDone() ? (hasOutput() ? Step.WAITING_FOR_SPACE : Step.DONE) : Step.PROCESSING;
	}
	
	public boolean isProcessDone(){
		return totalTime<=currentTime;
	}
	
	public double getProgress(){
		return isProcessDone() ? 1 : (double)currentTime/totalTime;
	}
	
	public boolean hasOutput(){
		if(output!=null&&output.length>0) for(ItemStack s: output)
			if(!s.isEmpty()) return true;
		if(fluidOutput!=null&&fluidOutput.length>0) for(FluidStack s: fluidOutput)
			if(s.amount>0) return true;
		return false;
	}
	
	public Crafting setRecipeKey(String recipeKey){
		this.recipeKey = recipeKey;
		return this;
	}
	
	public String getRecipeKey(){
		return recipeKey;
	}
	
	public Crafting setInput(IngredientStack... input){
		this.input = input==null||input.length==0 ? null : input;
		this.extractions = null;
		return this;
	}
	
	public Crafting setOutput(ItemStack... output){
		this.output = output==null||output.length==0 ? null : output;
		return this;
	}
	
	public Crafting setFluidInput(FluidStack... input){
		this.fluidInput = input==null||input.length==0 ? null : input;
		return this;
	}
	
	public Crafting setFluidOutput(FluidStack... output){
		this.fluidOutput = output==null||output.length==0 ? null : output;
		return this;
	}
	
	public IngredientStack[] getInput(){
		return input;
	}
	
	public ItemStack[] getOutput(){
		return output;
	}
	
	public FluidStack[] getFluidInput(){
		return fluidInput;
	}
	
	public FluidStack[] getFluidOutput(){
		return fluidOutput;
	}
	
	public Crafting setTotalTime(int totalTime){
		this.totalTime = totalTime;
		return this;
	}
	
	public Crafting setCurrentTime(int currentTime){
		this.currentTime = currentTime;
		return this;
	}
	
	public int getTotalTime(){
		return this.totalTime;
	}
	
	public int getCurrentTime(){
		return this.currentTime;
	}
	
	public Crafting setTimeIncrement(int timeIncrement){
		this.timeIncrement = timeIncrement;
		return this;
	}
	
	public int getTimeIncrement(){
		return timeIncrement;
	}
	
	public Crafting setEnergy(int energy){
		this.energy = energy;
		return this;
	}
	
	public int getEnergy(){
		return this.energy;
	}
	
	public void cancel(){
		this.setCurrentTime(this.totalTime);
		setOutput().setFluidOutput();
	}
	
	public void expectInputSize(int value){
		expect(this.input, value, value, "Unexpected input size %d, must be %d");
	}
	
	public void expectInputSize(int min, int max){
		expect(this.input, min, max, "Unexpected input size %d, must between %d and %d");
	}
	
	public void expectOutputSize(int value){
		expect(this.output, value, value, "Unexpected output size %d, must be %d");
	}
	
	public void expectOutputSize(int min, int max){
		expect(this.output, min, max, "Unexpected output size %d, must between %d and %d");
	}
	
	public void expectFluidInputSize(int value){
		expect(this.fluidInput, value, value, "Unexpected fluid input size %d, must be %d");
	}
	
	public void expectFluidInputSize(int min, int max){
		expect(this.fluidInput, min, max, "Unexpected fluid input size %d, must between %d and %d");
	}
	
	public void expectFluidOutputSize(int value){
		expect(this.fluidOutput, value, value, "Unexpected fluid output size %d, must be %d");
	}
	
	public void expectFluidOutputSize(int min, int max){
		expect(this.fluidOutput, min, max, "Unexpected fluid output size %d, must between %d and %d");
	}
	
	/**
	 * [input] [handler]
	 */
	@Nullable
	private int[][] extractions;
	
	public boolean extractInput(IItemHandler handler, boolean simulate){
		if(this.input!=null&&this.input.length>0){
			if(extractions!=null){
				if(extractions.length!=this.input.length||extractions[0].length!=handler.getSlots()) extractions = null;
				else{
					L0:
					for(int i = 0; i<this.extractions.length; i++){
						IngredientStack ing = input[i];
						int[] arr = this.extractions[i];
						if(IntStream.of(arr).sum()!=ing.getQuantity()){
							extractions = null;
							break;
						}
						for(int j = 0; j<arr.length; j++){
							ItemStack s = handler.extractItem(j, arr[j], true);
							if(s.getCount()<arr[j]||!ing.getIngredient().test(s)){
								extractions = null;
								break L0;
							}
						}
					}
				}
			}
			if(extractions==null){
				extractions = new int[this.input.length][handler.getSlots()];
				for(int i = 0; i<this.extractions.length; i++){
					IngredientStack ing = input[i];
					int[] arr = this.extractions[i];
					int stackLeft = ing.getQuantity();
					for(int j = 0; j<arr.length; j++){
						ItemStack s = handler.extractItem(j, 64, true);
						if(!s.isEmpty()&&ing.getIngredient().test(s)){
							stackLeft -= arr[j] = Math.min(stackLeft, s.getCount());
						}
					}
					if(stackLeft>0){
						extractions = null;
						return false;
					}
				}
			}
			
			if(!simulate) for(int i = 0, i0 = handler.getSlots(); i<i0; i++){
				int req = 0;
				for(int j = 0; j<this.extractions.length; j++)
					req += this.extractions[j][i];
				if(req>0&&handler.extractItem(i, req, false).getCount()!=req) RailReborn.LOGGER.error("Unexpected extractInput operation occured. ");
			}
		}
		return true;
	}
	
	// TODO Fix this later, now im completely exhausted
	public boolean insertOutput(IItemHandler handler, boolean simulate){
		if(this.output!=null&&this.output.length>0){
			for(int i = 0; i<this.output.length; i++){
				ItemStack s = this.output[i].copy();
				for(int j = 0, j0 = handler.getSlots(); j<j0; j++){
					s = handler.insertItem(j, s, simulate);
					if(s.isEmpty()) break;
				}
				if(!simulate) this.output[i] = s;
				if(!s.isEmpty()) return false;
			}
		}
		if(!simulate) this.output = null;
		return true;
	}
	
	// TODO Fix this later, now im completely exhausted
	public boolean extractFluidInput(IFluidHandler handler, boolean simulate){
		if(this.fluidInput!=null&&this.fluidInput.length>0){
			for(int i = 0; i<this.fluidInput.length; i++){
				FluidStack s = this.fluidInput[i];
				if(s.amount>0){
					FluidStack s2 = handler.drain(s, !simulate);
					if(s.amount>s2.amount) return false;
				}
			}
		}
		return true;
	}
	
	// TODO Fix this later, now im completely exhausted
	public boolean insertFluidOutput(IFluidHandler handler, boolean simulate){
		if(this.fluidOutput!=null&&this.fluidOutput.length>0){
			for(int i = 0; i<this.fluidOutput.length; i++){
				FluidStack s = this.fluidOutput[i];
				if(s.amount>0){
					int amount = handler.fill(s, !simulate);
					if(!simulate&&amount>0) (this.fluidOutput[i] = s.copy()).amount -= amount;
					if(s.amount>amount) return false;
				}
			}
			if(!simulate) this.fluidOutput = null;
		}
		return true;
	}
	
	public boolean requiresLeastOne(ItemStack stack){
		if(!stack.isEmpty()&&this.input!=null&&this.input.length>0) for(IngredientStack ing: input)
			if(ing.getIngredient().test(stack)) return true;
		return false;
	}
	
	public boolean requiresLeastOne(Fluid fluid){
		if(this.fluidInput!=null&&this.fluidInput.length>0) for(FluidStack s: fluidInput)
			if(s.getFluid()==fluid) return true;
		return false;
	}
	
	private static void expect(@Nullable Object[] arr, int min, int max, String error){
		int size = arr==null ? 0 : arr.length;
		if(size<min||size>max) throw new IllegalStateException(String.format(error, size, min, max));
	}
	
	@Override
	public JsonElement getDebugInfo(){
		JsonObject obj = new JsonObject();
		obj.addProperty("Recipe Key", this.recipeKey);
		if(this.input!=null) obj.add("Input", Debugable.debugInstances(this.input));
		if(this.output!=null) obj.add("Output", Debugable.debugItemStacks(this.output));
		if(this.fluidInput!=null) obj.add("Fluid Input", Debugable.debugFluidStacks(this.fluidInput));
		if(this.fluidOutput!=null) obj.add("Fluid Output", Debugable.debugFluidStacks(this.fluidOutput));
		if(this.energy!=0) obj.addProperty("'''Energy Thing'''", this.energy);
		obj.addProperty("Total Ticks", this.totalTime);
		obj.addProperty("Current Ticks", this.currentTime);
		if(this.timeIncrement!=1) obj.addProperty("Tick Increment", this.timeIncrement);
		return obj;
	}
	
	@Override
	public String toString(){
		return Debugable.toDebugString(getDebugInfo());
	}
	
	public enum Step{
		PROCESSING,
		WAITING_FOR_SPACE,
		DONE;
	}
}
